package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
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
public class UnbanUserCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public UnbanUserCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public UnbanUserCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        List<IUser> mentions = event.getMentions();
        for (IUser user : mentions)
        {
            this.bot.unbanUser(user);
            this.main.getDatabase().unbanUser(user.getStringID());
        }
        this.bot.sendMessage(mentions.size() == 1 ? "Unbanned "+mentions.size()+" user." : "Unbanned "+mentions.size()+" users.", event.getChannel(), Colors.GREEN);
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Unban User Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Unbans the mentioned users from using any features of the bot. \n\n"
                + "Example: \n\n"
                + Bot.getPrefix()+"unban @user"
                + "\n\n\n"
                + "Related Commands: \n"
                + "- ban \n"
                + "```";
    }
}