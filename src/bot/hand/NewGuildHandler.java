package bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
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
            commandHandler.addCommand(new HelpCommand(new String[]{"help"}, UserPermissions.USER, bot, main));
            commandHandler.addCommand(new OverridePermissionCommand(new String[]{"override"}, UserPermissions.OWNER, bot, main));
            commandHandler.addCommand(new RandomAllCommand(new String[]{"randomall", "all"}, UserPermissions.USER, bot, main));
            commandHandler.addCommand(new RandomChannelCommand(new String[]{"randomchannel", "channel"}, UserPermissions.USER, bot, main));
            commandHandler.addCommand(new RandomOnlineCommand(new String[]{"randomonline", "online"}, UserPermissions.USER, bot, main));
            commandHandler.addCommand(new RandomCommand(new String[]{"random"}, UserPermissions.USER, bot, main));
            commandHandler.addCommand(new GetPermissionLevelCommand(new String[]{"permissions", "perms"}, UserPermissions.USER, bot, main));
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
            guild.setCommandHandler(commandHandler);
            if(this.bot.addGuildObject(guild))
            {
                main.getDatabase().addMaster(guild.getGuild().getOwner().getStringID(), guild.getStringID(), UserPermissions.OWNER);
                guild.addOwner(guild.getGuild().getOwner());
                Bot.log.print(this, "Bot was added to '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")");
            }
        }
    }
}