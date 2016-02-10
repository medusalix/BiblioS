package de.medusalix.biblios.database.mappers;

import de.medusalix.biblios.database.objects.Stat;
import org.skife.jdbi.v2.BeanMapper;
import org.skife.jdbi.v2.StatementContext;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class StatMapper extends BeanMapper<Stat>
{
    public StatMapper()
    {
        super(Stat.class);
    }

    @Override
    public Stat map(int row, ResultSet rs, StatementContext ctx) throws SQLException
    {
        Stat stat = super.map(row, rs, ctx);

        ResultSetMetaData metaData = rs.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++)
        {
            String columnName = metaData.getColumnName(i);

            if (columnName.equals("Title"))
                stat.setBookTitle(rs.getString(i));
        }

        return stat;
    }
}
