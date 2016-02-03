package de.medusalix.biblios.sql.query.general;

import de.medusalix.biblios.sql.query.base.ResultQuery;
import de.medusalix.biblios.sql.query.base.ResultQuery;

public class SelectQuery extends ResultQuery
{
    private String table;
    private String[] columns;

    public SelectQuery(String table, String... columns)
    {
        this.table = table;
        this.columns = columns;
    }

    @Override
    protected String getQueryString()
    {
        return String.format("SELECT %s FROM %s", String.join(", ", columns), table);
    }
}
