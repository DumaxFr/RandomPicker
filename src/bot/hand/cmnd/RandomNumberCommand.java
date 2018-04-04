package bot.hand.cmnd;

import java.util.List;
import java.util.Random;

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
public class RandomNumberCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public RandomNumberCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public RandomNumberCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        String[] parts = event.getMessage().getContent().trim().toLowerCase().split(" ");
        int number = 6;
        try
        {
            if (parts.length > 1)
            {
                number = Integer.parseInt(parts[1].trim());
            }
        }
        catch (NumberFormatException e)
        {
            return;
        }
        if (number < 1)
        {
            return;
        }
        Random r = new Random();
        try
        {
            Thread.sleep(r.nextInt(200));
        }
        catch (InterruptedException e)
        {
        }
        int num = r.nextInt(number) + 1;
        this.bot.sendMessage(Integer.toString(num), event.getChannel(), Colors.PURPLE);
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Roll Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Rolls a random number between 1 and the number you gave it. If you don't specify a number then "
                + "it will pick from 1-6.\n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix() + "roll 10\n"
                + Bot.getPrefix() + "roll"
                + "```";
    }
}