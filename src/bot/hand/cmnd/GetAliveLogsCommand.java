package bot.hand.cmnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;

/**
 * @author &#8904
 *
 */
public class GetAliveLogsCommand extends Command
{
    private Bot bot;

    /**
     * @param validExpressions
     * @param permission
     */
    public GetAliveLogsCommand(String[] validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public GetAliveLogsCommand(List<String> validExpressions, int permission, Bot bot)
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
        try 
        {
            File file = new File("logs/alive_check.log");
            String lastError = "";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    if(!line.toLowerCase().contains("alive"))
                    {
                        lastError = line;
                    }
                }
            }
            catch (Exception e)
            {
                Bot.errorLog.print(this, e);
            }
            event.getMessage().getChannel().sendFile(file);
            if (!lastError.equals(""))
            {
                this.bot.sendMessage(lastError, event.getChannel());
            }
        }
        catch (FileNotFoundException e) 
        {
            Bot.errorLog.print(this, e);
        }
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Get Alivelogs Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This command will send you a file containing the alivelogs. \n\n\n"
                + "Related Commands: \n"
                + "- clearalivelogs"
                + "```";
    }
}