package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.util.RequestBuffer;
import bot.group.Group;
import bot.group.GroupManager;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;

import com.vdurmont.emoji.EmojiManager;

import core.Main;

/**
 * @author &#8904
 *
 */
public class LeaveGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public LeaveGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public LeaveGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        if (parts.length > 1)
        {
            name = parts[1];
        }
        else
        {
            this.bot.sendMessage("You have to add the name of the group. Example: '" + Bot.getPrefix() + "leave groupname'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        Group group = manager.getGroupByName(name);
        if (group == null)
        {
            this.bot.sendMessage("That group does not exist.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        if (group.removeMember(event.getAuthor()))
        {
            RequestBuffer.request(() -> event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"))).get();
            return;
        }
        this.bot.sendMessage("You are not part of that group.", event.getMessage().getChannel(), Colors.RED);
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Leave Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Makes you leave the group with the given name if it exists. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix() + "leave group1"
                + "\n\n\n"
                + "Related commands: \n"
                + "- create\n"
                + "- close\n"
                + "- remove\n"
                + "- join"
                + "```";
    }
}