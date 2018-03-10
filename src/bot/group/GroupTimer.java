package bot.group;

/**
 * @author &#8904
 *
 */
public class GroupTimer
{
    private final String name;
    private final long time;
    
    public GroupTimer(String name, long time)
    {
        this.name = name;
        this.time = time;
    }
    
    public long getTime()
    {
        return this.time;
    }
    
    public String getName()
    {
        return this.name;
    }
}