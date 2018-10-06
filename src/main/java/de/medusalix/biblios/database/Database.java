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

package de.medusalix.biblios.database;

import de.medusalix.biblios.Consts;
import de.medusalix.biblios.database.access.BookDatabase;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.access.StatDatabase;
import de.medusalix.biblios.database.access.StudentDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.nio.file.Files;

public class Database
{
    private static final Logger logger = LogManager.getLogger(Database.class);
    
    private static Jdbi jdbi = Jdbi.create(Consts.Database.CONNECTION_URL);
    
    public static void create()
    {
        logger.info("Initializing database");

        try
        {
            Files.createDirectories(Consts.Paths.DATA_FOLDER);
        }

        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.useExtension(StudentDatabase.class, StudentDatabase::createTable);
        jdbi.useExtension(BookDatabase.class, BookDatabase::createTable);
        jdbi.useExtension(BorrowedBookDatabase.class, BorrowedBookDatabase::createTable);
        jdbi.useExtension(StatDatabase.class, StatDatabase::createTable);

        // Truncate the history of returned books
        jdbi.useExtension(BorrowedBookDatabase.class, db ->
            db.deleteOldestReturned(Consts.Database.RETURNED_BOOKS_LIMIT)
        );
    }

    public static <T> T get(Class<T> clazz)
    {
        return jdbi.onDemand(clazz);
    }
}
