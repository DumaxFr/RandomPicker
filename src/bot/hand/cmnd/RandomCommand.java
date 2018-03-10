package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
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
public class RandomCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public RandomCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public RandomCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        return new RandomCommand(this.validExpressions, this.permission, this.bot, this.main);
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        List<IUser> users = new ArrayList<IUser>();
        
        boolean hasMentions = false;
        
        if (!event.getMessage().getMentions().isEmpty())
        {
            hasMentions = true;
            for (IUser user : event.getMessage().getMentions())
            {
                if (!users.contains(user))
                {
                    users.add(user);
                }
            }
        }
        if (!event.getMessage().getRoleMentions().isEmpty())
        {
            hasMentions = true;
            for (IRole role : event.getMessage().getRoleMentions())
            {
                for (IUser user : event.getGuildObject().getGuild().getUsersByRole(role))
                {
                    if (!users.contains(user))
                    {
                        users.add(user);
                    }
                }
            }
        }
        
        if (!hasMentions)
        {
            this.bot.sendMessage("Make sure to mention either users or at least one role for the bot to pick from.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        Random r = new Random();
        try
        {
            Thread.sleep(r.nextInt(50));
        }
        catch (InterruptedException e)
        {
        }
        
        if (!users.isEmpty())
        {
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
                + "Random From Mentioned Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Picks a random user from the tagged users or roles. \n\n"
                + Bot.getPrefix() + "random @user @role \n\n"
                + "This picks from a pool constisting of the mentioned user and every other user that has the mentioned role."
                + "```";
    }
}