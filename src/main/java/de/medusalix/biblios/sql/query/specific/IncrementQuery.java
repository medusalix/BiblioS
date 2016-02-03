package de.medusalix.biblios.sql.query.specific;

import de.medusalix.biblios.sql.query.base.ActionQuery;

public class IncrementQuery extends ActionQuery
{
    private String table, column;

    public IncrementQuery(String table, String column)
    {
        this.table = table;
        this.column = column;
    }

    @Override
    protected String getQueryString()
    {
        return String.format("UPDATE %s SET %s = %s + 1", table, column, column);
    }
}
