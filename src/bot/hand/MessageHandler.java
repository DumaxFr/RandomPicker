package bot.hand;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import core.Main;

/**
 * @author &#8904
 *
 */
public class MessageHandler implements IListener<MessageReceivedEvent>
{
    private Bot bot;
    /** Threadpool which provides threads when needed to handle events. */
    private ExecutorService executor;
    private Main main;
    
    public MessageHandler(Bot bot, Main main)
    {
        this.bot = bot;
        this.main = main;
        executor = Executors.newCachedThreadPool();
    }

    /**
     * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
     */
    @Override
    public void handle(MessageReceivedEvent event)
    {
        executor.execute(new Runnable()
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
                        if(!hasMuteRole(event.getAuthor(), event.getGuild()))
                        {
                            if(text.toLowerCase().trim().startsWith(Bot.getPrefix()))
                            {
                                //commands in guild channels
                                CommandEvent event = new CommandEvent(guildObject, message);
                                if (guildObject.getCommandHandler().dispatch(event))
                                {
                                    Main.channelLog.print("'" + event.getCommand() + "' command was used on '" + guildObject.getGuild().getName() + "'.");
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