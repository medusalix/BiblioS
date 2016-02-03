package de.medusalix.biblios.sql.query.specific;

import de.medusalix.biblios.sql.query.base.ActionQuery;
import de.medusalix.biblios.sql.query.base.ActionQuery;

public class TableCreationQuery extends ActionQuery
{
    private String tableCreationQueryString;

    public TableCreationQuery(String tableCreationQueryString)
    {
        this.tableCreationQueryString = tableCreationQueryString;
    }

    @Override
    protected String getQueryString()
    {
        return tableCreationQueryString;
    }
}
