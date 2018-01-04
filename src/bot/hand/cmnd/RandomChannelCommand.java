package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
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
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public RandomChannelCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
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
            this.bot.sendMessage("You have to be in a voicechannel.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        for(IUser user : allUsers)
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
        Random r = new Random();
        try
        {
            Thread.sleep(r.nextInt(50));
        }
        catch (InterruptedException e)
        {
        }
        int num = r.nextInt(users.size());
        this.bot.sendMessage(users.get(num).mention(), event.getChannel(), Colors.PURPLE);
        Main.channelLog.print("Picked option " + (num + 1) + " out of " + users.size() + ".");
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Random User In Channel Command \n"   
                + "<User> \n\n"
                + "Picks a random non-bot user that is currently in the same voicechannel as you. \n"
                + "You can extend the command like so '"+Bot.getPrefix()+"channel n' so it wont choose you."
                + "```";
    }
}