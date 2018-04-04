package bot.hand.cmnd;

import java.io.File;
import java.io.FileNotFoundException;
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
                + "Get Systemlogs Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This command will send you a file containing the system logs. \n\n\n"
                + "Related Commands: \n"
                + "- clearsystemlogs"
                + "```";
    }
}