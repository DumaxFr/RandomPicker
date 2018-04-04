package bot.hand.cmnd;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;

import com.vdurmont.emoji.EmojiManager;

/**
 * @author &#8904
 *
 */
public class ClearNoHupLogsCommand extends Command
{

    /**
     * @param validExpressions
     * @param permission
     */
    public ClearNoHupLogsCommand(String[] validExpressions, int permission) 
    {
        super(validExpressions, permission);
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public ClearNoHupLogsCommand(List<String> validExpressions, int permission) 
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
            new PrintWriter("nohup.out").close();
            event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
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
                + "Clear nohup Logs Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This command will clear the nohup file. \n\n\n"
                + "Related Commands: \n"
                + "- getnohuplogs"
                + "```";
    }
}