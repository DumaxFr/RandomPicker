package bot.hand.cmnd;

import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.hand.impl.GuildCommandHandler;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class OverridePermissionCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public OverridePermissionCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public OverridePermissionCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event) 
    {
        String[] parts = event.getMessage().getContent().split(" ");
        if (parts.length < 3)
        {
            this.bot.sendMessage("Usage: " + Bot.getPrefix() + "override commandName permissionName", event.getChannel(), Colors.RED);
            return;
        }
        String commandText = parts[1];
        Command command = ((GuildCommandHandler)event.getGuildObject().getCommandHandler()).getCommand(commandText);
        if (command != null)
        {
            //desired level
            int permissionLevel = UserPermissions.getPermissionLevelForString(parts[2]);
            
            if (permissionLevel == -1)
            {
                this.bot.sendMessage("Unknown permission level.", event.getChannel(), Colors.RED);
                return;
            }
            
            //user level
            int userPerm = UserPermissions.getPermissionLevel(event.getAuthor(), event.getGuildObject());
            
            if (!command.isValidPermission(userPerm, event.getGuildObject()))
            {
                this.bot.sendMessage("Your permission level is too low to override this command.", event.getChannel(), Colors.RED);
            }
            else if (userPerm < permissionLevel)
            {
                this.bot.sendMessage("You can't change the permission level to anything higher than your own.", event.getChannel(), Colors.RED);
            }
            else
            {
                int addResp = command.overridePermission(permissionLevel, event.getGuildObject());
                if (addResp == Command.DEFAULT_PERMISSION)
                {
                    this.main.getDatabase().removeOverride(event.getGuildObject().getStringID(), command.getValidExpressions().get(0));
                    
                    this.bot.sendMessage("Changed the needed permission level of the '" + command.getValidExpressions().get(0) + "' command "
                            + "to " + UserPermissions.getPermissionString(permissionLevel) + ".", event.getChannel(), Colors.GREEN);
                    
                    Bot.log.print(this, "Changed the needed permission level of the '" + command.getValidExpressions().get(0) + "' command "
                            + " on '" + event.getGuildObject().getGuild().getName() + "' to " + UserPermissions.getPermissionString(permissionLevel) + ".");
                }
                else if (addResp == Command.NEW_PERMISSION)
                {
                    this.main.getDatabase().addOverride(event.getGuildObject().getStringID(), command.getValidExpressions().get(0), permissionLevel);
                    
                    this.bot.sendMessage("Changed the needed permission level of the '" + command.getValidExpressions().get(0) + "' command "
                            + "to " + UserPermissions.getPermissionString(permissionLevel) + ".", event.getChannel(), Colors.GREEN);
                    
                    Bot.log.print(this, "Changed the needed permission level of the '" + command.getValidExpressions().get(0) + "' command "
                            + " on '" + event.getGuildObject().getGuild().getName() + "' to " + UserPermissions.getPermissionString(permissionLevel) + ".");
                }
                else if (addResp == Command.CANT_OVERRIDE)
                {
                    this.bot.sendMessage("The permission level of this command can't be overriden.", event.getChannel(), Colors.RED);
                }
            }
            return;
        }    
        
        this.bot.sendMessage("That is not a valid command.", event.getChannel(), Colors.RED);
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Override Permission Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "With this command you can change the needed permission level to execute certain commands. \n\n"
                + "Valid permission levels are: \n\n"
                + " -user\n"
                + " -master\n"
                + " -owner \n\n\n"
                + "Example: \n\n"
                + Bot.getPrefix() + "override online owner \n\n"
                + "This will change the permission level of the 'online' command to owner. Meaning that only people "
                + "with at least owner permissions can use this command on your server. \n\n\n"
                + Bot.getPrefix() + "override master user \n\n"
                + "This will change the permission level of the 'master' command to user. That means that everyone will be able to "
                + "assign master permissions to other people and themself. \n\n"
                + "Keep in mind that you can only change the level of commands for which you "
                + "already have a high enough level and that you can not change it to anything higher than your own permission. "
                + "So masters can't override owner or creator commands and they can't change the needed permission to anything higher than master."
                + "```";
    }
}