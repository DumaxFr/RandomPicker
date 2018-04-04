package core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import remote.RemoteServer;
import sx.blah.discord.api.events.IListener;
import bot.group.GroupManager;
import bot.hand.GuildKickHandler;
import bot.hand.MessageHandler;
import bot.hand.NetworkEventHandler;
import bot.hand.NewGuildHandler;
import bot.hand.ReadyHandler;
import bot.hand.cmnd.AddMasterCommand;
import bot.hand.cmnd.AddOwnerCommand;
import bot.hand.cmnd.AutomateCommand;
import bot.hand.cmnd.BanUserCommand;
import bot.hand.cmnd.ClearAliveLogsCommand;
import bot.hand.cmnd.ClearNoHupLogsCommand;
import bot.hand.cmnd.ClearSystemLogsCommand;
import bot.hand.cmnd.CloseGroupCommand;
import bot.hand.cmnd.CreateGroupCommand;
import bot.hand.cmnd.DeleteFileCommand;
import bot.hand.cmnd.DirectoryCommand;
import bot.hand.cmnd.DiscSpaceCommand;
import bot.hand.cmnd.DmAllCommand;
import bot.hand.cmnd.DownloadFileCommand;
import bot.hand.cmnd.GetAliveLogsCommand;
import bot.hand.cmnd.GetNoHupLogsCommand;
import bot.hand.cmnd.GetPermissionLevelCommand;
import bot.hand.cmnd.GetSystemLogsCommand;
import bot.hand.cmnd.HelpCommand;
import bot.hand.cmnd.JoinGroupCommand;
import bot.hand.cmnd.LeaveGroupCommand;
import bot.hand.cmnd.MemoryCommand;
import bot.hand.cmnd.OverridePermissionCommand;
import bot.hand.cmnd.PatchCommand;
import bot.hand.cmnd.PickAndRemoveFromGroupCommand;
import bot.hand.cmnd.RandomAllCommand;
import bot.hand.cmnd.RandomChannelCommand;
import bot.hand.cmnd.RandomCommand;
import bot.hand.cmnd.RandomFromGroupCommand;
import bot.hand.cmnd.RandomNumberCommand;
import bot.hand.cmnd.RandomOnlineCommand;
import bot.hand.cmnd.RebootCommand;
import bot.hand.cmnd.RemoveAutomatedCommand;
import bot.hand.cmnd.RemoveFromGroupCommand;
import bot.hand.cmnd.RemoveMasterCommand;
import bot.hand.cmnd.RemoveOwnerCommand;
import bot.hand.cmnd.ShowAutomatedCommandsCommand;
import bot.hand.cmnd.ShowGroupMembersCommand;
import bot.hand.cmnd.ShowMastersCommand;
import bot.hand.cmnd.ShutdownCommand;
import bot.hand.cmnd.StatisticCommand;
import bot.hand.cmnd.StatusCommand;
import bot.hand.cmnd.TeamChannelCommand;
import bot.hand.cmnd.TeamCommand;
import bot.hand.cmnd.TeamFromGroupCommand;
import bot.hand.cmnd.ThreadCountCommand;
import bot.hand.cmnd.UnbanUserCommand;
import bot.hand.cmnd.VersionCommand;
import bowt.bot.Bot;
import bowt.bot.exc.BowtieClientException;
import bowt.cons.LibConstants;
import bowt.hand.impl.GuildCommandHandler;
import bowt.hand.impl.OfflineHandler;
import bowt.log.Logger;
import bowt.prop.Properties;
import bowt.thread.Threads;
import bowt.util.perm.UserPermissions;
import db.DatabaseAccess;

/**
 * @author &#8904
 *
 */
public class Main
{
    public static final String BOT_VERSION = "3.3.7";
    public static GuildCommandHandler commandHandler;
    private Bot bot;
    private OfflineHandler offlineHandler;
    private DatabaseAccess database;
    private RemoteServer server;
    
