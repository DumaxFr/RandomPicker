package bot.hand.cmnd;

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
public class RemoveAutomatedCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public RemoveAutomatedCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public RemoveAutomatedCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        String[] parts = event.getFixedContent().split(" ");
        
        long id = -1;
        try
        {
            id = Long.parseLong(parts[1]);
        }
        catch (Exception e)
        {
            this.bot.sendMessage("You have to add a positive number.", event.getChannel(), Colors.RED);
            return;
        }
        
        AutomatedCommand command = AutomatedCommand.get(id, event.getGuildObject().getStringID());
        
        if (command == null)
        {
            this.bot.sendMessage("There is no active automated command with that number on your server.", event.getChannel(), Colors.RED);
            return;
        }
        
        command.stop();
        AutomatedCommand.remove(command);
        main.getDatabase().removeAutomatedCommand(id);
        this.bot.sendMessage("The automated command has been stopped.", event.getChannel(), Colors.GREEN);
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Stop Automated Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Stops the automated command with the given number. \n\n"
                + "You can look up the number with the \"showautomated\" comand.\n\n\n"
                + "Example:\n\n"
                + "\"" + Bot.getPrefix() + "stopauto 45"
                + "```";
    }
}