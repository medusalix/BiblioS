package de.medusalix.biblios.managers;

import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.helpers.Exceptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
            return Files.list(java.nio.file.Paths.get(Reference.Paths.BACKUP_FOLDER)).map(backup -> backup.getFileName().toString()
                                                                                                          .replace(Reference.Database.BACKUP_PREFIX, "")
                                                                                                          .replace(Reference.Database.SUFFIX, ""));
        }

        catch (IOException e)
        {
            Exceptions.log(e);
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
        Path backupPath = java.nio.file.Paths.get(String.format("%s/%s%s%s%s", Reference.Paths.BACKUP_FOLDER, Reference.Database.BACKUP_PREFIX, LocalDateTime.now().format(Reference.Misc.DATE_TIME_FORMATTER), suffix, Reference.Database.SUFFIX));

        if (Files.notExists(backupPath))
        {
            try
            {
                Files.createDirectories(java.nio.file.Paths.get(Reference.Paths.BACKUP_FOLDER));
                Files.copy(java.nio.file.Paths.get(Reference.Paths.DATABASE_FULL), backupPath);
            }

            catch (IOException e)
            {
                Exceptions.log(e);
            }
        }

        return backupPath;
    }

    public static void deleteAllBackups()
    {
        try
        {
            Files.list(java.nio.file.Paths.get(Reference.Paths.BACKUP_FOLDER)).forEach(backup ->
            {
                try
                {
                    Files.delete(backup);
                }

                catch (IOException e)
                {
                    Exceptions.log(e);
                }
            });
        }

        catch (IOException e)
        {
            Exceptions.log(e);
        }
    }

    public static void deleteOldBackups()
    {
        Stream<String> backupStream = getBackupStream();

        if (backupStream != null)
        {
            List<String> backups = backupStream.filter(backup -> !backup.endsWith(Reference.Database.MANUAL_BACKUP_SUFFIX) && !backup.endsWith(Reference.Database.START_OF_SCHOOL_BACKUP_SUFFIX)).collect(Collectors.toList());

            int numberOfBackups = backups.size();

            if (numberOfBackups > Reference.Database.MAX_NUMBER_OF_BACKUPS)
            {
                int backupsToDelete = numberOfBackups - Reference.Database.MAX_NUMBER_OF_BACKUPS;

                Stream<LocalDateTime> backupTimes = backups.stream().map(backup -> LocalDateTime.parse(backup, Reference.Misc.DATE_TIME_FORMATTER)).sorted();

                backupTimes.limit(backupsToDelete).forEach(backupTime ->
                {
                    String fullBackupName = backupTime.format(Reference.Misc.DATE_TIME_FORMATTER);

                    Path backupPath = java.nio.file.Paths.get(String.format("%s/%s%s%s", Reference.Paths.BACKUP_FOLDER, Reference.Database.BACKUP_PREFIX, fullBackupName, Reference.Database.SUFFIX));

                    try
                    {
                        Files.delete(backupPath);
                    }

                    catch (IOException e)
                    {
                        Exceptions.log(e);
                    }
                });
            }
        }
    }
}
