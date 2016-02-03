package de.medusalix.biblios.sql.operator;

public class JoinOnOperator implements Operator
{
    private String table, column1, column2;

    public JoinOnOperator(String table, String column1, String column2)
    {
        this.table = table;
        this.column1 = column1;
        this.column2 = column2;
    }

    @Override
    public String getQueryString()
    {
        return String.format("JOIN %s ON %s = %s.%s", table, column1, table, column2);
    }
}
