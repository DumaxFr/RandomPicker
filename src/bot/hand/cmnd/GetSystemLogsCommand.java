package bot.hand.cmnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import bowt.cmnd.Command;
import bowt.cmnd.CommandCooldown;
import bowt.evnt.impl.CommandEvent;
import core.Main;

/**
 * @author &#8904
 *
 */
public class GetSystemLogsCommand extends Command
{

    /**
     * @param validExpressions
     * @param permission
     */
    public GetSystemLogsCommand(String[] validExpressions, int permission) 
    {
        super(validExpressions, permission);
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public GetSystemLogsCommand(List<String> validExpressions, int permission)
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
            event.getMessage().getChannel().sendFile(new File("logs/system_logs.log"));
            new CommandCooldown(this, 10000).startTimer();;
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
                + "Get Systemlogs Command \n"
                + "<Creator> \n\n"
                + "This command will send you a file containing the system logs. \n\n\n"
                + "Related Commands: \n"
                + "- clearsystemlogs"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new GetSystemLogsCommand(this.validExpressions, this.permission);
    }
}