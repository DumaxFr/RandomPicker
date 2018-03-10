package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.StatusType;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class RandomChannelCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public RandomChannelCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public RandomChannelCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new RandomChannelCommand(this.validExpressions, this.permission, this.bot, this.main);
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        boolean exceptUser = false;
        String[] parts = event.getMessage().getContent().split(" ");
        if (parts.length > 1 && parts[1].trim().equals("n"))
        {
            exceptUser = true;
        }
        List<IUser> allUsers = null;
        List<IUser> users = new ArrayList<IUser>();
        
        if (event.getMessage().getChannelMentions().isEmpty())
        {
            List<IVoiceChannel> channels = event.getGuildObject().getGuild().getVoiceChannels();
            IVoiceChannel wantedChannel = null;
            for(IVoiceChannel voiceChannel : channels){
                if(voiceChannel.getConnectedUsers().contains(event.getAuthor())){
                    wantedChannel = voiceChannel;
                }
            }
            if(wantedChannel != null){
                allUsers = wantedChannel.getConnectedUsers();
            }else{
                this.bot.sendMessage("You have to be in a voicechannel or tag a textchannel with a #.", event.getMessage().getChannel(), Colors.RED);
                return;
            }
        }
        else
        {
            try
            {
                allUsers = event.getMessage().getChannelMentions().get(0).getUsersHere();
            }
            catch (Exception e)
            {
                Bot.errorLog.print(e);
            }
        }
        for(IUser user : allUsers)
        {
            if (event.getMessage().getContent().contains("online"))
            {
                if (!user.isBot() && !user.getPresence().getStatus().equals(StatusType.OFFLINE) && !user.getPresence().getStatus().equals(StatusType.INVISIBLE))
                {
                    if (exceptUser && user.equals(event.getAuthor()))
                    {
                        
                    }
                    else
                    {
                        users.add(user);
                    }
                }
            }
            else
            {
                if (!user.isBot())
                {
                    if (exceptUser && user.equals(event.getAuthor()))
                    {
                        
                    }
                    else
                    {
                        users.add(user);
                    }
                }
            }
        }
        Random r = new Random();
        try
        {
            Thread.sleep(r.nextInt(50));
        }
        catch (InterruptedException e)
        {
        }
        if (users.size() > 0)
        {
            int num = r.nextInt(users.size());
            this.bot.sendMessage(users.get(num).mention() + " \n(" + users.get(num).getDisplayName(event.getGuildObject().getGuild()) + ")", event.getChannel(), Colors.PURPLE);
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Random User In Channel Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Picks a random non-bot user from a channel. \n\n"
                + "Usage:\n"
                + "To pick from a voicechannel simply use '"+Bot.getPrefix()+"channel' while connected to that channel.\n\n"
                + "To pick from users that have read permissions in a certain textchannel tag the channel with a # after the command like so:\n\n"
                + Bot.getPrefix()+"channel #channel\n\n\n"
                + "You can extend the command like this '"+Bot.getPrefix()+"channel n' or '"+Bot.getPrefix()+"channel n #channel'so it wont choose you.\n\n\n"
                + "If you only want to pick users from textchannels that are currently online add the word 'online' after the channel tag.\n\n"
                + Bot.getPrefix()+"channel #channel online"
                + "```";
    }
}