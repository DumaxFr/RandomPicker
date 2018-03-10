package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import bot.group.Group;
import bot.group.GroupManager;
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
public class TeamFromGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public TeamFromGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public TeamFromGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        return new TeamFromGroupCommand(this.validExpressions, this.permission, this.bot, this.main);
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        boolean exceptUser = false;
        boolean slowmode = false;
        int numberOfTeams = 0;
        int delay = 5000;
        List<List<IUser>> teams = new ArrayList<List<IUser>>();
        String[] parts = event.getMessage().getContent().split(" ");
        String groupName = "";
        if (parts.length > 2)
        {
            try
            {
                numberOfTeams = Integer.parseInt(parts[1]);
                if (numberOfTeams <= 0)
                {
                    numberOfTeams = 2;
                }
                groupName = parts[2];
                
                if (parts.length >= 4)
                {
                    slowmode = parts[3].trim().contains("slow");
                    
                    if (event.getMessage().getContent().replace("slow (", "slow(").contains("slow("))
                    {
                        String message = event.getMessage().getContent().replace("slow (", "slow(");
                        for (String s : message.split(" "))
                        {
                            if (s.contains("slow("))
                            {
                                try
                                {
                                    delay = Integer.parseInt(s.replace("slow(", "").replace(")", "").trim()) * 1000;
                                    
                                    if (delay > 300000)
                                    {
                                        this.bot.sendMessage("The slowmode delay time may not be higher than 300 seconds. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                        delay = 5000;
                                    }
                                    if (delay < 0)
                                    {
                                        this.bot.sendMessage("The slowmode delay time may not be lower than 0 seconds. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                        delay = 5000;
                                    }
                                }
                                catch (NumberFormatException e)
                                {
                                    this.bot.sendMessage("Wrong format for the desired slowmode delay time. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                    delay = 5000;
                                }
                            }
                        }
                    }
                }
            }
            catch (NumberFormatException e)
            {
                numberOfTeams = 2;
                groupName = parts[1];
                
                if (parts.length >= 3)
                {
                    slowmode = parts[2].trim().contains("slow");
                    
                    if (event.getMessage().getContent().replace("slow (", "slow(").contains("slow("))
                    {
                        String message = event.getMessage().getContent().replace("slow (", "slow(");
                        for (String s : message.split(" "))
                        {
                            if (s.contains("slow("))
                            {
                                try
                                {
                                    delay = Integer.parseInt(s.replace("slow(", "").replace(")", "").trim()) * 1000;
                                    
                                    if (delay > 300000)
                                    {
                                        this.bot.sendMessage("The slowmode delay time may not be higher than 300 seconds. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                        delay = 5000;
                                    }
                                    if (delay < 0)
                                    {
                                        this.bot.sendMessage("The slowmode delay time may not be lower than 0 seconds. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                        delay = 5000;
                                    }
                                }
                                catch (NumberFormatException ex)
                                {
                                    this.bot.sendMessage("Wrong format for the desired slowmode delay time. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                    delay = 5000;
                                }
                            }
                        }
                    }
                }
            }
            
        }
        else
        {
            numberOfTeams = 2;
            if (parts.length < 2)
            {
                this.bot.sendMessage("You have to add the name of the team that you want to pick from.", event.getMessage().getChannel(), Colors.RED);
                return;
            }
            else
            {
                groupName = parts[1];
                
                if (parts.length >= 3)
                {
                    slowmode = parts[2].trim().contains("slow");
                    
                    if (event.getMessage().getContent().replace("slow (", "slow(").contains("slow("))
                    {
                        String message = event.getMessage().getContent().replace("slow (", "slow(");
                        for (String s : message.split(" "))
                        {
                            if (s.contains("slow("))
                            {
                                try
                                {
                                    delay = Integer.parseInt(s.replace("slow(", "").replace(")", "").trim()) * 1000;
                                    
                                    if (delay > 300000)
                                    {
                                        this.bot.sendMessage("The slowmode delay time may not be higher than 300 seconds. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                        delay = 5000;
                                    }
                                    if (delay < 0)
                                    {
                                        this.bot.sendMessage("The slowmode delay time may not be lower than 0 seconds. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                        delay = 5000;
                                    }
                                }
                                catch (NumberFormatException e)
                                {
                                    this.bot.sendMessage("Wrong format for the desired slowmode delay time. The bot will use the default 5 second delay.", event.getMessage().getChannel(), Colors.RED);
                                    delay = 5000;
                                }
                            }
                        }
                    }
                }
            }
        }
        GroupManager manager = GroupManager.getManagerForGuild(event.getGuildObject());
        Group group = manager.getGroupByName(groupName);
        if (group == null)
        {
            this.bot.sendMessage("Make sure to open a group by using the create command first.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        List<IUser> users = new ArrayList<>();
        users.addAll(group.getMembers());
        
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
        if (!slowmode)
        {
            while (!users.isEmpty())
            {
                try
                {
                    Thread.sleep(r.nextInt(50));
                }
                catch (InterruptedException e)
                {
                }
                if (currentTeam == teams.size())
                {
                    currentTeam = 0;
                }
                num = r.nextInt(users.size());
                teams.get(currentTeam).add(users.remove(num));
                currentTeam++;
                try
                {
                    Thread.sleep(r.nextInt(128));
                }
                catch (InterruptedException e)
                {
                    Bot.errorLog.print(this, e);
                }
            }
            List<String> mentions = null;
            for (int i = 0; i < teams.size(); i++)
            {
                mentions = new ArrayList<String>();
                for (IUser user : teams.get(i))
                {
                    mentions.add(user.mention() + " \n(" + user.getDisplayName(event.getGuildObject().getGuild()) + ")");
                }
                this.bot.sendListMessage("Team " + (i + 1), mentions, event.getChannel(), 25, true);
            }
        }
        else //slowmode
        {
            //setting up teammessages
            List<IMessage> messages = new ArrayList<>();
            for (int i = 0; i < teams.size(); i++)
            {
                messages.add(this.bot.sendMessage("Team " + (i + 1), event.getChannel()));
            }
            
            List<String> mentions = null;
            
            while (!users.isEmpty())
            {
                try
                {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e)
                {
                    Bot.errorLog.print(this, e);
                }
                
                if (currentTeam == teams.size())
                {
                    currentTeam = 0;
                }
                num = r.nextInt(users.size());
                teams.get(currentTeam).add(users.remove(num));
                
                mentions = new ArrayList<String>();
                for (IUser user : teams.get(currentTeam))
                {
                    mentions.add(user.mention() + " \n(" + user.getDisplayName(event.getGuildObject().getGuild()) + ")");
                }
                
                final int team = currentTeam;
                final List<String> finalMentions = mentions;
                
                RequestBuffer.request(() -> messages.get(team).edit(this.bot.createListEmbeds("Team " + (team + 1), finalMentions, 25, true).get(0))).get();
                
                currentTeam++;
            }
        }
        this.bot.sendMessage("The teams are finished, master.", event.getMessage().getChannel(), Colors.GREEN);
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Team Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Forms teams from the users that joined the given group. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix()+"teamgroup 4 group1\n\n"
                + "The 4 indicates that you want 4 teams consisting of the users in 'group1'. You can change that number to anything above 0. "
                + "If you leave it out, the bot will create 2 teams by default. \n\n\n"
                + "If you add the word 'slow' AFTER the desired number of teams and the groupname the bot will fill the teams with a default delay of 5 seconds "
                + "between every user. \n\n"
                + Bot.getPrefix()+"teamgroup 4 group1 slow\n\n"
                + "To use a custom delay between 0 and 300 seconds simply add the number with parenthesis right after the slow keyword.\n\n"
                + Bot.getPrefix()+"teamgroup 4 group1 slow (10)\n\n"
                + "To have the bot use a 10 second delay between each user."
                + "```";
    }
}