package bot.hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.RequestBuffer.IRequest;
import bot.auto.AutomatedCommand;
import bot.group.Group;
import bot.group.GroupManager;
import bot.group.GroupTimer;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.guild.GuildObject;
import bowt.hand.impl.BotReadyHandler;
import bowt.hand.impl.ChannelLogger;
import bowt.hand.impl.GuildCommandHandler;
import bowt.hand.impl.PlayingTextHandler;
import bowt.prop.Properties;
import bowt.thread.Threads;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class ReadyHandler extends BotReadyHandler
{
    private Main main;
    
    public ReadyHandler(Bot bot, Main main)
    {
        super(bot);
        this.main = main;
    }
    
    /**
     * @see bowt.hand.impl.BotReadyHandler#prepare()
     */
    @Override
    public void prepare()
    {
        int attempt = 0;
        while (this.bot.getGuildObjects().size() != this.bot.getClient().getGuilds().size())
        {
            attempt++;
            this.bot.setPlayingText("Loading. Attempt: "+ attempt);
            this.bot.createGuildObjects();
            loadMastersAndOwners();
            loadBannedUsers();
            loadCommandHandlers();
            checkGroupTimers();
            loadAutomatedCommands();
        }
        if (attempt > 1)
        {
            Bot.errorLog.print(this, "Needed " + attempt + " attempts to load guilds.");
        }
        this.bot.setPlayingText(Bot.getPrefix()+"help");
        PlayingTextHandler statusHandler = new PlayingTextHandler(this.bot, 20000);
        statusHandler.setDynamicUpdateHandler(() -> 
        {
            List<String> playingTexts = new ArrayList<String>();
            playingTexts.add(Bot.getPrefix()+"help");
            playingTexts.add("Join https://discord.gg/KRdQK8q for help or feedback");
            playingTexts.add(this.bot.getGuildCount()+" servers");
            statusHandler.setPlayingTexts(playingTexts);
        });
        statusHandler.start();
        ChannelLogger channelLog = new ChannelLogger(Bot.log, 300000, this.bot);
        if (channelLog.setLogChannel(Properties.getValueOf("logchannel")))
        {
            channelLog.start();
            Bot.log.print(this, "Started ChannelLogger.");
        }
        else
        {
            Bot.errorLog.print(this, "Failed to set channel for the ChannelLogger.");
        }
        
        ChannelLogger errrorLog = new ChannelLogger(Bot.errorLog, 300000, this.bot);
        if (errrorLog.setLogChannel(Properties.getValueOf("errorlogchannel")))
        {
            errrorLog.start();
            Bot.log.print(this, "Started error ChannelLogger.");
        }
        else
        {
            Bot.errorLog.print(this, "Failed to set channel for the error ChannelLogger.");
        }
    }
    
    private void loadCommandHandlers()
    {
        for (GuildObject guild : this.bot.getGuildObjects())
        {
            Threads.fixedPool.execute(new Runnable(){
                @Override
                public void run()
                {
                    GuildCommandHandler commandHandler = Main.commandHandler;
                    
                    Map<String, Integer> overrides = main.getDatabase().getOverridesForGuild(guild.getStringID());
                    
                    if (!overrides.isEmpty())
                    {
                        List<String> commandStrings = new ArrayList<String>(overrides.keySet());
                        
                        for (String commandString : commandStrings)
                        {
                            Command command = commandHandler.getCommand(commandString);
                            
                            command.overridePermission(overrides.get(commandString), guild);
                        }
                        
                        Bot.log.print(this, "Loaded " + overrides.size() + " command overrides for '" + guild.getGuild().getName() + "'.");
                    }
                    
                    guild.setCommandHandler(commandHandler); 
                }
            });
        }
        Bot.log.print(this, "Loaded command handlers.");
    }
    
    private void loadMastersAndOwners()
    {
        List<String> ids = null;
        for (GuildObject guild : this.bot.getGuildObjects())
        {
            main.getDatabase().addMaster(guild.getGuild().getOwner().getStringID(), guild.getStringID(), UserPermissions.OWNER);
            ids = main.getDatabase().getMasterIDs(guild, UserPermissions.OWNER);
            for (String id : ids)
            {
                IUser user = RequestBuffer.request(new IRequest<IUser>()
                        {
                    @Override
                    public IUser request()
                    {
                        return bot.getClient().fetchUser(Long.parseLong(id));
                    }
                }).get();
                guild.addOwner(user);
            }
            ids = main.getDatabase().getMasterIDs(guild, UserPermissions.MASTER);
            for (String id : ids)
            {
                IUser user = RequestBuffer.request(new IRequest<IUser>()
                        {
                    @Override
                    public IUser request()
                    {
                        return bot.getClient().fetchUser(Long.parseLong(id));
                    }
                }).get();
                guild.addMaster(user);
            }
        }
        Bot.log.print(this, "Registered "+this.bot.getTotalOwnerCount()+" owners.");
        Bot.log.print(this, "Registered "+this.bot.getTotalMasterCount()+" masters.");
    }
    
    private void loadBannedUsers()
    {
        List<String> ids = main.getDatabase().getBannedUsers();
        for (String id : ids)
        {
            try 
            {
                IUser user = RequestBuffer.request(new IRequest<IUser>()
                        {
                    @Override
                    public IUser request()
                    {
                        return bot.getClient().fetchUser(Long.parseLong(id));
                    }
                }).get();
                this.bot.banUser(user);
            }
            catch (Exception e)
            {
                Bot.errorLog.print(this, e);
            }
        }
        Bot.log.print(this, "Loaded "+ids.size()+" banned users.");
    }
    
    private void loadGroupManagers()
    {
        Bot.log.print(this, "Loading group managers.");
        for (GuildObject guild : this.bot.getGuildObjects())
        {
            GroupManager.addManager(new GroupManager(guild));
        }
        Bot.log.print(this, "Loaded group managers.");
    }
    
    private void checkGroupTimers()
    {
        int closeCount = 0;
        int resumeCount = 0;
        for (GuildObject guild : this.bot.getGuildObjects())
        {
            List<GroupTimer> timers = this.main.getDatabase().getGroupTimersForGuild(guild.getStringID());
            
            if (!timers.isEmpty())
            {
                GroupManager manager = GroupManager.getManagerForGuild(guild);
                for (GroupTimer timer : timers)
                {
                    Group group = manager.getGroupByName(timer.getName());
                    if (System.currentTimeMillis() >= timer.getTime())
                    {
                        if (manager.remove(timer.getName()))
                        {
                            closeCount++;
                        }
                    }
                    else
                    {
                        group.destroyAt(timer.getTime());
                        resumeCount++;
                    }
                }
            }
        }
        if (closeCount != 0)
        {
            Bot.log.print(this, "Closed " + closeCount + " groups because their timer expired.");
        }
        if (resumeCount != 0)
        {
            Bot.log.print(this, "Resumed " + resumeCount + " grouptimers.");
        }
    }
    
    private void loadAutomatedCommands()
    {
        List<AutomatedCommand> commands = this.main.getDatabase().getAutomatedCommands();
        
        for (AutomatedCommand command : commands)
        {
            AutomatedCommand.add(command);
        }
        Bot.log.print(this, "Loaded " + commands.size() + " automated commands.");
    }
}