package bot.hand.cmnd;

import java.time.LocalDateTime;
import java.util.List;

import sx.blah.discord.util.RequestBuffer;
import bot.group.Group;
import bot.group.GroupManager;
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
public class CreateGroupCommand extends Command
{
    private Bot bot;
    private Main main;

    /**
     * @param validExpressions
     * @param permission
     */
    public CreateGroupCommand(String[] validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }
    
    public CreateGroupCommand(List<String> validExpressions, int permission, Bot bot, Main main) 
    {
        super(validExpressions, permission, true);
        this.bot = bot;
        this.main = main;
    }

    /**
     * @see bowt.cmnd.Command#execute(bowt.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event)
    {
        GroupManager manager = GroupManager.getManagerForGuild(event.getGuildObject());
        String[] parts = event.getMessage().getContent().trim().toLowerCase().split(" ");
        String name = "";
        long destroyTime = -1;
        if (parts.length == 2)
        {
            name = parts[1];
        }
        else if (parts.length < 2)
        {
            this.bot.sendMessage("You have to add the name of the group. Example: '" + Bot.getPrefix() + "create groupname'.", event.getMessage().getChannel(), Colors.RED);
            return;
        }
        else if (parts.length > 2)
        {
            name = parts[1];
            
            int weeks = 0;
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            
            for (int i = 2; i < parts.length; i++)
            {
                if (parts[i].toLowerCase().contains("w"))
                {
                    weeks = getNumber(parts[i], "w");
                    if (weeks < 0)
                    {
                        this.bot.sendMessage("'" + parts[i] + "' seems to be in a wrong format. Only positive numbers formatted like '2w' or '34w' are allowed.", 
                                event.getMessage().getChannel(), Colors.RED);
                        return;
                    }
                }
                else if (parts[i].toLowerCase().contains("d"))
                {
                    days = getNumber(parts[i], "d");
                    if (days < 0)
                    {
                        this.bot.sendMessage("'" + parts[i] + "' seems to be in a wrong format. Only positive numbers formatted like '1d' or '15d' are allowed.", 
                                event.getMessage().getChannel(), Colors.RED);
                        return;
                    }
                }
                else if (parts[i].toLowerCase().contains("h"))
                {
                    hours = getNumber(parts[i], "h");
                    if (hours < 0)
                    {
                        this.bot.sendMessage("'" + parts[i] + "' seems to be in a wrong format. Only positive numbers formatted like '3h' or '10h' are allowed.", 
                                event.getMessage().getChannel(), Colors.RED);
                        return;
                    }
                }
                else if (parts[i].toLowerCase().contains("m"))
                {
                    minutes = getNumber(parts[i], "m");
                    if (minutes < 0)
                    {
                        this.bot.sendMessage("'" + parts[i] + "' seems to be in a wrong format. Only positive numbers formatted like '5m' or '33m' are allowed.", 
                                event.getMessage().getChannel(), Colors.RED);
                        return;
                    }
                }
                else if (parts[i].toLowerCase().contains("s"))
                {
                    seconds = getNumber(parts[i], "s");
                    if (seconds < 0)
                    {
                        this.bot.sendMessage("'" + parts[i] + "' seems to be in a wrong format. Only positive numbers formatted like '10s' or '40s' are allowed.", 
                                event.getMessage().getChannel(), Colors.RED);
                        return;
                    }
                }
            }
            if (weeks > 52)
            {
                this.bot.sendMessage("The amount of weeks may not be higher than 52.", 
                        event.getMessage().getChannel(), Colors.RED);
                return;
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
            if (seconds > 300)
            {
                this.bot.sendMessage("The amount of seconds may not be higher than 300.", 
                        event.getMessage().getChannel(), Colors.RED);
                return;
            }
            
            
            destroyTime += seconds * 1000L;
            destroyTime += minutes * 60000L;
            destroyTime += hours * 3600000L;
            destroyTime += days * 86400000L;
            destroyTime += weeks * 4233600000L;
        }
        
        LocalDateTime date = LocalDateTime.now();
        String dateString = date.getYear() + "." + date.getMonth().getValue() + "." + date.getDayOfMonth();
        Group group = new Group(name, manager, dateString);
        if (manager.add(group))
        {
            if (destroyTime != -1)
            {
                group.destroyAt(System.currentTimeMillis() + destroyTime);
                main.getDatabase().addGroupTimer(event.getGuildObject().getStringID(), name, System.currentTimeMillis() + destroyTime);
                RequestBuffer.request(() -> event.getMessage().addReaction(EmojiManager.getForAlias("alarm_clock"))).get();
            }
            RequestBuffer.request(() -> event.getMessage().addReaction(EmojiManager.getForAlias("white_check_mark"))).get();
        }
        else
        {
            bot.sendMessage("That group does already exist.", event.getChannel(), Colors.RED);
        }
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
     * @see bowt.cmnd.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild)
    {
        return "```"
                + "Create Group Command \n"   
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Creates a new group with the given name. \n\n\n"
                + "Usage: \n\n"
                + Bot.getPrefix() + "create team1"
                + "\n\n\n"
                + "You can set a time after which this group will be closed automatically. To do that add the desired delay "
                + "after the name of the group like so: \n\n"
                + Bot.getPrefix() + "create team1 2h\n\n"
                + "Valid time units are w (Weeks), d (days), h (Hours), m (Minutes), s (Seconds). You can mix them if needed, "
                + "just make sure to separate them with spaces.\n\n"
                + Bot.getPrefix() + "create team1 3w 15d 2h 45m 94s\n\n"
                + "Related commands: \n"
                + "- close\n"
                + "- join\n"
                + "- leave"
                + "```";
    }
}