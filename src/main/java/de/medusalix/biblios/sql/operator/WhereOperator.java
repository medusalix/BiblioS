package de.medusalix.biblios.sql.operator;

public class WhereOperator implements Operator
{
    protected String column;
    protected Object value;

    public WhereOperator(String column, Object value)
    {
        this.column = column;
        this.value = value;
    }

    @Override
    public String getQueryString()
    {
        return String.format("WHERE %s = '%s'", column, value);
    }
}
