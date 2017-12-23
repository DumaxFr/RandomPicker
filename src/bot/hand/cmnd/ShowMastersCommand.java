package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cmnd.CommandCooldown;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class ShowMastersCommand extends Command
{
    private Bot bot;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public ShowMastersCommand(String[] validExpressions, int permission, Bot bot)
    {
        super(validExpressions, permission);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public ShowMastersCommand(List<String> validExpressions, int permission, Bot bot)
    {
        super(validExpressions, permission);
        this.bot = bot;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        new CommandCooldown(this, 5000).startTimer();
        List<String> ownerNames = new ArrayList<String>();
        List<IUser> owners = event.getGuildObject().getOwners();
        for (IUser user : owners)
        {
            ownerNames.add(user.getName()+"#"+user.getDiscriminator());
        }
        this.bot.sendListMessage("These people have OWNER permissions on this server:", 
                                ownerNames, event.getMessage().getChannel(), Colors.PURPLE, 15, true);
        
        List<String> masterNames = new ArrayList<String>();
        List<IUser> masters = event.getGuildObject().getMasters();
        for (IUser user : masters)
        {
            masterNames.add(user.getName()+"#"+user.getDiscriminator());
        }
        if (!masterNames.isEmpty())
        {
            this.bot.sendListMessage("These people have MASTER permissions on this server:", 
                    masterNames, event.getMessage().getChannel(), Colors.PURPLE, 15, true);
        }
    }
    
    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp()
    {
        return "```"
                + "Show Masters Command \n"
                + "<User> \n\n"
                + "This command will show the users that have owner and master permissions on this server. \n\n\n"
                + "Related Commands: \n"
                + "- master \n"
                + "- nomaster\n"
                + "- owner\n"
                + "- noowner"
                + "```";
    }

    /**
     * @see bowt.cmnd.Command#copy()
     */
    @Override
    public Command copy()
    {
        return new ShowMastersCommand(this.validExpressions, this.permission, this.bot);
    }
}