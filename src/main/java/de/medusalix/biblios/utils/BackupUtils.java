/*
 * Copyright (C) 2016 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.medusalix.biblios.utils;

import de.medusalix.biblios.Consts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BackupUtils
{
    public static List<Backup> findBackups() throws IOException
    {
        return Files.list(Consts.Paths.BACKUP_FOLDER)
            .filter(path -> path.getFileName().toString().endsWith(Consts.Database.EXTENSION))
            .map(path ->
            {
                String fileName = path.getFileName()
                    .toString()
                    .replace(Consts.Database.BACKUP_PREFIX, "")
                    .replace(Consts.Database.EXTENSION, "");

                int separatorCount = fileName.length() - fileName.replace("-", "").length();

                // No suffix appended
                if (separatorCount == 3)
                {
                    return new Backup(
                        path,
                        fileName,
                        LocalDateTime.parse(fileName, Consts.Misc.DATE_TIME_FORMATTER)
                    );
                }

                else
                {
                    int separatorIndex = fileName.lastIndexOf('-');

                    LocalDateTime time = LocalDateTime.parse(
                        fileName.substring(0, separatorIndex),
                        Consts.Misc.DATE_TIME_FORMATTER
                    );
                    String suffix = fileName.substring(separatorIndex);

                    return new Backup(path, fileName, time, suffix);
                }
            })
            .collect(Collectors.toList());
    }

    public static void createBackup(String suffix) throws IOException
    {
        String name = Consts.Database.BACKUP_PREFIX
            + LocalDateTime.now().format(Consts.Misc.DATE_TIME_FORMATTER)
            + suffix
            + Consts.Database.EXTENSION;

        Path backupPath = Consts.Paths.BACKUP_FOLDER.resolve(name);

        // If two backups are created in the same second, this can occur
        if (Files.exists(backupPath))
        {
            return;
        }

        Files.createDirectories(Consts.Paths.BACKUP_FOLDER);
        Files.copy(Consts.Paths.DATABASE, backupPath);
    }

    public static void createBackup() throws IOException
    {
        createBackup("");
    }

    public static void deleteObsoleteBackups() throws IOException
    {
        // Sorted from oldest to newest
        Iterator<Backup> iterator = findBackups()
            .stream()
            .filter(backup -> backup.getSuffix() == null)
            .sorted()
            .skip(Consts.Database.BACKUP_LIMIT)
            .iterator();

        // We cannot use forEach, because of the exceptions
        while (iterator.hasNext())
        {
            iterator.next().delete();
        }
    }

    public static void deleteAllBackups() throws IOException
    {
        List<Backup> backups = findBackups();

        // We cannot use forEach, because of the exceptions
        for (Backup backup : backups)
        {
            backup.delete();
        }
    }

    public static class Backup implements Comparable<Backup>
    {
        private Path path;
        private String name;

        private LocalDateTime time;
        private String suffix;

        public Backup(Path path, String name, LocalDateTime time, String suffix)
        {
            this.path = path;
            this.name = name;

            this.time = time;
            this.suffix = suffix;
        }

        public Backup(Path path, String name, LocalDateTime time)
        {
            this(path, name, time, null);
        }

        @Override
        public String toString()
        {
            return name;
        }

        @Override
        public int compareTo(Backup o)
        {
            // From oldest to newest
            return o.getTime().compareTo(time);
        }

        public String getName()
        {
            return name;
        }

        public LocalDateTime getTime()
        {
            return time;
        }

        public String getSuffix()
        {
            return suffix;
        }

        public void restoreDatabase() throws IOException
        {
            Files.move(path, Consts.Paths.DATABASE, StandardCopyOption.REPLACE_EXISTING);
        }

        public void delete() throws IOException
        {
            Files.delete(path);
        }
    }
}
