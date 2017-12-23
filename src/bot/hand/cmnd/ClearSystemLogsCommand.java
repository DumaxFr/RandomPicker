package bot.hand.cmnd;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import bowt.cmnd.Command;
import bowt.evnt.impl.CommandEvent;

import com.vdurmont.emoji.EmojiManager;

import core.Main;

/**
 * @author &#8904
 *
 */
public class ClearSystemLogsCommand extends Command
{

    /**
     * @param validExpressions
     * @param permission
     */
    public ClearSystemLogsCommand(String[] validExpressions, int permission) 
    {
        super(validExpressions, permission);
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public ClearSystemLogsCommand(List<String> validExpressions, int permission) 
    {
        super(validExpressions, permission);
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event) 
    {
        try 
        {
            new PrintWriter("logs/system_logs.log").close();
            event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
        }
        catch (FileNotFoundException e) 
        {
            Main.log.print(e);
        }
        
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Clear Systemlogs Command \n"
                + "<Creator> \n\n"
                + "This command will clear the systemlog file. \n\n\n"
                + "Related Commands: \n"
                + "- getsystemlogs"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new ClearSystemLogsCommand(this.validExpressions, this.permission);
    }
}