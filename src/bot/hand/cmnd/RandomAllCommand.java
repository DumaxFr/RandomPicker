package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class RandomAllCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public RandomAllCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public RandomAllCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        List<IUser> allusers = event.getGuildObject().getGuild().getUsers();
        List<IUser> users = new ArrayList<IUser>();
        for(IUser user : allusers)
        {
            if (!user.isBot())
            {
                users.add(user);
            }
        }
        int picks = 1;
        String[] parts = event.getFixedContent().split(" ");
        
        try
        {
            picks = Integer.parseInt(parts[1]);
            
            if (picks < 1)
            {
                picks = 1;
            }
        }
        catch (Exception e)
        {
            
        }
        
        
        
        if (picks == 1)
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
        else
        {
            if (picks > users.size())
            {
                this.bot.sendMessage("There are not enough people on this server.", event.getChannel(), Colors.RED);
                return;
            }
            
            List<String> mentions = new ArrayList<>();
            
            while (mentions.size() < picks)
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
                mentions.add(users.get(num).mention(false) + " \n(" + users.get(num).getDisplayName(event.getGuildObject().getGuild()) + ")");
                users.remove(num);
            }
            
            this.bot.sendListMessage("Picked users", mentions, event.getChannel(), 10, true);
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Random Global User Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Picks a random non-bot user from the entire user list. This includes offline users. \n\n"
                + "You can add a number after the command to specify how many users should be picked.\n\n"
                + Bot.getPrefix() + "all 5\n\n"
                + "This picks 5 users."
                + "```";
    }
}