package de.medusalix.biblios.database.access;

import de.medusalix.biblios.database.mappers.BorrowedBookMapper;
import de.medusalix.biblios.database.objects.BorrowedBook;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(BorrowedBookMapper.class)
public interface BorrowedBooks
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS BorrowedBook(Id IDENTITY PRIMARY KEY, StudentId INT, BookId INT UNIQUE, BorrowDate VARCHAR(255), ReturnDate VARCHAR(255), FOREIGN KEY(StudentId) REFERENCES Student(Id), FOREIGN KEY(BookId) REFERENCES Book(Id))")
    void createTable();

    @SqlUpdate("INSERT INTO BorrowedBook VALUES (NULL, :studentId, :bookId, :borrowDate, :returnDate)")
    void save(@BindBean BorrowedBook borrowedBook);

    @SqlQuery("SELECT Id, StudentId, BookId, BorrowDate, ReturnDate FROM BorrowedBook")
    List<BorrowedBook> findAll();

    @SqlQuery("SELECT BookId, Name FROM BorrowedBook JOIN Student ON StudentId = Student.Id")
    List<BorrowedBook> findAllWithStudentName();

    @SqlQuery("SELECT BorrowedBook.Id, BorrowDate, ReturnDate, Title FROM BorrowedBook JOIN Book ON BookId = Book.Id WHERE StudentId = :studentId")
    List<BorrowedBook> findAllWithBookTitleFromStudentId(@Bind("studentId") long studentId);

    @SqlQuery("SELECT ReturnDate, Name, Title FROM BorrowedBook JOIN Student ON StudentId = Student.Id JOIN Book ON BookId = Book.Id")
    List<BorrowedBook> findAllWithStudentNameAndBookTitle();

    @SqlQuery("SELECT COUNT(*) FROM BorrowedBook")
    long count();

    @SqlUpdate("UPDATE BorrowedBook SET ReturnDate = :returnDate WHERE Id = :id")
    void updateReturnDate(@Bind("id") long id, @Bind("returnDate") String returnDate);

    @SqlUpdate("DELETE FROM BorrowedBook WHERE Id = :id")
    void delete(@Bind("id") long id);

    @SqlUpdate("DELETE FROM BorrowedBook WHERE StudentId IN (SELECT Id FROM Student WHERE Grade LIKE '12_')")
    void deleteWhereStudentGrade12();
}
