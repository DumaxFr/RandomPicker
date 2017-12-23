package bot.hand.cmnd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import core.Main;

/**
 * @author &#8904
 *
 */
public class DirectoryCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public DirectoryCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public DirectoryCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        String[] parts = event.getMessage().getContent().trim().split(" ");
        String path = "";
        ArrayList<String> dic = new ArrayList<String>();
        if (parts.length > 1)
        {
            path = parts[1];
        }
        try(Stream stream = Files.list(Paths.get(new File(path).toURI())))
        {
            Iterator ite = stream.iterator();
            while (ite.hasNext())
            {
                dic.add(ite.next().toString());
            }
            this.bot.sendListMessage("Directory " + new File(path).getAbsolutePath(), dic, event.getChannel(), 15, false);
        }
        catch (IOException e)
        {
            Main.log.print(e);
            this.bot.sendMessage("An error has occurred. Check your path.", event.getChannel(), Colors.RED);
        }
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Directory Command \n"
                + "<Creator> \n\n"
                + "Shows the structure of the current or the given directory. \n\n\n"
                + "Related Commands: \n"
                + "- deletefile"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new DirectoryCommand(this.validExpressions, this.permission, this.bot, this.main);
    }
}