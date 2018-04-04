package bot.hand.cmnd;

import java.util.List;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class ShutdownCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public ShutdownCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public ShutdownCommand(List<String> validExpressions, int permission, Bot bot, Main main)
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        this.bot.sendMessage(":skull:", event.getMessage().getChannel(), Colors.ORANGE);
        this.main.kill(true);
    }
    
    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Shutdown Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This command will shut the bot down. \n\n\n"
                + "Related Commands: \n"
                + "- reboot \n"
                + "- patch"
                + "```";
    }
}