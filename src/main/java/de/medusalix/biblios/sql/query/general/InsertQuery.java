package de.medusalix.biblios.sql.query.general;

import de.medusalix.biblios.sql.query.base.ActionQuery;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InsertQuery extends ActionQuery
{
    private boolean noPrimaryKey;

    private String table;
    private Object[] values;

    public InsertQuery(String table, Object... values)
    {
        this.table = table;
        this.values = values;
    }

    public InsertQuery(boolean noPrimaryKey, String table, Object... values)
    {
        this.table = table;
        this.values = values;

        this.noPrimaryKey = noPrimaryKey;
    }

    @Override
    protected String getQueryString()
    {
        String baseQuery;

        if (noPrimaryKey)
        {
            baseQuery = "INSERT INTO %s VALUES (%s)";
        }

        else
        {
            baseQuery = "INSERT INTO %s VALUES (NULL, %s)";
        }

        Stream<String> valuesWithQuotes = Arrays.asList(values).stream().map(value -> String.format("'%s'", value));

        return String.format(baseQuery, table, valuesWithQuotes.collect(Collectors.joining(", ")));
    }
}
