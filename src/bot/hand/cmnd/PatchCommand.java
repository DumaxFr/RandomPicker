package bot.hand.cmnd;

import java.io.File;
import java.util.List;

import sx.blah.discord.handle.obj.IMessage.Attachment;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import core.Main;

/**
 * @author &#8904
 *
 */
public class PatchCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public PatchCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public PatchCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (!attachments.isEmpty())
        {
            if (!attachments.get(0).getFilename().toLowerCase().contains(".jar"))
            {
                return;
            }
            String url = attachments.get(0).getUrl();
            this.bot.sendMessage("Patching..", event.getMessage().getChannel(), Colors.GREEN, true);
            this.main.kill(false);
            File jar = null;
            try
            {
                jar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                Process process = Runtime.getRuntime().exec("java -jar patcher.jar "+jar.getPath()+" "+url+" boot");
            }
            catch (Exception e)
            {
                Main.log.print(e);
            }
            System.exit(0);
        }
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Patch Command \n"
                + "<Creator> \n\n"
                + "Replaces the bot file with the attached one and reboots the bot. \n\n\n"
                + "Related Commands: \n"
                + "- reboot"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new PatchCommand(this.validExpressions, this.permission, this.bot, this.main);
    }
}