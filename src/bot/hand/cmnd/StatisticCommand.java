package bot.hand.cmnd;

import java.util.List;

import bot.auto.AutomatedCommand;
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
public class StatisticCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public StatisticCommand(String[] validExpressions, int permission, Bot bot, Main main)
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public StatisticCommand(List<String> validExpressions, int permission, Bot bot, Main main)
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        String statistics = "```"
                + "Active guilds: " + this.bot.getGuildCount()+"\n\n"
                + "Users: " + this.bot.getTotalUserCount()+"\n"
                + "Owners: "+this.bot.getTotalOwnerCount()+"\n"
                + "Masters: "+this.bot.getTotalMasterCount()+"\n"
                + "Banned: "+this.bot.getBannedUsers().size()+"\n"
                + "Open groups: " + this.main.getDatabase().getGroupCount()+"\n"
                + "Active automations: " + AutomatedCommand.size
                + "```";
        bot.sendMessage(statistics, event.getMessage().getChannel(), Colors.PURPLE);
    }
    
    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Statistics Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "This command will show interesting statistics about the bot. \n\n\n"
                + "Related Commands: \n"
                + "- threads \n"
                + "- disc \n"
                + "- ram"
                + "```";
    }
}