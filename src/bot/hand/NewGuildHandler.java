package bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import bot.guild.GroupManager;
import bot.hand.cmnd.ClearAliveLogsCommand;
import bot.hand.cmnd.ClearNoHupLogsCommand;
import bot.hand.cmnd.ClearSystemLogsCommand;
import bot.hand.cmnd.DeleteFileCommand;
import bot.hand.cmnd.DirectoryCommand;
import bot.hand.cmnd.DiscSpaceCommand;
import bot.hand.cmnd.DmAllCommand;
import bot.hand.cmnd.DownloadFileCommand;
import bot.hand.cmnd.GetAliveLogsCommand;
import bot.hand.cmnd.GetNoHupLogsCommand;
import bot.hand.cmnd.GetSystemLogsCommand;
import bot.hand.cmnd.HelpCommand;
import bot.hand.cmnd.JoinGroupCommand;
import bot.hand.cmnd.LeaveGroupCommand;
import bot.hand.cmnd.MemoryCommand;
import bot.hand.cmnd.PatchCommand;
import bot.hand.cmnd.RandomAllCommand;
import bot.hand.cmnd.RandomChannelCommand;
import bot.hand.cmnd.RandomFromGroupCommand;
import bot.hand.cmnd.RandomNumberCommand;
import bot.hand.cmnd.RandomOnlineCommand;
import bot.hand.cmnd.RebootCommand;
import bot.hand.cmnd.ShutdownCommand;
import bot.hand.cmnd.StatisticCommand;
import bot.hand.cmnd.StatusCommand;
import bot.hand.cmnd.TeamChannelCommand;
import bot.hand.cmnd.TeamCommand;
import bot.hand.cmnd.ThreadCountCommand;
import bot.hand.cmnd.VersionCommand;
import bowt.bot.Bot;
import bowt.guild.GuildObject;
import bowt.hand.impl.GuildCommandHandler;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * Handles {@link GuildCreateEvent}s which is received when the bot is added to a new
 * guild.
 * @author &#8904
 */
public class NewGuildHandler implements IListener<GuildCreateEvent>{
    private Bot bot;
    private Main main;
    
    /**
     * Creates a new instance for the given bot.
     * @param bot
     */
    public NewGuildHandler(Bot bot, Main main){
        this.bot = bot;
        this.main = main;
    }

    /**
     * Creates the {@link GuildObject} and registeres the guild owner as the first master.
     * 
     * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
     */
    @Override
    public void handle(GuildCreateEvent event)
    {
        if(this.bot.isReady()){ //to make sure its a new guild. else this happens after every boot
            GuildObject guild = new GuildObject(event.getGuild());
            GuildCommandHandler commandHandler = new GuildCommandHandler(guild);
            commandHandler.addCommand(new HelpCommand(new String[]{"help"}, UserPermissions.USER, this.bot, this.main));
            commandHandler.addCommand(new RandomAllCommand(new String[]{"randomall", "all"}, UserPermissions.USER, this.bot, this.main));
            commandHandler.addCommand(new RandomChannelCommand(new String[]{"randomchannel", "channel"}, UserPermissions.USER, this.bot, this.main));
            commandHandler.addCommand(new TeamCommand(new String[]{"team"}, UserPermissions.USER, this.bot, this.main));
            commandHandler.addCommand(new TeamChannelCommand(new String[]{"teamchannel"}, UserPermissions.USER, this.bot, this.main));
            commandHandler.addCommand(new RandomOnlineCommand(new String[]{"random", "online"}, UserPermissions.USER, this.bot, this.main));
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
            guild.setCommandHandler(commandHandler);
            if(this.bot.addGuildObject(guild))
            {
                this.main.addGrouManager(new GroupManager(guild));
                Main.log.print("Bot was added to '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")");
                Main.channelLog.print("Bot was added to '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")");
            }
        }
    }
}