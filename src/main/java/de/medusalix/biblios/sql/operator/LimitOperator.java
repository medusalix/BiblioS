package de.medusalix.biblios.sql.operator;

public class LimitOperator implements Operator
{
    private int limit;

    public LimitOperator(int limit)
    {
        this.limit = limit;
    }

    @Override
    public String getQueryString()
    {
        return String.format("LIMIT %s", limit);
    }
}