    public static void main(String[] args)
    {
        Bot.log.setLogToSystemOut(false);
        try
        {
            System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream(Bot.errorLog.getLoggerFile())), true));
        }
        catch (IOException e)
        {
            Bot.errorLog.print(e);
        }
        new Main();
    }
    
    public Main()
    {
        this.database = new DatabaseAccess(this);
        GroupManager.setDatabase(this.database);
        this.bot = new Bot(Properties.getValueOf("token"), "r-");
        setupCommandHandler();
        new LibConstants();
        Bot.log.print(this, "Booting Random bot version "+BOT_VERSION);
        Bot.log.print(this, "Patcher version "+Properties.getValueOf("patcherversion"));
        Bot.log.print(this, "Rebooter version "+Properties.getValueOf("rebooterversion"));
        
        Properties.setValueOf("botversion", BOT_VERSION);
        IListener[] listeners = {
                new ReadyHandler(this.bot, this),
                new GuildKickHandler(this.bot, this),
                new NewGuildHandler(this.bot, this),
                new MessageHandler(this.bot, this)
        };
        this.bot.addListeners(listeners);
        try
        {
            this.bot.login();
        }
        catch (BowtieClientException e)
        {
            Bot.errorLog.print(this, e);
        }
        
        offlineHandler = new OfflineHandler(this.bot){
            @Override
            public void handle()
            {
                Bot.log.print(this, "Bot offline. Trying to reboot.");
                File jar = null;
                try
                {
                    jar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                    Process process = Runtime.getRuntime().exec("java -jar rebooter.jar "+jar.getPath()+" 10");
                }
                catch(Exception e)
                {
                    Bot.errorLog.print(this, e);
                }
                kill(true);
            }
        };
        offlineHandler.getLogger().setLogToSystemOut(false);
        offlineHandler.start();
    }
    
    public void kill(boolean exit)
    {
        try
        {
            this.bot.logout();
        }
        catch (BowtieClientException e)
        {
            Bot.errorLog.print(this, e);
        }
        if (exit)
        {
            this.offlineHandler.stop();
            Threads.kill();
            Logger.closeAll();
            System.exit(0);
        }
    }
    
    public void setupRemoteServer()
    {
        try
        {
            if (server != null)
            {
                server.kill();
            }
            String port = Properties.getValueOf("defaultPort");
            if (port == null)
            {
                port = "0";
            }
            this.server = new RemoteServer(Integer.parseInt(port), this);
            this.server.registerEventHandler(new NetworkEventHandler(this));
            this.server.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void setupCommandHandler()
    {
        commandHandler = new GuildCommandHandler();
        commandHandler.addCommand(new HelpCommand(new String[]{"help"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new OverridePermissionCommand(new String[]{"override"}, UserPermissions.OWNER, bot, this));
        commandHandler.addCommand(new GetPermissionLevelCommand(new String[]{"permissions", "perms"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new RandomAllCommand(new String[]{"randomall", "all"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new RandomChannelCommand(new String[]{"randomchannel", "channel"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new RandomOnlineCommand(new String[]{"randomonline", "online"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new RandomCommand(new String[]{"random"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new TeamCommand(new String[]{"team"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new TeamChannelCommand(new String[]{"teamchannel"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new TeamFromGroupCommand(new String[]{"teamgroup"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new JoinGroupCommand(new String[]{"join"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new LeaveGroupCommand(new String[]{"leave"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new RemoveFromGroupCommand(new String[]{"remove", "removefromgroup"}, UserPermissions.MASTER, bot, this));
        commandHandler.addCommand(new RandomFromGroupCommand(new String[]{"group"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new ShowGroupMembersCommand(new String[]{"members"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new PickAndRemoveFromGroupCommand(new String[]{"pickremove"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new CreateGroupCommand(new String[]{"create", "creategroup"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new CloseGroupCommand(new String[]{"close", "closegroup"}, UserPermissions.MASTER, bot, this));
        commandHandler.addCommand(new AutomateCommand(new String[]{"auto", "automate"}, UserPermissions.MASTER, bot, this));
        commandHandler.addCommand(new RemoveAutomatedCommand(new String[]{"stopauto"}, UserPermissions.MASTER, bot, this));
        commandHandler.addCommand(new ShowAutomatedCommandsCommand(new String[]{"showauto", "showautomated"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new AddMasterCommand(new String[]{"master", "addmaster"}, UserPermissions.OWNER, bot, this));
        commandHandler.addCommand(new RemoveMasterCommand(new String[]{"nomaster", "removemaster"}, UserPermissions.OWNER, bot, this));
        commandHandler.addCommand(new AddOwnerCommand(new String[]{"owner", "addowner"}, UserPermissions.OWNER, bot, this));
        commandHandler.addCommand(new RemoveOwnerCommand(new String[]{"noowner", "removeowner"}, UserPermissions.OWNER, bot, this));
        commandHandler.addCommand(new ShowMastersCommand(new String[]{"showmasters", "showowners"}, UserPermissions.USER, bot));
        commandHandler.addCommand(new BanUserCommand(new String[]{"ban"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new UnbanUserCommand(new String[]{"unban"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new RandomNumberCommand(new String[]{"roll"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new StatisticCommand(new String[]{"stats", "statistics"}, UserPermissions.USER, bot, this));
        commandHandler.addCommand(new DmAllCommand(new String[]{"dmall"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new ClearSystemLogsCommand(new String[]{"clearlog", "clearlogs", "clearsystemlogs"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new ClearAliveLogsCommand(new String[]{"clearalivelog", "clearalivelogs", "clearalive"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new ClearNoHupLogsCommand(new String[]{"clearnohuplog", "clearnohuplogs", "clearnohup"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new DiscSpaceCommand(new String[]{"disc", "space", "discspace"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new GetSystemLogsCommand(new String[]{"getlogs", "logs", "log"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new GetAliveLogsCommand(new String[]{"getalivelogs", "alivelogs", "alivelog"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new GetNoHupLogsCommand(new String[]{"getnohuplogs", "nohuplogs", "nohuplog", "nohup"}, UserPermissions.CREATOR));
        commandHandler.addCommand(new MemoryCommand(new String[]{"ram", "memory"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new PatchCommand(new String[]{"patch", "update"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new RebootCommand(new String[]{"reboot", "restart"}, UserPermissions.CREATOR, this));
        commandHandler.addCommand(new ShutdownCommand(new String[]{"shutdown", "off"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new DeleteFileCommand(new String[]{"deletefile", "delete"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new DirectoryCommand(new String[]{"dir", "directory"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new DownloadFileCommand(new String[]{"download"}, UserPermissions.CREATOR, bot, this));
        commandHandler.addCommand(new StatusCommand(new String[]{"status"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new ThreadCountCommand(new String[]{"threads", "thread"}, UserPermissions.CREATOR, bot));
        commandHandler.addCommand(new VersionCommand(new String[]{"version", "v"}, UserPermissions.CREATOR, bot));
    }
    
    public Bot getBot()
    {
        return this.bot;
    }
    
    public DatabaseAccess getDatabase()
    {
        return this.database;
    }
}