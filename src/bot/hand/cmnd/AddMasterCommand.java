package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cmnd.CommandCooldown;
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
public class AddMasterCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public AddMasterCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public AddMasterCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        new CommandCooldown(this, 2000, event.getGuildObject()).startTimer();
        List<IUser> mentions = event.getMessage().getMentions();
        if (mentions.isEmpty())
        {
            this.bot.sendMessage("You have to tag the user that should become a master.", 
                                        event.getMessage().getChannel(), Colors.RED);
        }
        else
        {  
            if (event.getGuildObject().isOwner(mentions.get(0)))
            {
                this.bot.sendMessage("That user is already an owner on this server.",
                                            event.getMessage().getChannel(), Colors.RED);
            }
            else if (event.getGuildObject().addMaster(mentions.get(0)))
            {
                this.main.getDatabase().addMaster(mentions.get(0).getStringID(), 
                                            event.getGuildObject().getStringID(), UserPermissions.MASTER);
                event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
                Bot.log.print(this, "Registered "+mentions.get(0).getName()+"#"+mentions.get(0).getDiscriminator()+" as master on "
                        + "'"+event.getGuildObject().getGuild().getName()+"'.");
            }
            else
            {
                this.bot.sendMessage("That user is already a master on this server.", 
                                        event.getMessage().getChannel(), Colors.RED);
            }
        }
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Add Master Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Gives the mentioned user master permissions for this bot. \n\n"
                + "Users with master permissions can execute any user/master command, so "
                + "be careful who you give this power to. \n\n"
                + "Example: \n\n"
                + Bot.getPrefix()+"master @User"
                + "\n\n\n"
                + "Related Commands: \n"
                + "- nomaster \n"
                + "- showmasters \n"
                + "- owner \n"
                + "- noowner"
                + "```";
    }
}