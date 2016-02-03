package de.medusalix.biblios.sql.query.specific;

import de.medusalix.biblios.sql.query.base.ActionQuery;
import de.medusalix.biblios.sql.query.base.ActionQuery;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InsertOrIgnoreQuery extends ActionQuery
{
    private String table;
    private Object[] values;

    public InsertOrIgnoreQuery(String table, Object... values)
    {
        this.table = table;
        this.values = values;
    }

    @Override
    protected String getQueryString()
    {
        Stream<String> valuesWithQuotes = Arrays.asList(values).stream().map(value -> String.format("'%s'", value));

        return String.format("INSERT OR IGNORE INTO %s VALUES (%s)", table, valuesWithQuotes.collect(Collectors.joining(", ")));
    }
}
