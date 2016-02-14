package de.medusalix.biblios.managers;

import de.medusalix.biblios.core.Consts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BackupManager
{
    public static void init()
    {
        createBackup("");
        deleteOldBackups();
    }

    private static Stream<String> getBackupStream()
    {
        try
        {
            return Files.list(Paths.get(Consts.Paths.BACKUP_FOLDER)).map(backup -> backup.getFileName().toString()
                    .replace(Consts.Database.BACKUP_PREFIX, "")
                    .replace(Consts.Database.SUFFIX, ""));
        }

        catch (IOException e)
        {
            ExceptionManager.log(e);
        }

        return null;
    }

    public static List<String> getBackups()
    {
        Stream<String> backupStream = getBackupStream();

        if (backupStream != null)
        {
            return backupStream.collect(Collectors.toList());
        }

        return null;
    }

    public static Path createBackup(String suffix)
    {
        Path backupPath = Paths.get(String.format("%s/%s%s%s%s", Consts.Paths.BACKUP_FOLDER, Consts.Database.BACKUP_PREFIX, LocalDateTime.now().format(Consts.Misc.DATE_TIME_FORMATTER), suffix, Consts.Database.SUFFIX));

        if (Files.notExists(backupPath))
        {
            try
            {
                Files.createDirectories(Paths.get(Consts.Paths.BACKUP_FOLDER));
                Files.copy(Paths.get(Consts.Paths.DATABASE_FULL), backupPath);
            }

            catch (IOException e)
            {
                ExceptionManager.log(e);
            }
        }

        return backupPath;
    }

    public static void deleteAllBackups()
    {
        try
        {
            Files.list(Paths.get(Consts.Paths.BACKUP_FOLDER)).forEach(backup ->
            {
                try
                {
                    Files.delete(backup);
                }

                catch (IOException e)
                {
                    ExceptionManager.log(e);
                }
            });
        }

        catch (IOException e)
        {
            ExceptionManager.log(e);
        }
    }

    public static void deleteOldBackups()
    {
        Stream<String> backupStream = getBackupStream();

        if (backupStream != null)
        {
            List<String> backups = backupStream.filter(backup -> !backup.endsWith(Consts.Database.MANUAL_BACKUP_SUFFIX) && !backup.endsWith(Consts.Database.START_OF_SCHOOL_BACKUP_SUFFIX)).collect(Collectors.toList());

            int numberOfBackups = backups.size();

            if (numberOfBackups > Consts.Database.MAX_NUMBER_OF_BACKUPS)
            {
                int backupsToDelete = numberOfBackups - Consts.Database.MAX_NUMBER_OF_BACKUPS;

                Stream<LocalDateTime> backupTimes = backups.stream().map(backup -> LocalDateTime.parse(backup, Consts.Misc.DATE_TIME_FORMATTER)).sorted();

                backupTimes.limit(backupsToDelete).forEach(backupTime ->
                {
                    String fullBackupName = backupTime.format(Consts.Misc.DATE_TIME_FORMATTER);

                    Path backupPath = Paths.get(String.format("%s/%s%s%s", Consts.Paths.BACKUP_FOLDER, Consts.Database.BACKUP_PREFIX, fullBackupName, Consts.Database.SUFFIX));

                    try
                    {
                        Files.delete(backupPath);
                    }

                    catch (IOException e)
                    {
                        ExceptionManager.log(e);
                    }
                });
            }
        }
    }
}
