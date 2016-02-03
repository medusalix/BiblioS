package de.medusalix.biblios.sql.operator;

public class WhereLikeOperator extends WhereOperator
{
    public WhereLikeOperator(String column, Object value)
    {
        super(column, value);
    }

    @Override
    public String getQueryString()
    {
        return String.format("WHERE %s LIKE '%s'", column, value);
    }
}
