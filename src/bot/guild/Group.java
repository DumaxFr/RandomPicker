package bot.guild;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sx.blah.discord.handle.obj.IUser;
import core.Main;

/**
 * @author &#8904
 *
 */
public class Group
{
    public static final long lifeSpan = 7200000;
    private final String name;
    private List<IUser> members;
    private GroupManager manager;
    private Timer timer;
    
    public Group(String name, GroupManager manager)
    {
        this.name = name;
        this.members = new ArrayList<IUser>();
        this.manager = manager;
        start();
        Main.channelLog.print("Opened group '" + name + "' on '" + manager.getGuild().getGuild().getName() + "'.");
    }
    
    public boolean add(IUser user)
    {
        if (!this.members.contains(user))
        {
            if (this.members.add(user))
            {
                resetTimer();
                return true;
            }
        }
        return false;
    }
    
    public boolean remove(IUser user)
    {
        return this.members.remove(user);
    }
    
    public List<IUser> getMembers()
    {
        return this.members;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void resetTimer()
    {
        this.timer.cancel();
        start();
    }
    
    private void start()
    {
        this.timer = new Timer();
        this.timer.schedule(new TimerTask(){
            @Override
            public void run()
            {
                manager.remove(name);
                Main.channelLog.print("Closed group '" + name + "' on '" + manager.getGuild().getGuild().getName() + "'.");
            }
        }, lifeSpan); 
    }
}