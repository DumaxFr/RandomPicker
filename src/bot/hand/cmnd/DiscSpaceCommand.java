package bot.hand.cmnd;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import bowt.bot.Bot;
import bowt.cmnd.Command;
import bowt.cons.Colors;
import bowt.evnt.impl.CommandEvent;
import bowt.guild.GuildObject;
import bowt.util.perm.UserPermissions;

/**
 * @author &#8904
 *
 */
public class DiscSpaceCommand extends Command
{
    private Bot bot;
    
    /**
     * @param validExpressions
     * @param permission
     */
    public DiscSpaceCommand(String[] validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission);
        this.bot = bot;
    }
    
    /**
     * @param validExpressions
     * @param permission
     */
    public DiscSpaceCommand(List<String> validExpressions, int permission, Bot bot) 
    {
        super(validExpressions, permission);
        this.bot = bot;
    }

    /**
     * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
     */
    @Override
    public void execute(CommandEvent event) 
    {
        String jarFolderSize = null;
        String logFolderSize = null;
        String dbFolderSize = null;
        try
        {
            String jarParentPath = getJarParentFile().getAbsolutePath();
            jarFolderSize = formatSize(size(new File(jarParentPath).toPath()));
            dbFolderSize = formatSize(size(new File(jarParentPath+"/db").toPath()));
            logFolderSize = formatSize(size(new File(jarParentPath+"/logs").toPath()));
        }
        catch(Exception e)
        {
            Bot.errorLog.print(this, e);
        }
        bot.sendMessage("```Total: "+jarFolderSize+"\n\n"
                +"Database: "+dbFolderSize+"\n"
                +"Logs: "+logFolderSize+"```", event.getMessage().getChannel(), Colors.PURPLE);
    }
    
    public File getJarParentFile()
    {
        try
        {
            CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
            File jarFile;
            if (codeSource.getLocation() != null)
            {
                jarFile = new File(codeSource.getLocation().toURI());
            }
            else
            {
                String path = getClass().getResource(getClass().getSimpleName() + ".class").getPath();
                String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
                jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
                jarFile = new File(jarFilePath);
            } 
            return jarFile.getParentFile();  
        }
        catch (Exception e)
        {
            Bot.errorLog.print(this, e);
        }
        return null;
    }
    
    public long size(Path path)
    {
        final AtomicLong size = new AtomicLong(0);
        try
        {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e)
        {
            Bot.errorLog.print(this, e);
        }
        return size.get();
    }
    
    private String formatSize(long size)
    {
        String[] units = {"b", "kb", "mb", "gb"};
        float actSize = (float)size;
        String unit = units[0];
        for (int i = 0; i < 4; i++)
        {
            if (actSize >= 1000)
            {
                actSize /= 1000;
                unit = units[i+1];
            }
        }
        return String.format("%.2f", actSize)+" "+unit;
    }
    
    /**
     * @see bowtie.bot.obj.Command#getHelp()
     */
    @Override
    public String getHelp(GuildObject guild) 
    {
        return "```"
                + "Disc Space Command \n"
                + "<Needs " + UserPermissions.getPermissionString(this.getPermissionOverride(guild)) + " permissions> \n\n"
                + "Shows how much disc space the bot takes. \n\n\n"
                + "Related Commands: \n"
                + "- stats \n"
                + "- ram \n"
                + "- threads"
                + "```";
    }
}