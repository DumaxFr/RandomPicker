package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
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
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public RandomAllCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new RandomAllCommand(this.validExpressions, this.permission, this.bot, this.main);
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
        Random r = new Random();
        try
        {
            Thread.sleep(r.nextInt(50));
        }
        catch (InterruptedException e)
        {
        }
        int num = r.nextInt(users.size());
        this.bot.sendMessage(users.get(num).mention(), event.getChannel(), Colors.PURPLE);
        Main.channelLog.print("Picked option " + (num + 1) + " out of " + users.size() + ".");
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Random Global User Command \n"   
                + "<User> \n\n"
                + "Picks a random non-bot user from the entire user list. This includes offline users. \n\n"
                + "```";
    }
}