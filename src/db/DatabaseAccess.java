package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.RequestBuffer.IRequest;
import bot.group.Group;
import bot.group.GroupManager;
import bot.group.GroupTimer;
import bowt.bot.Bot;
import bowt.db.Database;
import bowt.guild.GuildObject;
import core.Main;
/**
 * @author &#8904
 *
 */
public class DatabaseAccess extends Database
{
    /** Indicates that the given word is known to the bot. */
    public static final int KNOWN = 1;
    /** Indicates that something is unknown to the bot. */
    public static final int UNKNOWN = 2;
    /** Indicates that the database action was succesfully executed. */
    public static final int SUCCESS = 3;
    public static final int ACTIVE = 4;
    public static final int NOT_ACTIVE = 5;
    private Main main;
    
    public DatabaseAccess(Main main)
    {
        super();
        this.main = main;
    }

    /**
     * @see bowt.db.Database#createTables()
     */
    @Override
    protected void createTables()
    {
        try (Statement statement = this.getConnection().createStatement())
        {
            statement.execute("CREATE TABLE groupTimers ("
                    + "ID INTEGER NOT NULL "
                    + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                    + "(START WITH 1, INCREMENT BY 1),"
                    + "guildID VARCHAR(100), "
                    + "destroyTime VARCHAR(100), "
                    + "groupName VARCHAR(100))");
            Bot.log.print(this, "Created groupTimers table.");
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, "Table groupTimers does already exist.");
        }
        
