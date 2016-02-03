package de.medusalix.biblios.sql.query.base;

import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.managers.ReportManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ActionQuery extends Query
{
    public void execute(Connection connection)
    {
        try (Statement statement = connection.createStatement())
        {
            statement.executeUpdate(buildQuery());
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    public void executeAsync(Connection connection, Runnable finished)
    {
        runAsync(connection, () -> execute(connection), finished);
    }

    public void executeBatch(Connection connection, ActionQuery... queries)
    {
        try (Statement statement = connection.createStatement())
        {
            statement.addBatch(buildQuery());

            for (ActionQuery query : queries)
            {
                statement.addBatch(query.buildQuery());
            }

            statement.executeBatch();
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    public void executeBatchAsync(Connection connection, Runnable finished, ActionQuery... queries)
    {
        runAsync(connection, () -> executeBatch(connection, queries), finished);
    }

    private void runAsync(Connection connection, Runnable runnable, Runnable finished)
    {
        // DON'T run it as a daemon because it can corrupt the database
        new Thread(() ->
        {
            runnable.run();
            finished.run();
        }).start();
    }
}
