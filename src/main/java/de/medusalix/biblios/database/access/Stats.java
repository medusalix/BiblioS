package de.medusalix.biblios.database.access;

import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.database.mappers.StatMapper;
import de.medusalix.biblios.database.objects.Stat;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(StatMapper.class)
public interface Stats
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Stat(BookId INT UNIQUE, NumberOfBorrows INT, FOREIGN KEY(BookId) REFERENCES Book(Id))")
    void createTable();

    @SqlUpdate("INSERT INTO Stat VALUES (:bookId, :numberOfBorrows)")
    void save(@BindBean Stat stat);

    @SqlQuery("SELECT BookId, NumberOfBorrows FROM Stat")
    List<Stat> findAll();

    @SqlQuery("SELECT NumberOfBorrows, Title FROM Stat JOIN Book ON BookId = Book.Id ORDER BY NumberOfBorrows DESC LIMIT " + Reference.Misc.MAX_BOOKS_IN_STATS)
    List<Stat> findAllWithBookTitle();

    @SqlQuery("SELECT COUNT(*) FROM Stat WHERE BookId = :bookId")
    int countByBookId(@Bind("bookId") long bookId);

    @SqlUpdate("UPDATE Stat SET NumberOfBorrows = NumberOfBorrows + 1 WHERE BookId = :bookId")
    void updateIncrementBorrows(@Bind("bookId") long bookId);

    @SqlUpdate("DELETE FROM Stat WHERE BookId = :bookId")
    void deleteByBookId(@Bind("bookId") long bookId);

    @SqlUpdate("DROP TABLE Stat")
    void deleteAll();
}
