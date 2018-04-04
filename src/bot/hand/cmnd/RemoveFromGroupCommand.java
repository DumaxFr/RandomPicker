package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import bot.group.Group;
import bot.group.GroupManager;
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
public class RemoveFromGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public RemoveFromGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public RemoveFromGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        GroupManager manager = GroupManager.getManagerForGuild(event.getGuildObject());
        String[] parts = event.getMessage().getContent().trim().toLowerCase().split(" ");
        String name = "";
        List<IUser> users = event.getMentions();
        
        for (IRole role : event.getMessage().getRoleMentions())
        {
            for (IUser user : event.getGuildObject().getGuild().getUsersByRole(role))
            {
                users.add(user);
            }
        }
        
        if (parts.length > 1)
        {
            name = parts[1];
        }
        else if (parts.length <= 1 || users.isEmpty())
        {
            this.bot.sendMessage("Usage: '" + Bot.getPrefix() + "remove groupname @user'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        Group group = manager.getGroupByName(name);
        if (group == null)
        {
            this.bot.sendMessage("That group does not exist.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        int count = 0;
        for (IUser user : users)
        {
            if (group.removeMember(user))
            {
                count++;
            }
        }
        
        this.bot.sendMessage("Removed " + count + " member/s from the group '" + group.getName() + "'.", event.getMessage().getChannel(), count == 0 ? Colors.RED : Colors.GREEN);
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Remove From Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Removes the mentioned user/s from the group with the given name. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix() + "remove group1 @user\n\n\n"
                + "'group1' is the groupname which you have to change to the name of the group that "
                + "you want to remove members from.\n\n"
                + "You can mention multiple users to remove them all at once."
                + "\n\n\n"
                + "Related commands: \n"
                + "- create\n"
                + "- close\n"
                + "- join\n"
                + "- leave"
                + "```";
    }
}