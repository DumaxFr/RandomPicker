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
                + "Get nohup Logs Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This command will send you a file containing the nohup output."
                + "```";
    }
}