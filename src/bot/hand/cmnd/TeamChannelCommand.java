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
public class TeamChannelCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public TeamChannelCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public TeamChannelCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        return new TeamChannelCommand(this.validExpressions, this.permission, this.bot, this.main);
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        boolean exceptUser = false;
        int numberOfTeams = 0;
        List<List<IUser>> teams = new ArrayList<List<IUser>>();
        String[] parts = event.getMessage().getContent().split(" ");
        if (parts.length > 1 && parts[1].trim().equals("n"))
        {
            exceptUser = true;
            if (parts.length > 2)
            {
                try
                {
                    numberOfTeams = Integer.parseInt(parts[2]);
                    if (numberOfTeams <= 0)
                    {
                        numberOfTeams = 2;
                    }
                }
                catch (NumberFormatException e)
                {
                    numberOfTeams = 2;
                }
            }
        }
        else if (parts.length > 1)
        {
            try
            {
                numberOfTeams = Integer.parseInt(parts[1]);
                if (numberOfTeams <= 0)
                {
                    numberOfTeams = 2;
                }
            }
            catch (NumberFormatException e)
            {
                numberOfTeams = 2;
            }
        }
        else
        {
            numberOfTeams = 2;
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
        if (users.size() < numberOfTeams)
        {
            this.bot.sendMessage("You don't have enough users for the teams.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        for (int i = 0; i < numberOfTeams; i++)
        {
            teams.add(new ArrayList<IUser>());
        }
        Random r = new Random();
        int num = 0;
        int currentTeam = 0;
        while (!users.isEmpty())
        {
            if (currentTeam == teams.size())
            {
                currentTeam = 0;
            }
            num = r.nextInt(users.size());
            Main.channelLog.print("Picked option " + (num + 1) + " out of " + users.size() + ".");
            teams.get(currentTeam).add(users.remove(num));
            currentTeam++;
            try
            {
                Thread.sleep((long)r.nextInt(128));
            }
            catch (InterruptedException e)
            {
                Main.errorLog.print(e);
            }
        }
        List<String> mentions = null;
        for (int i = 0; i < teams.size(); i++)
        {
            mentions = new ArrayList<String>();
            for (IUser user : teams.get(i))
            {
                mentions.add(user.mention());
            }
            this.bot.sendListMessage("Team " + (i + 1), mentions, event.getChannel(), 25, true);
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Team Channel Command \n"   
                + "<User> \n\n"
                + "Forms teams from the users that are connected to the same voicechannel as you. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix()+"teamchannel 4 \n\n"
                + "The 4 indicates that you want 4 teams. You can change that number to anything above 0. "
                + "If you leave it out, the bot will create 2 teams by default. \n\n\n"
                + "You can also extend the command like so:\n\n"
                + Bot.getPrefix()+"teamchannel n 4 \n\n"
                + "This will exclude you from the teams."
                + "```";
    }
}