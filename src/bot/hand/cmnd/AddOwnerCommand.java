package bot.hand.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cmnd.CommandCooldown;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.util.perm.UserPermissions;

import com.vdurmont.emoji.EmojiManager;

import core.Main;

/**
 * @author &#8904
 *
 */
public class AddOwnerCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public AddOwnerCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public AddOwnerCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        new CommandCooldown(this, 2000).startTimer();
        List<IUser> mentions = event.getMessage().getMentions();
        if (mentions.isEmpty())
        {
            this.bot.sendMessage("You have to tag the user that should become an owner.", 
                                        event.getMessage().getChannel(), Colors.RED);
        }
        else
        {  
            if (event.getGuildObject().addOwner(mentions.get(0)))
            {
                if (event.getGuildObject().isMaster(mentions.get(0)))
                {
                    //if a master is being promoted
                    event.getGuildObject().removeMaster(mentions.get(0));
                    this.main.getDatabase().removeMaster(mentions.get(0).getStringID(), event.getGuildObject().getStringID());
                    this.main.getDatabase().addMaster(mentions.get(0).getStringID(), 
                            event.getGuildObject().getStringID(), UserPermissions.OWNER);
                    Bot.log.print(this, "Promoted "+mentions.get(0).getName()+"#"+mentions.get(0).getDiscriminator()+" to owner on "
                            + "'"+event.getGuildObject().getGuild().getName()+"'.");
                }
                else
                {
                    this.main.getDatabase().addMaster(mentions.get(0).getStringID(), 
                            event.getGuildObject().getStringID(), UserPermissions.OWNER);
                    Bot.log.print(this, "Registered "+mentions.get(0).getName()+"#"+mentions.get(0).getDiscriminator()+" as owner on "
                            + "'"+event.getGuildObject().getGuild().getName()+"'.");
                }
                event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
            }
            else
            {
                this.bot.sendMessage("That user is already an owner on this server.", 
                                        event.getMessage().getChannel(), Colors.RED);
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
                + "Add Owner Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.permissionOverride) + " permissions> \n\n"
                + "Gives the mentioned user owner permissions for this bot. \n\n"
                + "Users with owner permissions can execute any user/master/owner command, so "
                + "be careful who you give this power to. \n\n"
                + "Example: \n\n"
                + Bot.getPrefix()+"master @User"
                + "\n\n\n"
                + "Related Commands: \n"
                + "- nomaster \n"
                + "- showmasters \n"
                + "- master \n"
                + "- noowner"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new AddOwnerCommand(this.validExpressions, this.permission, this.bot, this.main);
    }
}