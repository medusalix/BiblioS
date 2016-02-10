package de.medusalix.biblios.database.mappers;

import de.medusalix.biblios.database.objects.BorrowedBook;
import org.skife.jdbi.v2.BeanMapper;
import org.skife.jdbi.v2.StatementContext;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class BorrowedBookMapper extends BeanMapper<BorrowedBook>
{
    public BorrowedBookMapper()
    {
        super(BorrowedBook.class);
    }

    @Override
    public BorrowedBook map(int row, ResultSet rs, StatementContext ctx) throws SQLException
    {
        BorrowedBook borrowedBook = super.map(row, rs, ctx);

        ResultSetMetaData metaData = rs.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++)
        {
            String columnName = metaData.getColumnName(i);

            switch (columnName)
            {
                case "Name":
                    borrowedBook.setStudentName(rs.getString(i));
                    break;

                case "Title":
                    borrowedBook.setBookTitle(rs.getString(i));
                    break;
            }
        }

        return borrowedBook;
    }
}
