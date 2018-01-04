package bot.hand;

import sx.blah.discord.handle.obj.IGuild;
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
import bot.hand.cmnd.TeamFromGroupCommand;
import bot.hand.cmnd.ThreadCountCommand;
import bot.hand.cmnd.VersionCommand;
import bowt.bot.Bot;
import bowt.guild.GuildObject;
import bowt.hand.impl.GuildCommandHandler;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class GuildSetupHandler
{
    public static GuildObject setupGuildObject(IGuild guild, Main main, Bot bot)
    {
        GuildObject guildObject = new GuildObject(guild);
        GuildCommandHandler commandHandler = new GuildCommandHandler(guildObject);
        commandHandler.addCommand(new HelpCommand(new String[]{"help"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new RandomAllCommand(new String[]{"randomall", "all"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new RandomChannelCommand(new String[]{"randomchannel", "channel"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new RandomOnlineCommand(new String[]{"random", "online"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new TeamCommand(new String[]{"team"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new TeamChannelCommand(new String[]{"teamchannel"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new TeamFromGroupCommand(new String[]{"teamgroup"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new JoinGroupCommand(new String[]{"join"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new LeaveGroupCommand(new String[]{"leave"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new RandomFromGroupCommand(new String[]{"group"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new RandomNumberCommand(new String[]{"roll"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new StatisticCommand(new String[]{"stats", "statistics"}, UserPermissions.USER, bot, main));
        commandHandler.addCommand(new DmAllCommand(new String[]{"dmall"}, UserPermissions.CREATOR, bot, main));
        commandHandler.addCommand(new ClearSystemLogsCommand(new String[]{"clearlog", "clearlogs", "clearsystemlogs"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new ClearAliveLogsCommand(new String[]{"clearalivelog", "clearalivelogs", "clearalive"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new ClearNoHupLogsCommand(new String[]{"clearnohuplog", "clearnohuplogs", "clearnohup"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new DiscSpaceCommand(new String[]{"disc", "space", "discspace"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new GetSystemLogsCommand(new String[]{"getlogs", "logs", "log"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new GetAliveLogsCommand(new String[]{"getalivelogs", "alivelogs", "alivelog"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new GetNoHupLogsCommand(new String[]{"getnohuplogs", "nohuplogs", "nohuplog", "nohup"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new MemoryCommand(new String[]{"ram", "memory"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new PatchCommand(new String[]{"patch", "update"}, UserPermissions.CREATOR, bot, main));
        commandHandler.addCommand(new RebootCommand(new String[]{"reboot", "restart"}, UserPermissions.CREATOR, main));
        commandHandler.addCommand(new ShutdownCommand(new String[]{"shutdown", "off"}, UserPermissions.CREATOR, bot, main));
        commandHandler.addCommand(new DeleteFileCommand(new String[]{"deletefile", "delete"}, UserPermissions.CREATOR, bot, main));
        commandHandler.addCommand(new DirectoryCommand(new String[]{"dir", "directory"}, UserPermissions.CREATOR, bot, main));
        commandHandler.addCommand(new DownloadFileCommand(new String[]{"download"}, UserPermissions.CREATOR, bot, main));
        commandHandler.addCommand(new StatusCommand(new String[]{"status"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new ThreadCountCommand(new String[]{"threads", "thread"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new VersionCommand(new String[]{"version", "v"}, UserPermissions.CREATOR, bot));
        guildObject.setCommandHandler(commandHandler);
        if (bot.addGuildObject(guildObject))
        {
            main.addGrouManager(new GroupManager(guildObject));
            Main.errorLog.print("Added new GuildObject after NullPointer.");
        }
        return guildObject;
    }
}