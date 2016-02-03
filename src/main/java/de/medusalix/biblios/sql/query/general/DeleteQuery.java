package de.medusalix.biblios.sql.query.general;

import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.base.ActionQuery;

public class DeleteQuery extends ActionQuery
{
    private String table;

    public DeleteQuery(String table, String column, Object value)
    {
        this.table = table;

        addOperator(new WhereOperator(column, value));
    }

    public DeleteQuery(String table)
    {
        this.table = table;
    }

    @Override
    protected String getQueryString()
    {
        return String.format("DELETE FROM %s", table);
    }
}
