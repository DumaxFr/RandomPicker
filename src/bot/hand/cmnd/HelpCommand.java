package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.util.EmbedBuilder;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
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
            helpMessage = getHelp(event.getGuildObject());
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
        else if (type.equals("automation"))
        {
            helpMessage = getAutomationHelp();
        }
        else if (type.equals("commands"))
        {
            helpMessage = getCommandsHelp();
            Bot.log.print(this, "Command help was send on '"+event.getGuildObject().getGuild().getName()+"'.");
        }
        else
        {
            GuildObject guild = event.getGuildObject();
            Command command = ((GuildCommandHandler)guild.getCommandHandler()).getCommand(type);
            if (command != null)
            {
                helpMessage = command.getHelp(event.getGuildObject());
                Bot.log.print(this, "Command '"+type+"' help was send on '"+event.getGuildObject().getGuild().getName()+"'.");
            }    
        }
        String message = helpMessage;
        if (message.equals(""))
        {
            return;
        }
        try
        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.withColor(Colors.GREEN.darker());
            builder.withDescription(message);    
            bot.sendMessage(builder.build(), event.getChannel());
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
    public String getHelp(GuildObject guild) 
    {
        return "**```json\n"
                + "Help \n\n"
                + "This is a Random bot to pick random users/numbers or form random teams. Written by \"Lukas&#8904\". \n\n"
                + "Use the phrases below to get more detailed information about how the bot works. \n"
                + "\"" + Bot.getPrefix()+"\" is the prefix for every command. \n\n\n"
                + "\"" + Bot.getPrefix()+"help commands\""
                        + "\nA list of commands. \n\n"
                + "\"" + Bot.getPrefix()+"help randomuser\""
                        + "\nHow to make the bot mention a random user. \n\n"
                + "\"" + Bot.getPrefix()+"help groups\""
                        + "\nHow to pick from a group of people. \n\n"
                + "\"" + Bot.getPrefix()+"help teams\""
                        + "\nHow to create randomized teams. \n\n"
                + "\"" + Bot.getPrefix()+"help diceroll\""
                        + "\nHow to roll a random number within a certain range. \n\n"
                + "\"" + Bot.getPrefix()+"help masters\""
                        + "\nHow the master system works. \n\n"
                + "\"" + Bot.getPrefix()+"help automation\""
                        + "\nHow to automatically run commands. \n\n"
                + "\"" + Bot.getPrefix()+"help overrideperms\""
                        + "\nHow to change the needed permission level of certain commands.\n\n\n"
                + "You can read a full description with all the features and special options of every single command by typing \"" + Bot.getPrefix() + "help commandname\". "
                + "(Replace \"commandname\" with the name of the command that you want to read more about, for example \"" + Bot.getPrefix() + "help online\" "
                + "or \"" + Bot.getPrefix() + "help join\")\n\n\n"
                + "If you want to stop certain users from using the bot simply give them a role called \"R Silenced\" \n"
                + "(You have to create this role yourself). The bot will ignore any messages of users with that role on your server. \n\n"
                + "```**"
                + "\n[Invite the bot](https://discordapp.com/oauth2/authorize?client_id=383029520304963584&scope=bot&permissions=117824)"
                + "\n[Join the Bowtie Bots server for feedback or questions](https://discord.gg/KRdQK8q)"
                + "\n[Donate via PayPal](https://www.paypal.me/LukasHartwig)"
                + "\n[Become a patreon](https://www.patreon.com/bowtiebots)";
    }
    
    public String getCommandsHelp()
    {
        return "**```json\n"
                + "This is the bots command list. \n\n"
                + "To get more detailed information about a command please use \""+Bot.getPrefix()+"help theCommandName\" (For example "
                        + "\""+Bot.getPrefix()+"help create\") . \n\n"
                + "The prefix for every comand is \""+Bot.getPrefix()+"\"\n\n\n"
                + "- \"online\""
                + "\nPicks a random non-bot user that is online.\n\n"
                + "- \"all\""
                + "\nPicks a random non-bot user.\n\n"
                + "- \"random\""
                + "\nPicks a random user from either mentioned users or mentioned roles.\n\n"
                + "- \"channel\""
                + "\nPicks a random user from the voicechannel that you are in or a mentioned textchannel.\n\n"
                + "- \"roll\""
                + "\nRolls a random number between 1 and the number you specify after the command.\n\n"
                + "- \"team\""
                + "\nForms teams from mentioned users or roles.\n\n"
                + "- \"teamgroup\""
                + "\nForms teams from the users in the given group.\n\n"
                + "- \"teamchannel\""
                + "\nForms teams from the users in the same voicechannel as you.\n\n"
                + "- \"create\""
                + "\nCreates a group with the given name.\n\n"
                + "- \"join\""
                + "\nJoins the group with the given name if it exists.\n\n"
                + "- \"leave\""
                + "\nLeaves the group with the given name if it exists.\n\n"
                + "- \"remove\""
                + "\nRemoves the mentioned users from the given group.\n\n"
                + "- \"group\""
                + "\nPicks a random user from the given group.\n\n"
                + "- \"pickremove\""
                + "\nPicks and removes a random user from the given group.\n\n"
                + "- \"close\""
                + "\nCloses the given group.\n\n"
                + "- \"members\""
                + "\nShows the members of the named group.\n\n"
                + "- \"auto\""
                + "\nAutomatically uses a command at a given interval.\n\n"
                + "- \"shotautomated\""
                + "\nShows all currently active automations on your server.\n\n"
                + "- \"stopauto\""
                + "\nStops the automation with the given number.\n\n"
                + "- \"master\""
                + "\nGives master permissions.\n\n"
                + "- \"nomaster\""
                + "\nRemoves the master permissions.\n\n"
                + "- \"owner\""
                + "\nGives owner permissions.\n\n"
                + "- \"noowner\""
                + "\nRemoves the owner permissions.\n\n"
                + "- \"showmasters\""
                + "\nShows the masters.\n\n"
                + "- \"perms\""
                + "\nShows your permissionlevel for the bot.\n\n"
                + "- \"override\""
                + "\nCan change the needed permissionlevel for commands.\n\n"
                + "- \"stats\""
                + "\nShows some stats of the bot."
                + "```**";
    }
    
    public String getAutomationHelp()
    {
        return "**```json\n"
                + "Automation of command executions \n\n"
                + "Sometimes it might be useful to have the bot use commands automatically at gvien intervals. "
                + "For example to pick a new winner of a givaway every hour or to pick new teams every week.\n\n"
                + "To create an automated command use \"" + Bot.getPrefix() + "auto\"\n\n"
                        + "First you have to specify the interval at which the command will be executed.\n"
                        + "Valid time units are d (Days), h (Hours), m (Minutes). You can mix them if needed, "
                        + "just make sure to separate them with spaces.\n\n"
                        + "\"" + Bot.getPrefix() + "auto 1d 2h 45m\"\n\n\n"
                        + "Then you have to specify which command should be executed and with which parameters. "
                        + "For that simply write the entire command like you normally would, separated by a '<>' after the "
                        + "time.\n\n"
                        + "\"" + Bot.getPrefix() + "auto 1d 2h 45m <> " + Bot.getPrefix() + "team 3 @user @role @user\"\n\n\n"
                        + "After that you can add additional text that is displayed right before the command is executed. "
                        + "Again separate it with a '<>' from the rest of the command.\n\n"
                        + "\"" + Bot.getPrefix() + "auto 1d 2h 45m <> " + Bot.getPrefix() + "team 3 @user @role @user <> These are the teams!\"\n\n\n\n"
                + "Now the command will be executed with the set interval between executions. \n\n"
                + "To see all currently active automated commands use the \"" + Bot.getPrefix() + "showautomated\".\n\n"
                + "To stop the automation of a command use \"" + Bot.getPrefix() + "stopauto\" and add the number of the automation "
                        + "(which you can see with \"showautomated\") after the command.\n\n"
                        + "\"" + Bot.getPrefix() + "stopauto 43\""
                + "```**";
    }
    
    public String getOverrideHelp()
    {
        return "**```json\n"
                + "Overriding permission levels for commands \n\n"
                + "You might find yourself in a situation in which you are not satisfied with the default permission levels "
                + "of certain commands. For example you don't want everyone to be able to create groups and restrict it to masters and owners. "
                + "In that case you can override the default level with the one that suits your needs! \n\n"
                + "Valid permission levels are: \n\n"
                + " -\"user\"\n"
                + " -\"master\"\n"
                + " -\"owner\"\n\n\n"
                + "To change the level you simply have to use the override command like so: \n\n"
                + "\"" + Bot.getPrefix() + "override create master\" \n\n"
                + "This means that the 'create' command can now only be used by people with either master or owner permissions. "
                + "For more information on the master system type \"" + Bot.getPrefix() + "help masters\". And for a more detailed description "
                + "of the override command check out its own help text with \"" + Bot.getPrefix() + "help override\"."
                + "```**";
    }
    
    public String getMastersHelp()
    {
        return "**```json\n"
                + "Master \n\n"
                + "To determine whether a user is allowed to use certain commands the bot splits them into "
                + "permission categories. \n\n"
                + "These are the levels that the bot knows: \n\n"
                + "- \"CREATOR\"\n"
                + "Creators have full control over the bot, they can "
                + "use every single command even stuff like restart or shutdown. "
                + "This permissions level is only meant for the developers of the bot. \n\n"
                + "- \"OWNERS\"\n"
                + "Owners are people that have a lot of control over the bot on their server. "
                + "They add or delete lines which the bot uses to answer and they can grant users "
                + "master or even owner permissions. "
                + "When you add the bot to a server only the actual server owner has this permission level. \n\n"
                + "- \"MASTERS\"\n"
                + "Masters have slightly less control over the bot. They can still add and delete lines, "
                + "but they can't give other users master or owner permissions. \n\n"
                + "- \"USERS\"\n"
                + "These are the normal users without any special permissions. They can use the bot and "
                + "get information from the help command. \n\n"
                + "- \"BANNED / NONE\"\n"
                + "Users that have the intention to break the bot or actively try to annoy me"
                + "might get banned. This means that they can no longer use any features of the bot "
                + "on any server.\n\n"
                + "To give a user master permissions you have to use the \""+Bot.getPrefix()+"master @user\" command. \n\n"
                + "To remove someones master permissions you have to use the \""+Bot.getPrefix()+"nomaster @user\" command. \n\n"
                + "Same goes for promoting or demoting owners \""+Bot.getPrefix()+"owner @user\" and \""+Bot.getPrefix()+"noowner @user\".\n\n"
                + "To show the current masters and owners on this server use the \""+Bot.getPrefix()+"showmasters\" command."
                + "```**";
    }
    
    public String getRandomUserHelp()
    {
        return "**```json\n"
                + "To make the bot mention a random user you can use either of its three commands: \n\n"
                + "Use \""+Bot.getPrefix()+"all\" to pick a user from the entire list (including offline users). \n\n"
                + "Use \""+Bot.getPrefix()+"online\" to pick a user that is currently online. \n\n"
                + "Use \""+Bot.getPrefix()+"random @user1 @user2\" or \""+Bot.getPrefix()+"random @role\" to pick from the mentioned users or from the users that "
                + "have the mentioned role. \n\n"
                + "Use \""+Bot.getPrefix()+"channel\" to pick a user from the voicechannel that you are currently connected to. \n"
                + "Use \""+Bot.getPrefix()+"channel #channelname\" to pick a user that has read permissions in the tagged channel. \n"
                + "You can extend the \"channel\" command like so \""+Bot.getPrefix()+"channel n\" so it wont choose you.\n\n\n"
                + "If you want to pick multiple users simply add the number after the command."
                + "```**";
    }
    
    public String getGroupHelp()
    {
        return "**```json\n"
                + "You can form groups and picks random users from those: \n\n"
                + "To create a group use \"" + Bot.getPrefix() + "create group1\", this will create a group with the name 'group1'. The name can be changed to whatever you like.\n\n"
                + "To close a group use \"" + Bot.getPrefix() + "close group1\", this will close the group with the name 'group1'.\n\n"
                + "To join a group use \"" + Bot.getPrefix() + "join group1\", this will make you join 'group1'.\n\n"
                + "To leave a group use \"" + Bot.getPrefix() + "leave group1\", this will make you leave 'group1' if it exists.\n\n"
                + "To remove users from a group use \"" + Bot.getPrefix() + "remove group1 @user\", this will remove the mentioned user/s from 'group1'.\n\n"
                + "To pick from an individual group use \"" + Bot.getPrefix() + "group group1\", this will mention a random user from that group.\n\n"
                + "To pick from an individual group and remove the picked user right after use \"" + Bot.getPrefix() + "pickremove group1\".\n\n"
                + "To show the current members of a group use \"" + Bot.getPrefix() + "members group1\".\n\n"
                + "For further information check out the individual descriptions of the command by using \"" + Bot.getPrefix() + "help commandname\". "
                + "(Replace \"commandname\" with the name of the command that you want to read more about, for example \"" + Bot.getPrefix() + "help create\" "
                + "or \"" + Bot.getPrefix() + "help join\")"
                + "```**";
    }
    
    public String getTeamHelp()
    {
        return "**```json\n"
                + "To make the bot form random teams use the following: \n\n"
                + "Use \""+Bot.getPrefix()+"team 4 @user @user @user @user @user @user @user @user\" to create 4 teams from the mentioned users. You can "
                + "also mention roles to form teams from users that have that role.\n\n"
                + "Use \""+Bot.getPrefix()+"teamgroup 4 group1\" to create 4 teams from the users that joined group1. \n\n"
                + "Use \""+Bot.getPrefix()+"teamchannel 6\" to create 6 teams from the users that are in the same voicechannel as you. \n"
                + "You can extend the command like so \""+Bot.getPrefix()+"teamchannel n 6\" so it wont choose you.\n"
                + "You can change the number of teams to anything above 0.\n\n"
                + "Check the detailed descriptions of these commands to read about additional features such as the slowmode."
                + "```**";
    }
    
    public String getRollHelp()
    {
        return "**```json\n"
                + "If you simply want a random number use \"" + Bot.getPrefix() + "roll 10\" this sends a "
                + "number between 1 and 10 (inclusive), you can change the 10 to anything you want. If you "
                + "don't specify a number the bot will pick between 1 and 6."
                + "```**";
    }
}