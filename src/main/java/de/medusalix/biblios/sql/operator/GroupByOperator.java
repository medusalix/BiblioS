package de.medusalix.biblios.sql.operator;

public class GroupByOperator implements Operator
{
    private String column;

    public GroupByOperator(String column)
    {
        this.column = column;
    }

    @Override
    public String getQueryString()
    {
        return String.format("GROUP BY %s", column);
    }
}
