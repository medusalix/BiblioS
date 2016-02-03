package de.medusalix.biblios.sql.operator;

public class WhereNotOperator extends WhereOperator
{
    public WhereNotOperator(String column, Object value)
    {
        super(column, value);
    }

    @Override
    public String getQueryString()
    {
        return String.format("WHERE %s != '%s'", column, value);
    }
}
