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
public class RemoveMasterCommand extends Command{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public RemoveMasterCommand(String[] validExpressions, int permission, Bot bot, Main main) {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public RemoveMasterCommand(List<String> validExpressions, int permission, Bot bot, Main main) {
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
            this.bot.sendMessage("You have to tag the user that should lose the master permissions.", event.getMessage().getChannel(), Colors.RED);
        }
        else
        {  
            if (!event.getGuildObject().isMaster(mentions.get(0)))
            {
                this.bot.sendMessage("That user is not a master on this server.", 
                                            event.getMessage().getChannel(), Colors.RED);
            }
            else if (event.getGuildObject().removeMaster(mentions.get(0)))
            {
                this.main.getDatabase().removeMaster(mentions.get(0).getStringID(), event.getGuildObject().getStringID());
                event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"));
                Bot.log.print(this, "Removed master permissions of "+mentions.get(0).getName()+"#"+mentions.get(0).getDiscriminator()
                                                        +" on '"+event.getGuildObject().getGuild().getName()+"'.");
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
                + "Remove Master Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Takes the master permissions of the mentioned user away. \n\n\n"
                + "Related Commands: \n"
                + "- master \n"
                + "- showmasters \n"
                + "- owner \n"
                + "- noowner"
                + "```";
    }
}