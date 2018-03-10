package remote;

import java.io.IOException;

import bowt.data.ExchangeData;
import bowt.data.ret.ReturnManager;
import bowt.evnt.BowtieNetworkEvent;
import bowt.impl.BowtieServer;
import core.Main;

/**
 * @author &#8904
 *
 */
public class RemoteServer extends BowtieServer
{
    private Main main;
    
    public RemoteServer(int port, Main main) throws IOException
    {
        super(port);
        this.main = main;
    }
    
    @Override
    protected void processInput(ExchangeData data)
    {
        int type = data.getType();
        
        switch (type)
        {
            case ExchangeData.RETURN:
                ReturnManager.addData(data);
                break;
                
            case ExchangeData.PING:
                sendOutput(new ExchangeData<String>(String.class, "Pong", ExchangeData.RETURN, data.getID()));
                break;
                
            case ExchangeData.MANUAL_DISCONNECT:
                dispatchEvent(new BowtieNetworkEvent(BowtieNetworkEvent.MANUAL_DISCONNECT, "Client [" + this.client.getRemoteSocketAddress() + "] manually disconnected."));
                this.isConnected = false;
                break;
        }
    }
}