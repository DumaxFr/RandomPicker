package bot.hand.cmnd;

import java.io.File;
import java.util.List;

import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import core.Main;

/**
 * @author &#8904
 *
 */
public class RebootCommand extends Command
{
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public RebootCommand(String[] validExpressions, int permission, Main main) 
    {
        super(validExpressions, permission);
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public RebootCommand(List<String> validExpressions, int permission, Main main) 
    {
        super(validExpressions, permission);
        this.main = main;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event) 
    {
        this.main.getBot().sendMessage("Rebooting..", event.getMessage().getChannel(), Colors.GREEN, true);
        this.main.kill(false);
        File jar = null;
        try
        {
            jar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Process process = Runtime.getRuntime().exec("java -jar rebooter.jar "+jar.getPath()+" 5");
        }
        catch(Exception e)
        {
            Main.log.print(e);
        }
        System.exit(0);
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Reboot Command \n"
                + "<Creator> \n\n"
                + "Reboots the bot. \n\n\n"
                + "Related Commands: \n"
                + "- patch"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new RebootCommand(this.validExpressions, this.permission, this.main);
    }
}