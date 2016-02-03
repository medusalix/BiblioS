package de.medusalix.biblios.sql.query.base;

import de.medusalix.biblios.managers.ReportManager;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ResultQuery extends Query
{
    public List<HashMap<String, Object>> execute(Connection connection)
    {
        List<HashMap<String, Object>> results = new ArrayList<>();

        try (Statement statement = connection.createStatement())
        {
            ResultSet result = statement.executeQuery(buildQuery());
            ResultSetMetaData metaData = result.getMetaData();

            while (result.next())
            {
                HashMap<String, Object> row = new HashMap<>();

                for (int i = 1; i <= metaData.getColumnCount(); i++)
                {
                    Object object = result.getObject(i);

                    row.put(metaData.getColumnName(i), object);
                }

                results.add(row);
            }
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }

        return results;
    }

    public <T> List<T> execute(Connection connection, Class<T> mappableClass)
    {
        List<T> results = new ArrayList<>();

        try (Statement statement = connection.createStatement())
        {
            ResultSet result = statement.executeQuery(buildQuery());
            ResultSetMetaData metaData = result.getMetaData();

            while (result.next())
            {
                T instance = mappableClass.newInstance();

                for (int i = 1; i <= metaData.getColumnCount(); i++)
                {
                    String fieldName = metaData.getColumnName(i);

                    fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

                    Field field = instance.getClass().getDeclaredField(fieldName);

                    field.setAccessible(true);
                    field.set(instance, result.getObject(i));
                }

                results.add(instance);
            }
        }

        catch (SQLException | InstantiationException | IllegalAccessException | NoSuchFieldException e)
        {
            ReportManager.reportException(e);
        }

        return results;
    }
}
