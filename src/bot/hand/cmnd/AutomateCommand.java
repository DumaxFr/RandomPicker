package bot.hand.cmnd;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import bot.auto.AutomatedCommand;
import bot.auto.DummyMessage;
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
public class AutomateCommand extends Command
{
    private Bot bot;
    private Main main;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public AutomateCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public AutomateCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
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
        String[] parts = event.getMessage().getContent().split("<>");
        String content = "";
        
        if (parts.length < 2)
        {
            this.bot.sendMessage("You have to add the command including all parameters. Example: '" 
                                    + Bot.getPrefix() + "automate 1h 30m <> r-group group1'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        String[] times = parts[0].trim().split(" ");
        
        int days = 0;
        int hours = 0;
        int minutes = 0;
        
        for (int i = 1; i < times.length; i++)
        {
            if (times[i].toLowerCase().contains("d"))
            {
                days = getNumber(times[i], "d");
                if (days < 1)
                {
                    this.bot.sendMessage("'" + times[i] + "' seems to be in a wrong format. Only positive numbers formatted like '1d' or '15d' are allowed.", 
                            event.getMessage().getChannel(), Colors.RED);
                    return;
                }
            }
            else if (times[i].toLowerCase().contains("h"))
            {
                hours = getNumber(times[i], "h");
                if (hours < 1)
                {
                    this.bot.sendMessage("'" + times[i] + "' seems to be in a wrong format. Only positive numbers formatted like '3h' or '10h' are allowed.", 
                            event.getMessage().getChannel(), Colors.RED);
                    return;
                }
            }
            else if (times[i].toLowerCase().contains("m"))
            {
                minutes = getNumber(times[i], "m");
                if (minutes < 1)
                {
                    this.bot.sendMessage("'" + times[i] + "' seems to be in a wrong format. Only positive numbers formatted like '5m' or '33m' are allowed.", 
                            event.getMessage().getChannel(), Colors.RED);
                    return;
                }
            }
        }
        if (days > 365)
        {
            this.bot.sendMessage("The amount of days may not be higher than 365.", 
                    event.getMessage().getChannel(), Colors.RED);
            return;
        }
        if (hours > 168)
        {
            this.bot.sendMessage("The amount of hours may not be higher than 168.", 
                    event.getMessage().getChannel(), Colors.RED);
            return;
        }
        if (minutes > 1440)
        {
            this.bot.sendMessage("The amount of minutes may not be higher than 1440.", 
                    event.getMessage().getChannel(), Colors.RED);
            return;
        }
        long interval = 0;
        interval += minutes * 60000L;
        interval += hours * 3600000L;
        interval += days * 86400000L;
        
        if (interval == 0)
        {
            this.bot.sendMessage("You have to add at least 1 minute as an interval between commands.", 
                    event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        String commandName = parts[1].replace(Bot.getPrefix(), "").trim().split(" ")[0];
        Command cmd = Main.commandHandler.getCommand(commandName);
        
        if (cmd == null)
        {
            this.bot.sendMessage("'" + commandName + "' is not a valid command.", 
                    event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        if (cmd instanceof AutomateCommand)
        {
            this.bot.sendMessage("This command can not be automated.", 
                    event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        if (!cmd.isValidPermission(UserPermissions.getPermissionLevel(event.getAuthor(), event.getGuildObject()), event.getGuildObject()))
        {
            this.bot.sendMessage("You don't have a high enough permission level to execute the '" + commandName + "' command.", 
                    event.getMessage().getChannel(), Colors.RED);
            return;
        }
        
        List<Long> mentions = new ArrayList<>();
        
        for (IUser user : event.getMentions())
        {
            mentions.add(user.getLongID());
        }
        
        List<Long> roleMentions = new ArrayList<>();
        for (IRole role : event.getMessage().getRoleMentions())
        {
            roleMentions.add(role.getLongID());
        }
        
        content += parts[1].trim();
        
        if (parts.length > 2)
        {
            if (parts[2].length() > 100)
            {
                this.bot.sendMessage("The additional text may not be longer than 100 characters.", 
                        event.getMessage().getChannel(), Colors.RED);
                return;
            }
            
            content += " <> " + parts[2]; 
        }
        
        DummyMessage message = new DummyMessage(bot.getClient(), event.getMessage().getLongID(), content, 
                                                    event.getAuthor(), event.getChannel(), event.getMessage().mentionsEveryone(), mentions, roleMentions);
        
        AutomatedCommand command = new AutomatedCommand(AutomatedCommand.currentID++, message, event.getGuildObject(), main, interval);
        AutomatedCommand.add(command);
        main.getDatabase().addAutomatedCommand(command);
    }
    
    private int getNumber(String text, String replace)
    {
        String numberText = text.toLowerCase().replace(replace.toLowerCase(), "");
        int number = -1;
        try
        {
            number = Integer.parseInt(numberText);
        }
        catch (NumberFormatException e)
        {
        }
        return number;
    }

    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Automate Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Makes the bot automatically use the command with the given delay between executions. \n\n"
                + "The first part is the automation command with the desired delay, see below which time units are valid. "
                + "The second part (separated by <>) is the command which should be automatically executed. Make sure to add all parameters "
                + "that are needed. The third part (again separated by <>) is an optional piece of text which will be displayed right before the command "
                + "execution. \n\n\n"
                + "Example: \n\n"
                + Bot.getPrefix() + "auto 2h <> " + Bot.getPrefix() + "all\n\n"
                + "This will use the 'all' command every 2 hours.\n\n\n"
                + "Valid time units are d (days), h (Hours), m (Minutes). You can mix them if needed, "
                + "just make sure to separate them with spaces.\n\n\n"
                + Bot.getPrefix() + "auto 15d 2h 45m <> " + Bot.getPrefix() + "group group1 <> I picked this guy from group1:\n\n"
                + "The bot will pick a user from 'group1' every 15 days 2 hours and 45 minutes. It will display the text 'I picked this guy from group1:' "
                + "before it displays the picked user."
                + "```";
    }
}