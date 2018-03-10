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
import bowt.bot.Bot;
import bowt.bot.exc.BowtieClientException;
import bowt.cons.LibConstants;
import bowt.hand.impl.OfflineHandler;
import bowt.log.Logger;
import bowt.prop.Properties;
import bowt.thread.Threads;
import db.DatabaseAccess;

/**
 * @author &#8904
 *
 */
public class Main
{
    public static final String BOT_VERSION = "3.2.6";
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
    
    public Bot getBot()
    {
        return this.bot;
    }
    
    public DatabaseAccess getDatabase()
    {
        return this.database;
    }
}