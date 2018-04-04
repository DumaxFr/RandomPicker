package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;

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
public class ShowGroupMembersCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public ShowGroupMembersCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public ShowGroupMembersCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
            this.bot.sendMessage("You have to add the name of the group. Example: '" + Bot.getPrefix() + "members groupname'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        Group group = manager.getGroupByName(name);
        if (group == null)
        {
            this.bot.sendMessage("Make sure to open a group by using the create command first.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        List<IUser> users = group.getMembers();
        List<String> mentions = new ArrayList<String>();
        
        for (IUser user : users)
        {
            mentions.add(user.mention() + " \n(" + user.getDisplayName(event.getGuildObject().getGuild()) + ")");
        }
        
        bot.sendListMessage("Members of '" + group.getName() + "'", mentions, event.getChannel(), 15, true);
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Members of Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Shows the names of the members of the named group. \n\n\n"
                + "Usage:\n\n"
                + Bot.getPrefix() + "members group1"
                + "```";
    }
}