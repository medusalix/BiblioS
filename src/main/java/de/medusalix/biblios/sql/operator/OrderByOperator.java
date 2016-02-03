package de.medusalix.biblios.sql.operator;

public class OrderByOperator implements Operator
{
    private String column;
    private Order order;

    public OrderByOperator(String column, Order order)
    {
        this.column = column;
        this.order = order;
    }

    public enum Order
    {
        ASCENDING,
        DESCENDING
    }

    @Override
    public String getQueryString()
    {
        String orderKeyword = null;

        switch (order)
        {
            case ASCENDING:
                orderKeyword = "ASC";
                break;

            case DESCENDING:
                orderKeyword = "DESC";
                break;
        }

        return String.format("ORDER BY %s %s", column, orderKeyword);
    }
}
