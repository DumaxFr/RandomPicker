package bot.hand.cmnd;

import java.util.ArrayList;
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
public class ShowAutomatedCommandsCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public ShowAutomatedCommandsCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public ShowAutomatedCommandsCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        List<AutomatedCommand> commands = AutomatedCommand.get(event.getGuildObject().getStringID());
        
        List<String> titles = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        
        for (AutomatedCommand command : commands)
        {
            titles.add(Long.toString(command.getID()));
            String message = "";
            if (command.getAddText() != null)
            {
                message += command.getAddText() + "\n";
            }
            message += command.getCommandString();
            messages.add(message);
        }
        
        bot.sendListMessage("Active automated commands on this server:", titles, messages, event.getChannel(), Colors.DEFAULT, 10, false);
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Show Automated Commands \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Shows the active automated commands on your server. The number is the ID of the command which you "
                + "can use to stop the automation. \n\n"
                + "Example: \n\n"
                + Bot.getPrefix()+"showauto"
                + "\n\n\n"
                + "Related Commands: \n"
                + "- auto \n"
                + "- stopauto"
                + "```";
    }
}