package de.medusalix.biblios.sql.query.base;

import de.medusalix.biblios.sql.operator.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Query
{
    protected List<Operator> operatorList = new ArrayList<>();

    public void addOperator(Operator operator)
    {
        operatorList.add(operator);
    }

    protected abstract String getQueryString();

    protected String buildQuery()
    {
        String operatorQueries = operatorList.stream().map(Operator::getQueryString).collect(Collectors.joining(" "));

        return String.format("%s %s", getQueryString(), operatorQueries);
    }
}
