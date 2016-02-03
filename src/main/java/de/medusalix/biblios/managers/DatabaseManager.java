package de.medusalix.biblios.managers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.sql.query.base.ActionQuery;
import de.medusalix.biblios.sql.query.specific.TableCreationQuery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseManager
{
    public static void init()
    {
        try
        {
            Files.createDirectories(Paths.get(Consts.Resources.DATA_FOLDER_PATH));

            try (Connection connection = openConnection())
            {
                ActionQuery studentsQuery = new TableCreationQuery(Consts.Database.CREATE_STUDENTS_TABLE_QUERY);
                ActionQuery booksQuery = new TableCreationQuery(Consts.Database.CREATE_BOOKS_TABLE_QUERY);
                ActionQuery borrowedBookQuery = new TableCreationQuery(Consts.Database.CREATE_BORROWED_BOOKS_TABLE_QUERY);
                ActionQuery statsQuery = new TableCreationQuery(Consts.Database.CREATE_STATS_TABLE_QUERY);

                studentsQuery.executeBatch(connection, booksQuery, borrowedBookQuery, statsQuery);
            }
        }

        catch (IOException | SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    public static Connection openConnection()
    {
        try
        {
            return DriverManager.getConnection(Consts.Database.CONNECTION_URL);
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }

        return null;
    }
}
