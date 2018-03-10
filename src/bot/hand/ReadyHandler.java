package bot.hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.RequestBuffer.IRequest;
import bot.group.Group;
import bot.group.GroupManager;
import bot.group.GroupTimer;
import bot.hand.cmnd.AddMasterCommand;
import bot.hand.cmnd.AddOwnerCommand;
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
import bot.hand.cmnd.RandomAllCommand;
import bot.hand.cmnd.RandomChannelCommand;
import bot.hand.cmnd.RandomCommand;
import bot.hand.cmnd.RandomFromGroupCommand;
import bot.hand.cmnd.RandomNumberCommand;
import bot.hand.cmnd.RandomOnlineCommand;
import bot.hand.cmnd.RebootCommand;
import bot.hand.cmnd.RemoveFromGroupCommand;
import bot.hand.cmnd.RemoveMasterCommand;
import bot.hand.cmnd.RemoveOwnerCommand;
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
        /**try
        {
            Thread.sleep(25000);
        }
        catch (InterruptedException e)
        {
            Bot.errorLog.print(this, e);
        }*/
        int attempt = 0;
        while (this.bot.getGuildObjects().size() != this.bot.getClient().getGuilds().size())
        {
            attempt++;
            this.bot.setPlayingText("Loading. Attempt: "+ attempt);
            this.bot.createGuildObjects();
            loadMastersAndOwners();
            loadBannedUsers();
            loadCommandHandlers();
            //loadGroupManagers(); probably not needed and wasted resource since not all guilds use groups
            checkGroupTimers();
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
        Bot.log.print(this, "Loading command handlers.");
        for (GuildObject guild : this.bot.getGuildObjects())
        {
            Threads.fixedPool.execute(new Runnable(){
                @Override
                public void run()
                {
                    GuildCommandHandler commandHandler = new GuildCommandHandler(guild);
                    commandHandler.addCommand(new HelpCommand(new String[]{"help"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new OverridePermissionCommand(new String[]{"override"}, UserPermissions.OWNER, bot, main));
                    commandHandler.addCommand(new GetPermissionLevelCommand(new String[]{"permissions", "perms"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new RandomAllCommand(new String[]{"randomall", "all"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new RandomChannelCommand(new String[]{"randomchannel", "channel"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new RandomOnlineCommand(new String[]{"randomonline", "online"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new RandomCommand(new String[]{"random"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new TeamCommand(new String[]{"team"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new TeamChannelCommand(new String[]{"teamchannel"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new TeamFromGroupCommand(new String[]{"teamgroup"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new JoinGroupCommand(new String[]{"join"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new LeaveGroupCommand(new String[]{"leave"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new RemoveFromGroupCommand(new String[]{"remove", "removefromgroup"}, UserPermissions.MASTER, bot, main));
                    commandHandler.addCommand(new RandomFromGroupCommand(new String[]{"group"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new CreateGroupCommand(new String[]{"create", "creategroup"}, UserPermissions.USER, bot, main));
                    commandHandler.addCommand(new CloseGroupCommand(new String[]{"close", "closegroup"}, UserPermissions.MASTER, bot, main));
                    commandHandler.addCommand(new AddMasterCommand(new String[]{"master", "addmaster"}, UserPermissions.OWNER, bot, main));
                    commandHandler.addCommand(new RemoveMasterCommand(new String[]{"nomaster", "removemaster"}, UserPermissions.OWNER, bot, main));
                    commandHandler.addCommand(new AddOwnerCommand(new String[]{"owner", "addowner"}, UserPermissions.OWNER, bot, main));
                    commandHandler.addCommand(new RemoveOwnerCommand(new String[]{"noowner", "removeowner"}, UserPermissions.OWNER, bot, main));
                    commandHandler.addCommand(new ShowMastersCommand(new String[]{"showmasters", "showowners"}, UserPermissions.USER, bot));
                    commandHandler.addCommand(new BanUserCommand(new String[]{"ban"}, UserPermissions.CREATOR, bot, main));
                    commandHandler.addCommand(new UnbanUserCommand(new String[]{"unban"}, UserPermissions.CREATOR, bot, main));
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
                    
                    Map<String, Integer> overrides = main.getDatabase().getOverridesForGuild(guild.getStringID());
                    
                    for (Command command : commandHandler.getCommands())
                    {
                        if (command.canOverride() && overrides.containsKey(command.getValidExpressions().get(0)))
                        {
                            command.overridePermission(overrides.get(command.getValidExpressions().get(0)));
                        }
                    }
                    
                    if (!overrides.isEmpty())
                    {
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
}