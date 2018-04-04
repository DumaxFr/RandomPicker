
package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.Discord4J;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.cons.LibConstants;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.prop.Properties;
import bowt.util.perm.UserPermissions;

/**
 * @author &#8904
 *
 */
public class VersionCommand extends Command 
{
    private Bot bot;

    /**
     * @param validExpressions
     * @param permission
     */
    public VersionCommand(String[] validExpressions, int permission, Bot bot)
    {
        super(validExpressions, permission, true);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public VersionCommand(List<String> validExpressions, int permission, Bot bot) 
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
        String versionInformation = "```"
                                    + "Bot version: "+Properties.getValueOf("botversion")+" \n"
                                    + "BowtieLib version: "+LibConstants.VERSION+"\n"
                                    + "Discord4J version: "+Discord4J.VERSION+"\n"
                                    + "Discord4J commit: "+Discord4J.COMMIT+"\n"
                                    + "Patcher version: "+Properties.getValueOf("patcherversion")+" \n"
                                    + "Rebooter version: "+Properties.getValueOf("rebooterversion")+"\n"
                                    + "Property version: "+Properties.getValueOf("propertyVersion")+" \n"
                                    + "Logger version: "+Properties.getValueOf("loggerVersion")
                                    + "```";
        bot.sendMessage(versionInformation, event.getMessage().getChannel(), Colors.PURPLE);
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Get Versions Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This will send you information about the current versions of the bot and other libs that are "
                + "used."
                + "```";
    }
}