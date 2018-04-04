package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;

import com.vdurmont.emoji.EmojiManager;

import core.Main;

/**
 * @author &#8904
 *
 */
public class BanUserCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public BanUserCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public BanUserCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        if(!mentions.isEmpty())
        {
            for (IUser user : mentions)
            {
                this.bot.banUser(user);
                this.main.getDatabase().banUser(user.getStringID());
            }
            this.bot.sendMessage(mentions.size() == 1 ? "Banned "+mentions.size()+" user." : "Banned "+mentions.size()+" users.", event.getChannel(), Colors.RED);
        }
        else
        {
            String id = event.getMessage().getContent().split(" ")[1];
            this.bot.banUser(this.bot.getClient().fetchUser(Long.parseLong(id)));
            this.main.getDatabase().banUser(id);
            event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Ban User Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Bans the mentioned users from using any features of the bot. \n\n"
                + "Example: \n\n"
                + Bot.getPrefix()+"ban @user"
                + "\n\n\n"
                + "Related Commands: \n"
                + "- unban \n"
                + "```";
    }
}