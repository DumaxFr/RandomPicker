package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.util.RequestBuffer;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cmnd.CommandCooldown;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.hand.impl.GuildCommandHandler;
import core.Main;

/**
 * @author &#8904
 *
 */
public class HelpCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public HelpCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public HelpCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event) 
    {
        String type = event.getMessage().getContent().toLowerCase().replace(Bot.getPrefix()+"help", "").trim();
        String helpMessage = "";
        if (type.equals(""))
        {
            helpMessage = getHelp();
        }
        else if (type.equals("randomuser"))
        {
            helpMessage = getRandomUserHelp();
        }
        else if (type.equals("groups"))
        {
            helpMessage = getGroupHelp();
        }
        else if (type.equals("teams"))
        {
            helpMessage = getTeamHelp();
        }
        else if (type.equals("diceroll"))
        {
            helpMessage = getRollHelp();
        }
        else if (type.equals("masters"))
        {
            helpMessage = getMastersHelp();
        }
        else if (type.equals("overrideperms"))
        {
            helpMessage = getOverrideHelp();
        }
        else
        {
            GuildObject guild = event.getGuildObject();
            List<Command> commands = ((GuildCommandHandler)guild.getCommandHandler()).getCommands();
            for (Command command : commands)
            {
                if (command.isValidExpression(type))
                {
                    helpMessage = command.getHelp();
                    Bot.log.print(this, "Command '"+type+"' help was send on '"+event.getGuildObject().getGuild().getName()+"'.");
                }
            }
        }
        String message = helpMessage;
        if (message.equals(""))
        {
            return;
        }
        new CommandCooldown(this, 1500).startTimer();
        try
        {
            RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(message));
        }
        catch(Exception e)
        {
            Bot.errorLog.print(this, e);
        }
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "**```"
                + "Help \n\n"
                + "This is a Random bot to pick random users/numbers or form random teams. Written by Lukas&#8904. \n\n"
                + "Use the phrases below to get more detailed information about how the bot works. \n"
                + Bot.getPrefix()+" is the prefix for every command. \n\n\n"
                + Bot.getPrefix()+"help randomuser         |       How to make the bot mention a random user. \n\n"
                + Bot.getPrefix()+"help groups             |       How to pick from a group of people. \n\n"
                + Bot.getPrefix()+"help teams              |       How to create randomized teams. \n\n"
                + Bot.getPrefix()+"help diceroll           |       How to roll a random number within a certain range. \n\n"
                + Bot.getPrefix()+"help masters            |       How the master system works. \n\n"
                + Bot.getPrefix()+"help overrideperms      |       How to change the needed permission level of certain commands.\n\n\n"
                + "You can read a full description with all the features and special options of every single command by typing '" + Bot.getPrefix() + "help commandname'. "
                + "(Replace 'commandname' with the name of the command that you want to read more about, for example '" + Bot.getPrefix() + "help online' "
                + "or '" + Bot.getPrefix() + "help join')\n\n\n"
                + "If you want to stop certain users from using the bot simply give them a role called \"R Silenced\" \n"
                + "(You have to create this role yourself). The bot will ignore any messages of users with that role on your server. \n\n"
                + "You can add this bot to your server with this link: \n\n"
                + "https://discordapp.com/oauth2/authorize?client_id=383029520304963584&scope=bot&permissions=117824"
                + "\n\n\n"
                + "If you have any kind of feedback feel free to join my Discord server and let me know. \n\n"
                + "https://discord.gg/KRdQK8q"
                + "```**";
    }
    
    public String getOverrideHelp()
    {
        return "**```"
                + "Overriding permission levels for commands \n\n"
                + "You might find yourself in a situation in which you are not satisfied with the default permission levels "
                + "of certain commands. For example you don't want everyone to be able to create groups and restrict it to masters and owners. "
                + "In that case you can override the default level with the one that suits your needs! \n\n"
                + "Valid permission levels are: \n\n"
                + " -user\n"
                + " -master\n"
                + " -owner\n\n\n"
                + "To change the level you simply have to use the override command like so: \n\n"
                + Bot.getPrefix() + "override create master \n\n"
                + "This means that the 'create' command can now only be used by people with either master or owner permissions. "
                + "For more information on the master system type '" + Bot.getPrefix() + "help masters'. And for a more detailed description "
                + "of the override command check out its own help text with '" + Bot.getPrefix() + "help override'."
                + "```**";
    }
    
    public String getMastersHelp()
    {
        return "**```"
                + "Master \n\n"
                + "To determine whether a user is allowed to use certain commands the bot splits them into "
                + "permission categories. \n\n"
                + "You may use the override command to change the needed permission level for certain commands. "
                + "For that take a look at the overrideperms section of the help command.\n\n\n"
                + "These are the levels that the bot knows: \n"
                + "- CREATOR\n"
                + "         Creators have full control over the bot, they can \n"
                + "         use every single command even stuff like restart or shutdown.\n"
                + "         This permissions level is only meant for the developers of the bot. \n\n"
                + "- OWNERS\n"
                + "         Owners are people that have a lot of control over the bot on their server. \n"
                + "         They can grant users master or even owner permissions. \n"
                + "         When you add the bot to a server only the actual server owner has this permission level. \n\n"
                + "- MASTERS\n"
                + "         Masters have slightly less control over the bot. \n\n"
                + "- USERS\n"
                + "         These are the normal users without any special permissions. They can use the bot and \n"
                + "         get information from the help command. \n\n"
                + "- BANNED / NONE\n"
                + "         Users that have the intention to break the bot or actively try to annoy me \n"
                + "         might get banned. This means that they can no longer use any features of the bot \n"
                + "         on any server.\n\n"
                + "To give a user master permissions you have to use the '"+Bot.getPrefix()+"master @user' command. \n\n"
                + "To remove someones master permissions you have to use the '"+Bot.getPrefix()+"nomaster @user' command. \n\n"
                + "Same goes for promoting or demoting owners '"+Bot.getPrefix()+"owner @user' and '"+Bot.getPrefix()+"noowner @user'.\n\n"
                + "To show the current masters and owners on this server use the '"+Bot.getPrefix()+"showmasters' command.\n\n"
                + "You can also check what permission you or someone else has by using the '"+Bot.getPrefix()+"perms' command. Mention a user to see others "
                + "permission levels."
                + "```**";
    }
    
    public String getRandomUserHelp()
    {
        return "**```"
                + "To make the bot mention a random user you can use either of its three commands: \n\n"
                + "Use '"+Bot.getPrefix()+"all' to pick a user from the entire list (including offline users). \n\n"
                + "Use '"+Bot.getPrefix()+"online' to pick a user that is currently online. \n\n"
                + "Use '"+Bot.getPrefix()+"random @user1 @user2' or '"+Bot.getPrefix()+"random @role' to pick from the mentioned users or from the users that "
                + "have the mentioned role. \n\n"
                + "Use '"+Bot.getPrefix()+"channel' to pick a user from the voicechannel that you are currently connected to. \n"
                + "Use '"+Bot.getPrefix()+"channel #channelname' to pick a user that has read permissions in the tagged channel. \n"
                + "You can extend the 'channel' command like so '"+Bot.getPrefix()+"channel n' so it wont choose you."
                + "```**";
    }
    
    public String getGroupHelp()
    {
        return "**```"
                + "You can form groups and picks random users from those: \n\n"
                + "To create a group use '" + Bot.getPrefix() + "create team1', this will create a group with the name 'team1'. The name can be changed to whatever you like.\n\n"
                + "To close a group use '" + Bot.getPrefix() + "close team1', this will close the group with the name 'team1'.\n\n"
                + "To join a group use '" + Bot.getPrefix() + "join team1', this will make you join 'team1'.\n\n"
                + "To leave a group use '" + Bot.getPrefix() + "leave team1', this will make you leave 'team1' if it exists.\n\n"
                + "To remove users from a group use '" + Bot.getPrefix() + "remove team1 @user', this will remove the mentioned user/s from 'team1'.\n\n"
                + "To pick from an individual group use '" + Bot.getPrefix() + "group team1', this will mention a random user from that group.\n\n"
                + "For further information check out the individual descriptions of the command by using '" + Bot.getPrefix() + "help commandname'. "
                + "(Replace 'commandname' with the name of the command that you want to read more about, for example '" + Bot.getPrefix() + "help create' "
                + "or '" + Bot.getPrefix() + "help join')"
                + "```**";
    }
    
    public String getTeamHelp()
    {
        return "**```"
                + "To make the bot form random teams use the following: \n\n"
                + "Use '"+Bot.getPrefix()+"team 4 @user @user @user @user @user @user @user @user' to create 4 teams from the mentioned users. \n\n"
                + "Use '"+Bot.getPrefix()+"teamgroup 4 group1' to create 4 teams from the users that joined group1. \n\n"
                + "Use '"+Bot.getPrefix()+"teamchannel 6' to create 6 teams from the users that are in the same voicechannel as you. \n"
                + "You can extend the command like so '"+Bot.getPrefix()+"teamchannel n 6' so it wont choose you.\n"
                + "You can change the number of teams to anything above 0.\n\n"
                + "Check the detailed descriptions of these commands to read about additional features such as the slowmode."
                + "```**";
    }
    
    public String getRollHelp()
    {
        return "**```"
                + "If you simply want a random number use '" + Bot.getPrefix() + "roll 6' this sends a "
                + "number between 1 and 6, you can change the 6 to anything you want."
                + "```**";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new HelpCommand(this.validExpressions, this.permission, this.bot, this.main);
    }
}