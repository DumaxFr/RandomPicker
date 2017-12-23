package bot.hand.cmnd;

import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cmnd.CommandCooldown;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import core.Main;

/**
 * @author &#8904
 *
 */
public class StatisticCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public StatisticCommand(String[] validExpressions, int permission, Bot bot, Main main)
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public StatisticCommand(List<String> validExpressions, int permission, Bot bot, Main main)
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        new CommandCooldown(this, 2000);
        String statistics = "```"
                + "Active guilds: "+this.bot.getGuildCount()+"\n\n"
                + "Users: "+this.bot.getTotalUserCount()
                + "```";
        bot.sendMessage(statistics, event.getMessage().getChannel(), Colors.PURPLE);
    }
    
    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Statistics Command \n"
                + "<USER> \n\n"
                + "This command will show interesting statistics about the bot. \n\n\n"
                + "Related Commands: \n"
                + "- threads \n"
                + "- disc \n"
                + "- ram"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new StatisticCommand(this.validExpressions, this.permission, this.bot, this.main);
    }
}