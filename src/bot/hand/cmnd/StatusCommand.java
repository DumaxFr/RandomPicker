package bot.hand.cmnd;

import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.evnt.impl.CommandEvent;

import com.vdurmont.emoji.EmojiManager;

/**
 * @author &#8904
 *
 */
public class StatusCommand extends Command 
{
    private Bot bot;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public StatusCommand(String[] validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public StatusCommand(List<String> validExpressions, int permission, Bot bot) 
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
        String status = event.getMessage().getContent().replace(Bot.getPrefix()+"status", "").trim();
        this.bot.getClient().changePlayingText(status);
        event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Status Command\n"
                + "<Creator>\n\n"
                + "Sets the 'playing game' text of the bot."
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new StatusCommand(this.validExpressions, this.permission, this.bot);
    }

}