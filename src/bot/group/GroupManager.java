package bot.group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import bowt.guild.GuildObject;
import db.DatabaseAccess;

/**
 * @author &#8904
 *
 */
public class GroupManager
{
    private static CopyOnWriteArrayList<GroupManager> groupManagers = new CopyOnWriteArrayList<GroupManager>();
    private List<Group> groups;
    private final GuildObject guild;
    protected static DatabaseAccess db;

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
        for (Group group : groups)
        {
            if (group.getName().equals(name))
            {
                return group;
            }
        }
        if (db.existingGroup(guild.getStringID(), name))
        {
            Group group = db.getGroup(guild.getStringID(), name);
            groups.add(group);
            return group;
        }
        return null;
    }

    public boolean add(Group group)
    {
        if (db.addGroup(guild.getStringID(), group.getName(), group.getDate()))
        {
            this.groups.add(group);
            return true;
        }
        return false;
    }
    
    public boolean remove(String name)
    {
        for (Group group : groups)
        {
            if (group.getName().equals(name))
            {
                if (db.removeGroup(guild.getStringID(), name))
                {
                    db.removeGroupTimer(guild.getStringID(), name);
                    this.groups.remove(group);
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void setDatabase(DatabaseAccess db)
    {
        GroupManager.db = db;
    }
    
    public static void addManager(GroupManager manager)
    {
        groupManagers.add(manager);
    }
    
    /**
     * Returns either an existing manager or creates a new one.
     * 
     * @param guild
     * @return
     */
    public static GroupManager getManagerForGuild(GuildObject guild)
    {
        for (GroupManager manager : groupManagers)
        {
            if (manager.getGuild().equals(guild))
            {
                return manager;
            }
        }
        GroupManager newManager = new GroupManager(guild);
        addManager(newManager);
        return newManager;
    }
}