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

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.BookDatabase;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.access.StatDatabase;
import de.medusalix.biblios.database.access.StudentDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseManager
{
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    
    private static DBI dbi = new DBI(Consts.Database.CONNECTION_URL);
    
    public static void createDatabase()
    {
        logger.info("Initializing database");
        
        Path dataFolderPath = Paths.get(Consts.Paths.DATA_FOLDER);
        
        if (!Files.exists(dataFolderPath))
        {
            try
            {
                Files.createDirectories(dataFolderPath);
            }
            
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        
        StudentDatabase studentDatabase = dbi.open(StudentDatabase.class);
        BookDatabase bookDatabase = dbi.open(BookDatabase.class);
        BorrowedBookDatabase borrowedBookDatabase = dbi.open(BorrowedBookDatabase.class);
        StatDatabase statDatabase = dbi.open(StatDatabase.class);
        
        studentDatabase.createTable();
        bookDatabase.createTable();
        borrowedBookDatabase.createTable();
        statDatabase.createTable();
        
        dbi.close(studentDatabase);
        dbi.close(bookDatabase);
        dbi.close(borrowedBookDatabase);
        dbi.close(statDatabase);
    }

    public static <T> T createDao(Class<T> daoClass)
    {
        return dbi.onDemand(daoClass);
    }
}
