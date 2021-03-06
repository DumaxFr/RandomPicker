package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;
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
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        GroupManager manager = GroupManager.getManagerForGuild(event.getGuildObject());
        String[] parts = event.getMessage().getContent().trim().toLowerCase().split(" ");
        String name = "";
        boolean online = false;
        if (parts.length > 1)
        {
            name = parts[1];
        }
        else
        {
            this.bot.sendMessage("You have to add the name of the group. Example: '" + Bot.getPrefix() + "group groupname'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        if (parts.length > 2)
        {
            online = parts[2].toLowerCase().trim().equals("online");
        }
        Group group = manager.getGroupByName(name);
        if (group == null)
        {
            this.bot.sendMessage("Make sure to open a group by using the create command first.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        List<IUser> users = group.getMembers();
        
        if (online)
        {
            List<IUser> onlineUsers = new ArrayList<IUser>();
            
            for(IUser user : users)
            {
                if (!user.getPresence().getStatus().equals(StatusType.OFFLINE) && !user.getPresence().getStatus().equals(StatusType.INVISIBLE))
                {
                    onlineUsers.add(user);
                }
            }
            users = onlineUsers;
        }
        
        if (!users.isEmpty())
        {
            Random r = new Random();
            try
            {
                Thread.sleep(r.nextInt(200));
            }
            catch (InterruptedException e)
            {
            }
            int num = r.nextInt(users.size());
            this.bot.sendMessage(users.get(num).mention(false) + " \n(" + users.get(num).getDisplayName(event.getGuildObject().getGuild()) + ")", event.getChannel(), Colors.PURPLE);
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Random User From Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Picks a random user from a group with the given name. \n\n"
                + "Add the word 'online' after the groupname to only pick people who are currently online.\n\n\n"
                + "Usage:\n\n"
                + Bot.getPrefix() + "group group1\n\n"
                + Bot.getPrefix() + "group group1 online"
                + "```";
    }
}