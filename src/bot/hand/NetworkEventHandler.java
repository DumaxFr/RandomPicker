package bot.hand;

import bowt.bot.Bot;
import bowt.evnt.BowtieNetworkEvent;
import bowt.evnt.intf.BowtieEventHandler;
import core.Main;

/**
 * @author &#8904
 *
 */
public class NetworkEventHandler implements BowtieEventHandler
{
    private Main main;
    
    public NetworkEventHandler(Main main)
    {
        this.main = main;
    }

    /**
     * @see bowt.evnt.intf.BowtieEventHandler#handle(bowt.evnt.BowtieNetworkEvent)
     */
    @Override
    public void handle(BowtieNetworkEvent e)
    {
        switch (e.code())
        {
            case BowtieNetworkEvent.CONNECT:
                Bot.log.print(this, e.message());
                break;
            case BowtieNetworkEvent.CONNECTION_INFO_KEY:
                this.main.getBot().sendMessage(e.message(), this.main.getBot().getClient().getApplicationOwner().getOrCreatePMChannel());
                break;
            case BowtieNetworkEvent.CONNECTION_INFO_PORT:
                this.main.getBot().sendMessage(e.message(), this.main.getBot().getClient().getApplicationOwner().getOrCreatePMChannel());
                break;
            case BowtieNetworkEvent.KEY_CHANGE:
                this.main.getBot().sendMessage(e.message(), this.main.getBot().getClient().getApplicationOwner().getOrCreatePMChannel());
                break;
            case BowtieNetworkEvent.DISCONNECT:
                Bot.log.print(this, e.message());
                break;
            case BowtieNetworkEvent.DATA_ERROR:
                Bot.errorLog.print(this, e.message());
                break;
            case BowtieNetworkEvent.SHUTDOWN:
                Bot.log.print(this, e.message());
                break;
            case BowtieNetworkEvent.PING_CHECK:
                break;
            default:
                Bot.log.print(this, e.message());
        }
    }
}