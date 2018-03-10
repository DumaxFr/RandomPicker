package bot.hand.cmnd;

import java.util.List;

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
public class CloseGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public CloseGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public CloseGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        return new CloseGroupCommand(this.validExpressions, this.permission, this.bot, this.main);
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
            this.bot.sendMessage("You have to add the name of the group. Example: '" + Bot.getPrefix() + "close groupname'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        Group group = manager.getGroupByName(name);
        if (group != null && manager.remove(name))
        {
            RequestBuffer.request(() -> event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark")));
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Close Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Closes an existing group with the given name. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix() + "close team1"
                + "\n\n\n"
                + "Related commands: \n"
                + "- create\n"
                + "- join\n"
                + "- leave"
                + "```";
    }
}