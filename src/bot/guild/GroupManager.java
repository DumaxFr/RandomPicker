package bot.guild;

import java.util.ArrayList;
import java.util.List;

import bowt.guild.GuildObject;

/**
 * @author &#8904
 *
 */
public class GroupManager
{
    private List<Group> groups;
    private final GuildObject guild;

    public GroupManager(GuildObject guild)
    {
        this.guild = guild;
        this.groups = new ArrayList<Group>();
    }
    
    public GuildObject getGuild()
    {
        return this.guild;
    }
    
    public Group getGroupByName(String name)
    {
        name = name.toLowerCase();
        for (Group group : groups)
        {
            if (group.getName().equals(name))
            {
                return group;
            }
        }
        return null;
    }

    public boolean add(Group group)
    {
        return this.groups.add(group);
    }
    
    public boolean add(GuildObject guild, String name)
    {
        return this.groups.add(new Group(name, this));
    }
    
    public boolean remove(String name)
    {
        for (Group group : groups)
        {
            if (group.getName().equals(name))
            {
                return this.groups.remove(group);
            }
        }
        return false;
    }
}