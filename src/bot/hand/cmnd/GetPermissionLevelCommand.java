package bot.hand.cmnd;

import java.awt.Color;
import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class GetPermissionLevelCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public GetPermissionLevelCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public GetPermissionLevelCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        Color color;
        
        IUser user;
        
        if (!event.getMentions().isEmpty())
        {
            user = event.getMentions().get(0);
        }
        else
        {
            user = event.getMessage().getAuthor();
        }
        
        String perm = UserPermissions.getPermissionString(UserPermissions.getPermissionLevel(user, event.getGuildObject()));
        
        switch (UserPermissions.getPermissionLevel(user, event.getGuildObject()))
        {
            case UserPermissions.CREATOR:
                color = Colors.RED;
                break;
                
            case UserPermissions.OWNER:
                color = Colors.ORANGE;
                break;
                
            case UserPermissions.MASTER:
                color = Colors.YELLOW;
                break;
                
            case UserPermissions.USER:
                color = Colors.DEFAULT;
                break;
                
            default:
                color = Color.GRAY;
                break;
        }
        
        this.bot.sendMessage(perm, event.getMessage().getChannel(), color);
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Get Permission Level Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Requests the permission level of either you or a tagged user for the server that this command was used on.\n\n\n"
                + Bot.getPrefix() + "perms\n\n"
                + Bot.getPrefix() + "perms @user"
                + "```";
    }
}