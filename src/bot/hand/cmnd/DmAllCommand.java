package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;
import core.Main;

/**
 * @author &#8904
 *
 */
public class DmAllCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public DmAllCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public DmAllCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        String[] parts = event.getMessage().getContent().split(">>");
        String text = parts[1].trim();
        List<IGuild> guilds = this.bot.getClient().getGuilds();
        List<IUser> sentUsers = new ArrayList<IUser>();
        int count = 0;
        for (IGuild guild : guilds)
        {
            if (!sentUsers.contains(guild.getOwner()))
            {
                this.bot.sendMessage(text, guild.getOwner().getOrCreatePMChannel());
                sentUsers.add(guild.getOwner());
                count++;
            }
        }
        this.bot.sendMessage("Sent message to "+count+" owners on "+guilds.size()+" guilds.", event.getChannel());
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "DM All Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Sends a message to the owner of each server the bot is on.\n\n"
                + "Example: \n\n"
                + Bot.getPrefix()+"dmall >> hello"
                + "\n\n\n"
                + "```";
    }
}