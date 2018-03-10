package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.util.perm.UserPermissions;

import com.vdurmont.emoji.EmojiManager;

import core.Main;

/**
 * @author &#8904
 *
 */
public class RemoveOwnerCommand extends Command{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public RemoveOwnerCommand(String[] validExpressions, int permission, Bot bot, Main main) {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public RemoveOwnerCommand(List<String> validExpressions, int permission, Bot bot, Main main) {
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
        List<IUser> mentions = event.getMessage().getMentions();
        if (mentions.isEmpty())
        {
            this.bot.sendMessage("You have to tag the user that should lose the owner permissions.", event.getMessage().getChannel(), Colors.RED);
        }
        else
        {  
            if (!event.getGuildObject().isOwner(mentions.get(0)))
            {
                this.bot.sendMessage("That user is not a owner on this server.", 
                                            event.getMessage().getChannel(), Colors.RED);
            }
            else if (event.getGuildObject().getOwners().size() == 1)
            {
                this.bot.sendMessage("That user is my last owner on this server. I can't function without an owner.", 
                        event.getMessage().getChannel(), Colors.RED);
            }
            else if (event.getGuildObject().removeOwner(mentions.get(0)))
            {
                this.main.getDatabase().removeMaster(mentions.get(0).getStringID(), event.getGuildObject().getStringID());
                event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
                Bot.log.print(this, "Removed owner permissions of "+mentions.get(0).getName()+"#"+mentions.get(0).getDiscriminator()
                                                        +" on '"+event.getGuildObject().getGuild().getName()+"'.");
            }
        }
    }
    
    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp() 
    {
        return "```"
                + "Remove Owner Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Takes the owner permissions of the mentioned user away. \n"
                + "Keep in mind that every server needs at least one owner for this bot to fully function. \n\n\n"
                + "Related Commands: \n"
                + "- master \n"
                + "- showmasters \n"
                + "- owner \n"
                + "- nomaster"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new RemoveOwnerCommand(this.validExpressions, this.permission, this.bot, this.main);
    }
}