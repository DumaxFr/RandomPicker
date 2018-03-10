package bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import bowt.bot.Bot;
import bowt.guild.GuildObject;
import core.Main;

/**
 * Handles {@link GuildLeaveEvent}s for the bot.
 * @author &#8904
 */
public class GuildKickHandler implements IListener<GuildLeaveEvent>{
    private Bot bot;
    private Main main;
    
    /**
     * Creates a new instance for the given bot.
     * @param bot
     */
    public GuildKickHandler(Bot bot, Main main){
        this.bot = bot;
        this.main = main;
    }
    
    /**
     * Removes the representing {@link GuildObject} from the {@link Bot#guilds} list,
     * clears every entry about this guild from the database.
     * 
     * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
     */
    @Override
    public void handle(GuildLeaveEvent event){
        GuildObject guild = this.bot.getGuildObjectByID(event.getGuild().getStringID());
        this.bot.removeGuildObject(guild);
        Bot.log.print(this, "Bot was kicked from '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")");
    }
}