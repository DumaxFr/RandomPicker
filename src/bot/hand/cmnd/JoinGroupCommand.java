package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.util.RequestBuffer;
import bot.guild.Group;
import bot.guild.GroupManager;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;

import com.vdurmont.emoji.EmojiManager;

import core.Main;

/**
 * @author &#8904
 *
 */
public class JoinGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public JoinGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }
    
    public JoinGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new JoinGroupCommand(this.validExpressions, this.permission, this.bot, this.main);
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        GroupManager manager = this.main.getManagerByGuild(event.getGuildObject());
        if (manager == null)
        {
            manager = new GroupManager(event.getGuildObject());
            this.main.addGrouManager(manager);
        }
        String[] parts = event.getMessage().getContent().trim().toLowerCase().split(" ");
        String name = "";
        if (parts.length > 1)
        {
            name = parts[1];
        }
        else
        {
            this.bot.sendMessage("You have to add the name of the group. Example: '" + Bot.getPrefix() + "join groupname'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        Group group = manager.getGroupByName(name);
        if (group == null)
        {
            group = new Group(name, manager);
            manager.add(group);
            RequestBuffer.request(() -> event.getMessage().addReaction(EmojiManager.getForAlias("star2")));
        }
        if (group.add(event.getAuthor()))
        {
            RequestBuffer.request(() -> event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark")));
            Main.channelLog.print("Someone joined the group '" + group.getName() + "' on '" + event.getGuildObject().getGuild().getName() + "'");
        }
    }

    /**
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Join Group Command \n"   
                + "<User> \n\n"
                + "Makes you join the group with the given name. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix() + "join team1 \n\n\n"
                + "If the group does not exist it will be created (indicated by a star reaction from the bot). \n\n"
                + "Note that existing groups will be closed 2 hours after the last user joined or was picked."
                + "```";
    }
}