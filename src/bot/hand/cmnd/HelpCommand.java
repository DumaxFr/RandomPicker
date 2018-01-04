package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.util.RequestBuffer;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cmnd.CommandCooldown;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.hand.impl.GuildCommandHandler;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class HelpCommand extends Command
{
    private GuildCommandHandler commandHandler;
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
    
    private void setupCommands()
    {
        commandHandler.addCommand(new HelpCommand(new String[]{"help"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new RandomAllCommand(new String[]{"randomall", "all"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new RandomChannelCommand(new String[]{"randomchannel", "channel"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new RandomOnlineCommand(new String[]{"random", "online"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new TeamCommand(new String[]{"team"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new TeamChannelCommand(new String[]{"teamchannel"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new TeamFromGroupCommand(new String[]{"teamgroup"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new JoinGroupCommand(new String[]{"join"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new LeaveGroupCommand(new String[]{"leave"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new RandomFromGroupCommand(new String[]{"group"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new RandomNumberCommand(new String[]{"roll"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new StatisticCommand(new String[]{"stats", "statistics"}, UserPermissions.USER, this.bot, this.main));
        commandHandler.addCommand(new DmAllCommand(new String[]{"dmall"}, UserPermissions.CREATOR, this.bot, this.main));
        commandHandler.addCommand(new ClearSystemLogsCommand(new String[]{"clearlog", "clearlogs", "clearsystemlogs"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new ClearAliveLogsCommand(new String[]{"clearalivelog", "clearalivelogs", "clearalive"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new ClearNoHupLogsCommand(new String[]{"clearnohuplog", "clearnohuplogs", "clearnohup"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new DiscSpaceCommand(new String[]{"disc", "space", "discspace"}, UserPermissions.CREATOR, this.bot));
        commandHandler.addCommand(new GetSystemLogsCommand(new String[]{"getlogs", "logs", "log"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new GetAliveLogsCommand(new String[]{"getalivelogs", "alivelogs", "alivelog"}, UserPermissions.CREATOR, this.bot));
        commandHandler.addCommand(new GetNoHupLogsCommand(new String[]{"getnohuplogs", "nohuplogs", "nohuplog", "nohup"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new MemoryCommand(new String[]{"ram", "memory"}, UserPermissions.CREATOR, this.bot));
        commandHandler.addCommand(new PatchCommand(new String[]{"patch", "update"}, UserPermissions.CREATOR, this.bot, this.main));
        commandHandler.addCommand(new RebootCommand(new String[]{"reboot", "restart"}, UserPermissions.CREATOR, this.main));
        commandHandler.addCommand(new ShutdownCommand(new String[]{"shutdown", "off"}, UserPermissions.CREATOR, this.bot, this.main));
        commandHandler.addCommand(new DeleteFileCommand(new String[]{"deletefile", "delete"}, UserPermissions.CREATOR, this.bot, this.main));
        commandHandler.addCommand(new DirectoryCommand(new String[]{"dir", "directory"}, UserPermissions.CREATOR, this.bot, this.main));
        commandHandler.addCommand(new DownloadFileCommand(new String[]{"download"}, UserPermissions.CREATOR, this.bot, this.main));
        commandHandler.addCommand(new StatusCommand(new String[]{"status"}, UserPermissions.CREATOR, this.bot));
        commandHandler.addCommand(new ThreadCountCommand(new String[]{"threads", "thread"}, UserPermissions.CREATOR, this.bot));
        commandHandler.addCommand(new VersionCommand(new String[]{"version", "v"}, UserPermissions.CREATOR, this.bot));
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event) 
    {
        String type = event.getMessage().getContent().replace(Bot.getPrefix()+"help", "").trim();
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
        else
        {
            GuildObject guild = event.getGuildObject();
            List<Command> commands = ((GuildCommandHandler)guild.getCommandHandler()).getCommands();
            for (Command command : commands)
            {
                if (command.isValidExpression(type))
                {
                    helpMessage = command.getHelp();
                    Main.log.print("Command '"+type+"' help was send on '"+event.getGuildObject().getGuild().getName()+"'.");
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
            Main.log.print(e);
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
                + "This is a Random bot written by Lukas#6668. \n\n"
                + "Use the phrases below to get more detailed information about how the bot works. \n"
                + Bot.getPrefix()+" is the prefix for every command. \n\n\n"
                + Bot.getPrefix()+"help randomuser         |       How to make the bot mention a random user. \n\n"
                + Bot.getPrefix()+"help groups             |       How to pick from a group of people. \n\n"
                + Bot.getPrefix()+"help teams              |       How to create randomized teams. \n\n"
                + Bot.getPrefix()+"help diceroll           |       How to roll a random number within a certain range. \n\n\n"
                + "If you want to stop certain users from using the bot simply give them a role called \"R Silenced\" \n"
                + "(You have to create this role yourself). The bot will ignore any messages of users with that role on your server. \n\n"
                + "You can add this bot to your server with this link: \n\n"
                + "https://discordapp.com/oauth2/authorize?client_id=383029520304963584&scope=bot&permissions=117824"
                + "\n\n\n"
                + "If you have any kind of feedback feel free to join my Discord server and let me know. \n\n"
                + "https://discord.gg/KRdQK8q"
                + "```**";
    }
    
    public String getRandomUserHelp()
    {
        return "**```"
                + "To make the bot mention a random user you can use either of its three commands: \n\n"
                + "Use '"+Bot.getPrefix()+"all' to pick a user from the entire list (including offline users). \n\n"
                + "Use '"+Bot.getPrefix()+"online' to pick a user that is currently online. \n\n"
                + "Use '"+Bot.getPrefix()+"channel' to pick a user from the voicechannel that you are currently connected to. \n"
                + "You can extend the command like so '"+Bot.getPrefix()+"channel n' so it wont choose you."
                + "```**";
    }
    
    public String getGroupHelp()
    {
        return "**```"
                + "You can form groups and picks random users from those: \n\n"
                + "To join or create a group use '" + Bot.getPrefix() + "join team1' this will make you join 'team1' or create the group "
                + "if it doesn't exist yet. Note that groups are being closed 2 hours after the last user joined or was picked automatically. The name can be changed to whatever you like.\n\n"
                + "To leave a group use '" + Bot.getPrefix() + "leave team1' this will make you leave 'team1' if it exists.\n\n"
                + "To pick from an individual group use '" + Bot.getPrefix() + "group team1' this will mention a random user from that group."
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
                + "You can change the number of teams to anything above 0."
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