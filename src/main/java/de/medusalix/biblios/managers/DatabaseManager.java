package de.medusalix.biblios.managers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.Books;
import de.medusalix.biblios.database.access.BorrowedBooks;
import de.medusalix.biblios.database.access.Stats;
import de.medusalix.biblios.database.access.Students;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.exceptions.DBIException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseManager
{
    private static DBI dbi = new DBI(Consts.Database.CONNECTION_URL);

    public static <T> T createDao(Class<T> daoClass)
    {
        return dbi.onDemand(daoClass);
    }

    public static void init()
    {
        try
        {
            Files.createDirectories(Paths.get(Consts.Paths.DATA_FOLDER));

            dbi.onDemand(Students.class).createTable();
            dbi.onDemand(Books.class).createTable();
            dbi.onDemand(BorrowedBooks.class).createTable();
            dbi.onDemand(Stats.class).createTable();
        }

        catch (IOException | DBIException e)
        {
            ExceptionManager.log(e);
        }
    }
}
