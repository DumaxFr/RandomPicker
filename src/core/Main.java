package core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import sx.blah.discord.api.events.IListener;
import bot.guild.GroupManager;
import bot.hand.GuildKickHandler;
import bot.hand.MessageHandler;
import bot.hand.NewGuildHandler;
import bot.hand.ReadyHandler;
import bowt.bot.Bot;
import bowt.bot.exc.BowtieClientException;
import bowt.cons.LibConstants;
import bowt.guild.GuildObject;
import bowt.hand.impl.OfflineHandler;
import bowt.log.Logger;
import bowt.prop.Properties;

/**
 * @author &#8904
 *
 */
public class Main
{
    public static final String BOT_VERSION = "2.2.0";
    public static Logger log;
    public static Logger channelLog;
    public static Logger errorLog;
    private Bot bot;
    private OfflineHandler offlineHandler;
    private List<GroupManager> managers;
    
    public static void main(String[] args)
    {
        Main.log = new Logger("logs/system_logs.log", TimeZone.getTimeZone("CET"));
        Main.log.setPrefix("[R]");
        Main.channelLog = new Logger(TimeZone.getTimeZone("CET"));
        Main.channelLog.setLogToSystemOut(false);
        Main.errorLog = new Logger("logs/error_logs.log", TimeZone.getTimeZone("CET"));
        try
        {
            System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream(errorLog.getLoggerFile())), true));
        }
        catch (IOException e)
        {
            log.print(e);
        }
        new Main();
    }
    
    public Main()
    {
        this.bot = new Bot(Properties.getValueOf("token"), "r-");
        this.managers = new ArrayList<GroupManager>();
        channelLog.setPrefix("[Bowt]");
        channelLog.printEmpty();
        channelLog.print("Bowtie Bot Lib v"+LibConstants.VERSION);
        channelLog.print("Last updated "+LibConstants.LAST_UPDATE);
        channelLog.printEmpty();
        channelLog.setPrefix("[R]");
        log.print("Booting Random User bot version "+BOT_VERSION);
        log.print("Patcher version "+Properties.getValueOf("patcherversion"));
        log.print("Rebooter version "+Properties.getValueOf("rebooterversion"));
        
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
            log.print(e);
        }
        
        offlineHandler = new OfflineHandler(this.bot){
            @Override
            public void handle()
            {
                this.log.print("Bot offline. Trying to reboot.");
                File jar = null;
                try
                {
                    jar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                    Process process = Runtime.getRuntime().exec("java -jar rebooter.jar "+jar.getPath()+" 10");
                }
                catch(Exception e)
                {
                    Main.log.print(e);
                }
                System.exit(0);
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
            log.print(e);
        }
        if (exit)
        {
            this.offlineHandler.stop();
            Logger.closeAll();
            System.exit(0);
        }
    }
    
    public void addGrouManager(GroupManager manager)
    {
        this.managers.add(manager);
    }
    
    public GroupManager getManagerByGuild(GuildObject guild)
    {
        for (GroupManager manager : this.managers)
        {
            if (manager.getGuild().equals(guild))
            {
                return manager;
            }
        }
        return null;
    }
    
    public Bot getBot()
    {
        return this.bot;
    }
}