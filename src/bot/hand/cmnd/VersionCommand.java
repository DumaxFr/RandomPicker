package bot.hand.cmnd;

import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.prop.Properties;

/**
 * @author &#8904
 *
 */
public class VersionCommand extends Command 
{
    private Bot bot;

    /**
     * @param validExpressions
     * @param permission
     */
    public VersionCommand(String[] validExpressions, int permission, Bot bot)
    {
        super(validExpressions, permission);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public VersionCommand(List<String> validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission);
        this.bot = bot;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event) 
    {
        String versionInformation = "```"
                                    + "Bot version: "+Properties.getValueOf("botversion")+" \n"
                                    + "Patcher version: "+Properties.getValueOf("patcherversion")+" \n"
                                    + "Rebooter version: "+Properties.getValueOf("rebooterversion")
                                    + "```";
        bot.sendMessage(versionInformation, event.getMessage().getChannel(), Colors.PURPLE);
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Get Versions Command \n"
                + "<User> \n\n"
                + "This will send you information about the current versions of the bot, the "
                + "patcher and the rebooter."
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new VersionCommand(this.validExpressions, this.permission, this.bot);
    }
}