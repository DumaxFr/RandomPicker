package bot.hand;

import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;
import bot.group.GroupManager;
import bowt.bot.Bot;
import bowt.cmnd.Command;
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
        GuildCommandHandler commandHandler = Main.commandHandler;
        
        Map<String, Integer> overrides = main.getDatabase().getOverridesForGuild(guild.getStringID());
        
        for (Command command : commandHandler.getCommands())
        {
            if (command.canOverride() && overrides.containsKey(command.getValidExpressions().get(0)))
            {
                command.overridePermission(overrides.get(command.getValidExpressions().get(0)), guildObject);
            }
        }
        
        if (!overrides.isEmpty())
        {
            Bot.log.print("Loaded " + overrides.size() + " command overrides for '" + guildObject.getGuild().getName() + "'.");
        }
        
        guildObject.setCommandHandler(commandHandler); 
        
        if (bot.addGuildObject(guildObject))
        {
            main.getDatabase().addMaster(guildObject.getGuild().getOwner().getStringID(), guild.getStringID(), UserPermissions.OWNER);
            guildObject.addOwner(guildObject.getGuild().getOwner());
            GroupManager.addManager(new GroupManager(guildObject));
            Bot.errorLog.print("Added new GuildObject after NullPointer.");
        }
        return guildObject;
    }
}