        try (Statement statement = this.getConnection().createStatement())
        {
            statement.execute("CREATE TABLE userGroups ("
                    + "ID INTEGER NOT NULL "
                    + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                    + "(START WITH 1, INCREMENT BY 1),"
                    + "guildID VARCHAR(100), "
                    + "date VARCHAR(15), "
                    + "groupName VARCHAR(100))");
            Bot.log.print(this, "Created userGroups table.");
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, "Table userGroups does already exist.");
        }
        
        try (Statement statement = this.getConnection().createStatement())
        {
            statement.execute("CREATE TABLE groupMembers ("
                    + "ID INTEGER NOT NULL "
                    + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                    + "(START WITH 1, INCREMENT BY 1),"
                    + "guildID VARCHAR(100), "
                    + "userID VARCHAR(100), "
                    + "groupName VARCHAR(100))");
            Bot.log.print(this, "Created groupMembers table.");
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, "Table groupMembers does already exist.");
        }
        
        try (Statement statement = this.getConnection().createStatement())
        {
            statement.execute("CREATE TABLE commandOverrides ("
                    + "ID INTEGER NOT NULL "
                    + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                    + "(START WITH 1, INCREMENT BY 1),"
                    + "guildID VARCHAR(100), "
                    + "command VARCHAR(100), "
                    + "permissionLevel VARCHAR(10))");
            Bot.log.print(this, "Created commandOverrides table.");
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, "Table commandOverrides does already exist.");
        }
        
        try (Statement statement = this.getConnection().createStatement())
        {
            statement.execute("CREATE TABLE masters ("
                    + "ID INTEGER NOT NULL "
                    + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                    + "(START WITH 1, INCREMENT BY 1),"
                    + "guildID VARCHAR(100),"
                    + "masterID VARCHAR(100),"
                    + "level VARCHAR(1))");
            Bot.log.print(this, "Created masters table.");
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, "Table masters does already exist.");
        }
        
        try (Statement statement = this.getConnection().createStatement())
        {
            statement.execute("CREATE TABLE banned ("
                    + "ID INTEGER NOT NULL "
                    + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
                    + "(START WITH 1, INCREMENT BY 1),"
                    + "userID VARCHAR(100))");
            Bot.log.print(this, "Created banned table.");
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, "Table banned does already exist.");
        }
    }
    
    public boolean existingOverride(String guildID, String command)
    {
        String sql = "SELECT * FROM commandOverrides WHERE guildID = ? AND command = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, command);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return false;
    }
    
    public boolean addOverride(String guildID, String command, int permission)
    {
        if (!existingOverride(guildID, command))
        {
            String sql = "INSERT INTO commandOverrides (guildID, command, permissionLevel) VALUES (?, ?, ?)";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, guildID);
                statement.setString(2, command);
                statement.setInt(3, permission);
                statement.executeUpdate();
                return true;
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
        }
        return false;
    }
    
    public boolean removeOverride(String guildID, String command)
    {
        if (existingOverride(guildID, command))
        {
            String sql = "DELETE FROM commandOverrides WHERE guildID = ? AND command = ?";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, guildID);
                statement.setString(2, command);
                statement.executeUpdate();
                return true;
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
        }
        return false;
    }
    
    public int getOverride(String guildID, String command)
    {
        if (existingOverride(guildID, command))
        {
            String sql = "SELECT * FROM commandOverrides WHERE guildID = ? AND command = ?";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, guildID);
                statement.setString(2, command);
                ResultSet result = statement.executeQuery();
                return result.getInt("permissionLevel");
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
        }
        return -1;
    }
    
    public Map<String, Integer> getOverridesForGuild(String guildID)
    {
        String sql = "SELECT * FROM commandOverrides WHERE guildID = ?";
        Map<String, Integer> overrides = new HashMap<String, Integer>();
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            ResultSet result = statement.executeQuery();
            
            while (result.next())
            {
                overrides.put(result.getString("command"), result.getInt("permissionLevel"));
            }
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return overrides;
    }
    
    public boolean existingGroup(String guildID, String groupName)
    {
        String sql = "SELECT * FROM userGroups WHERE guildID = ? AND groupName = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, groupName);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return false;
    }
    
    public boolean addGroup(String guildID, String groupName, String date)
    {
        if (!existingGroup(guildID, groupName))
        {
            String sql = "INSERT INTO userGroups (guildID, groupName, date) VALUES (?, ?, ?)";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, guildID);
                statement.setString(2, groupName);
                statement.setString(3, date);
                statement.executeUpdate();
                return true;
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
        }
        return false;
    }
    
    public boolean removeGroup(String guildID, String groupName)
    {
        if (existingGroup(guildID, groupName))
        {
            String sql = "DELETE FROM userGroups WHERE guildID = ? AND groupName = ?";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, guildID);
                statement.setString(2, groupName);
                statement.executeUpdate();
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
            
            sql = "DELETE FROM groupMembers WHERE guildID = ? AND groupName = ?";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, guildID);
                statement.setString(2, groupName);
                statement.executeUpdate();
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
            return true;
        }
        return false;
    }
    
    public boolean isInGroup(String guildID, String groupName, String userID)
    {
        String sql = "SELECT * FROM groupMembers WHERE guildID = ? AND groupName = ? AND userID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, groupName);
            statement.setString(3, userID);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return false;
    }
    
    public boolean addGroupMember(String guildID, String groupName, String userID)
    {
        if (!existingGroup(guildID, groupName))
        {
            String sql = "INSERT INTO groupMembers (guildID, groupName, userID) VALUES (?, ?, ?)";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, guildID);
                statement.setString(2, groupName);
                statement.setString(3, userID);
                statement.executeUpdate();
                return true;
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
        }
        return false;
    }
    
    public boolean removeGroupMember(String guildID, String groupName, String userID)
    {
        String sql = "DELETE FROM groupMembers WHERE guildID = ? AND groupName = ? AND userID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, groupName);
            statement.setString(3, userID);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return false;
    }
    
    public List<IUser> getGroupMembers(String guildID, String groupName)
    {
        String sql = "SELECT * FROM groupMembers WHERE guildID = ? AND groupName = ?";
        List<IUser> members = new ArrayList<IUser>();
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, groupName);
            ResultSet result = statement.executeQuery();
            
            while (result.next())
            {
                IUser member = RequestBuffer.request(new IRequest<IUser>()
                {
                    @Override
                    public IUser request()
                    {
                        try
                        {
                            return main.getBot().getClient().fetchUser(Long.parseLong(result.getString("userID")));
                        }
                        catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).get();
                
                if (member != null)
                {
                    members.add(member);
                }
            }
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return members;
    }
    
    public Group getGroup(String guildID, String groupName)
    {
        String sql = "SELECT * FROM userGroups WHERE guildID = ? AND groupName = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, groupName);
            ResultSet result = statement.executeQuery();
            
            if (result.next())
            {
                List<IUser> members = getGroupMembers(guildID, groupName);
                Group group = new Group(result.getString("groupName"), 
                        GroupManager.getManagerForGuild(main.getBot().getGuildObjectByID(guildID)),
                        result.getString("date"));
                group.setMembers(members);
                return group;
            }
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return null;
    }
    
    public int getGroupCount()
    {
        String sql = "SELECT * FROM userGroups";
        int count = 0;
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            ResultSet result = statement.executeQuery();
            while (result.next())
            {
                count++;
            }
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return count;
    }
    
    public List<GroupTimer> getGroupTimersForGuild(String guildID)
    {
        List<GroupTimer> timers = new ArrayList<GroupTimer>();
        String sql = "SELECT * FROM groupTimers WHERE guildID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            ResultSet result = statement.executeQuery();
            
            while (result.next())
            {
                timers.add(new GroupTimer(result.getString("groupName"), result.getLong("destroyTime")));
            }
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return timers;
    }
    
    public void addGroupTimer(String guildID, String groupName, long time)
    {
        String sql = "INSERT INTO groupTimers (guildID, groupName, destroyTime) VALUES (?, ?, ?)";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, groupName);
            statement.setLong(3, time);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
    }
    
    public void removeGroupTimer(String guildID, String groupName)
    {
        String sql = "DELETE FROM groupTimers WHERE guildID = ? AND groupName = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setString(2, groupName);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
    }
 
    public boolean isMaster(String masterID, String guildID)
    {
        String sql = "SELECT * FROM masters WHERE masterID = ? AND guildID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, masterID);
            statement.setString(2, guildID);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return false;
    }
    
    public List<String> getMasterIDs(GuildObject guild, int level)
    {
        return this.getMasterIDs(guild.getStringID(), level);
    }
    
    public List<String> getMasterIDs(IGuild guild, int level)
    {
        return this.getMasterIDs(guild.getStringID(), level);
    }
    
    public List<String> getMasterIDs(String guildID, int level)
    {
        List<String> ids = new ArrayList<String>();
        String sql = "SELECT * FROM masters WHERE guildID = ? AND level = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.setInt(2, level);
            ResultSet results = statement.executeQuery();
            while (results.next())
            {
                ids.add(results.getString("masterID"));
            }
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return ids;
    }
    
    public void addMaster(String masterID, String guildID, int level)
    {
        if (!this.isMaster(masterID, guildID))
        {
            String sql = "INSERT INTO masters (masterID, guildID, level) VALUES (?, ?, ?)";
            try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
            {
                statement.setString(1, masterID);
                statement.setString(2, guildID);
                statement.setInt(3, level);
                statement.executeUpdate();
            }
            catch (SQLException e)
            {
                Bot.errorLog.print(this, e);
            }
        }
    }
    
    public void removeMaster(String masterID, String guildID)
    {
        String sql = "DELETE FROM masters WHERE masterID = ? AND guildID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, masterID);
            statement.setString(2, guildID);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
    }
    
    public void removeMasters(GuildObject guild)
    {
        this.removeMasters(guild.getStringID());
    }
    
    public void removeMasters(IGuild guild)
    {
        this.removeMasters(guild.getStringID());
    }
    
    public void removeMasters(String guildID)
    {
        String sql = "DELETE FROM masters WHERE guildID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guildID);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
    }
    
    public void removeGuildInformation(GuildObject guild)
    {
        this.removeMasters(guild);
        String sql = "DELETE FROM statistic WHERE guildID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, guild.getStringID());
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
    }
    
    public List<String> getBannedUsers()
    {
        String sql = "SELECT * FROM banned";
        List<String> ids = new ArrayList<String>();
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            ResultSet results = statement.executeQuery();
            while (results.next())
            {
                ids.add(results.getString("userID"));
            }
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
        return ids;
    }
    
    public void banUser(String userID)
    {
        String sql = "INSERT INTO banned (userID) VALUES (?)";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, userID);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
    }
    
    public void unbanUser(String userID)
    {
        String sql = "DELETE FROM banned WHERE userID = ?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql))
        {
            statement.setString(1, userID);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Bot.errorLog.print(this, e);
        }
    }
}