package bot.hand.cmnd;

import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.obj.IUser;
import bot.group.Group;
import bot.group.GroupManager;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class RandomFromGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public RandomFromGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public RandomFromGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        return new RandomFromGroupCommand(this.validExpressions, this.permission, this.bot, this.main);
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
            this.bot.sendMessage("Make sure to open a group by using the create command first.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        List<IUser> users = group.getMembers();
        if (!users.isEmpty())
        {
            Random r = new Random();
            try
            {
                Thread.sleep(r.nextInt(50));
            }
            catch (InterruptedException e)
            {
            }
            int num = r.nextInt(users.size());
            this.bot.sendMessage(users.get(num).mention() + " \n(" + users.get(num).getDisplayName(event.getGuildObject().getGuild()) + ")", event.getChannel(), Colors.PURPLE);
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Random User From Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Picks a random user from a group with the given name. \n\n\n"
                + "Usage:\n\n"
                + Bot.getPrefix() + "group group1"
                + "```";
    }
}