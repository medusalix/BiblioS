package de.medusalix.biblios.sql.query.general;

import de.medusalix.biblios.sql.query.base.ActionQuery;
import javafx.util.Pair;
import de.medusalix.biblios.sql.query.base.ActionQuery;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UpdateQuery extends ActionQuery
{
    private String table;
    private List<Pair<String, Object>> pairs;

    public UpdateQuery(String table, List<Pair<String, Object>> pairs)
    {
        this.table = table;
        this.pairs = pairs;
    }

    public UpdateQuery(String table, String column, Object value)
    {
        this(table, Collections.singletonList(new Pair<>(column, value)));
    }

    @Override
    protected String getQueryString()
    {
        Stream<String> valuesWithQuotes = pairs.stream().map(value -> String.format("%s = '%s'", value.getKey(), value.getValue()));

        return String.format("UPDATE %s SET %s", table, valuesWithQuotes.collect(Collectors.joining(", ")));
    }
}
