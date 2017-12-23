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
public class GetNoHupLogsCommand extends Command
{

    /**
     * @param validExpressions
     * @param permission
     */
    public GetNoHupLogsCommand(String[] validExpressions, int permission) 
    {
        super(validExpressions, permission);
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public GetNoHupLogsCommand(List<String> validExpressions, int permission)
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
            event.getMessage().getChannel().sendFile(new File("nohup.out"));
            new CommandCooldown(this, 10000).startTimer();
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
                + "Get nohup Logs Command \n"
                + "<Creator> \n\n"
                + "This command will send you a file containing the nohup output."
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new GetNoHupLogsCommand(this.validExpressions, this.permission);
    }
}