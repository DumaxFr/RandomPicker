package bot.hand.cmnd;

import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.util.perm.UserPermissions;

/**
 * @author &#8904
 *
 */
public class ThreadCountCommand extends Command
{
    private Bot bot;

    /**
     * @param validExpressions
     * @param permission
     */
    public ThreadCountCommand(String[] validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public ThreadCountCommand(List<String> validExpressions, int permission, Bot bot)
    {
        super(validExpressions, permission, true);
        this.bot = bot;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        bot.sendMessage("Current thread count: `"+Thread.activeCount()+"`.", event.getMessage().getChannel(), Colors.PURPLE);
    }
    
    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Threads Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "This command will show how many threads the bot is currently using. \n\n\n"
                + "Related Commands: \n"
                + "- stats \n"
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
        return new ThreadCountCommand(this.validExpressions, this.permission, this.bot);
    }
}