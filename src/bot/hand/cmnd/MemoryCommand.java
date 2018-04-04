package bot.hand.cmnd;

import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;

/**
 * @author &#8904
 *
 */
public class MemoryCommand extends Command
{
    private Bot bot;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public MemoryCommand(String[] validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public MemoryCommand(List<String> validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        String memory = format(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        bot.sendMessage("`Memory used: "+memory+"`", event.getMessage().getChannel(), Colors.PURPLE);
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Memory Usage Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This command shows how much memory the bot is currently using. \n\n\n"
                + "Related Commands: \n"
                + "- stats \n"
                + "- disc \n"
                + "- threads"
                + "```";
    }
    
    private String format(long ram)
    {
        String[] units = {"b", "kb", "mb", "gb"};
        float actSize = (float)ram;
        String unit = units[0];
        for (int i = 0; i < 4; i++)
        {
            if (actSize >= 1000)
            {
                actSize /= 1000;
                unit = units[i+1];
            }
        }
        return String.format("%.2f", actSize)+" "+unit;
    }
}