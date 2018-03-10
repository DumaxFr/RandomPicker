package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import bot.group.Group;
import bot.group.GroupManager;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.util.perm.UserPermissions;

import com.vdurmont.emoji.EmojiManager;

import core.Main;

/**
 * @author &#8904
 *
 */
public class JoinGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public JoinGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public JoinGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new JoinGroupCommand(this.validExpressions, this.permission, this.bot, this.main);
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
        if (parts.length > 1)
        {
            name = parts[1];
        }
        else
        {
            this.bot.sendMessage("You have to add the name of the group. Example: '" + Bot.getPrefix() + "join groupname'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        Group group = manager.getGroupByName(name);
        if (group == null)
        {
            this.bot.sendMessage("Make sure to open a group by using the 'create' command first.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        if (!event.getMessage().getMentions().isEmpty())
        {
            int count = 0;
            for (IUser user : event.getMessage().getMentions())
            {
                if (group.addMember(user))
                {
                    count++;
                }
            }
            this.bot.sendMessage("Added " + count + " user/s to the group '" + group.getName() + "'.", event.getMessage().getChannel(), count == 0 ? Colors.RED : Colors.GREEN);
        }
        else if (!event.getMessage().getRoleMentions().isEmpty())
        {
            int count = 0;
            for (IRole role : event.getMessage().getRoleMentions())
            {
                for (IUser user : event.getGuildObject().getGuild().getUsersByRole(role))
                {
                    if (group.addMember(user))
                    {
                        count++;
                    }
                }
            }
            this.bot.sendMessage("Added " + count + " user/s to the group '" + group.getName() + "'.", event.getMessage().getChannel(), count == 0 ? Colors.RED : Colors.GREEN);
        }
        else if (group.addMember(event.getAuthor()))
        {
            RequestBuffer.request(() -> event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark")));
        }
        else
        {
            this.bot.sendMessage("You are already a member of that group.", event.getMessage().getChannel(), Colors.RED);
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Join Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Makes you join the group with the given name. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix() + "join group1 \n\n\n"
                + "You can also put others into a group by mentioning them after the groupname in the command, like so:"
                + "\n\n"
                + Bot.getPrefix() + "join group1 @user1 @user2\n\n\n"
                + "This command will put the 2 users into the group, but not yourself."
                + "\n\n"
                + "Or put everyone with a specific role in the group by using: \n\n"
                + Bot.getPrefix() + "join group1 @role\n\n\n"
                + "Related commands: \n"
                + "- create\n"
                + "- close\n"
                + "- remove\n"
                + "- leave"
                + "```";
    }
}