package bot.hand;

import java.util.List;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.thread.Threads;
import core.Main;

/**
 * @author &#8904
 *
 */
public class MessageHandler implements IListener<MessageReceivedEvent>
{
    private Bot bot;
    private Main main;
    
    public MessageHandler(Bot bot, Main main)
    {
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
     */
    @Override
    public void handle(MessageReceivedEvent event)
    {
        if (this.bot.isReady())
        {
            Threads.cachedPool.execute(new Runnable()
            {
                private IMessage message = event.getMessage();
                
                @Override
                public void run()
                {
                    String text = message.getContent();
                    if (!bot.isBanned(event.getAuthor()))
                    {
                        if(!event.getChannel().isPrivate())
                        {
                            GuildObject guildObject = bot.getGuildObjectByID(event.getGuild().getStringID());
                            if (guildObject == null)
                            {
                                guildObject = GuildSetupHandler.setupGuildObject(event.getGuild(), main, bot);
                            }
                            if(!hasMuteRole(event.getAuthor(), event.getGuild()))
                            {
                                if(text.toLowerCase().trim().startsWith(Bot.getPrefix()))
                                {
                                    //commands in guild channels
                                    CommandEvent event = new CommandEvent(guildObject, message);
                                    if (guildObject.getCommandHandler().dispatch(event))
                                    {
                                        Bot.log.print(this, "'" + event.getCommand() + "' command was used on '" + guildObject.getGuild().getName() + "'.\n"
                                                + "Input: " + text);
                                    }
                                }
                            }
                        }
                    }
                }
                
                private boolean hasMuteRole(IUser user, IGuild guild)
                {
                    List<IRole> roles = guild.getRolesByName("R Silenced");
                    if (!roles.isEmpty())
                    {
                        return user.getRolesForGuild(guild).contains(roles.get(0)); 
                    }
                    return false;
                }
            });
        }
    }  
}