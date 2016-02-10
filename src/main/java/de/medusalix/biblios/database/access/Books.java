package de.medusalix.biblios.database.access;

import de.medusalix.biblios.database.mappers.BookMapper;
import de.medusalix.biblios.database.objects.Book;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(BookMapper.class)
public interface Books
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Book(Id IDENTITY PRIMARY KEY, Title VARCHAR(255), Author VARCHAR(255), Isbn BIGINT, Publisher VARCHAR(255), PublishedDate SMALLINT, AdditionalInfo VARCHAR(255))")
    void createTable();

    @SqlUpdate("INSERT INTO Book VALUES (NULL, :title, :author, :isbn, :publisher, :publishedDate, :additionalInfo)")
    void save(@BindBean Book book);

    @SqlQuery("SELECT Id, Title, Author, Isbn, Publisher, PublishedDate, AdditionalInfo FROM Book")
    List<Book> findAll();

    @SqlQuery("SELECT COUNT(*) FROM Book")
    long count();

    // The id needs to be bound to bookId so it doesn't collide with the bean
    @SqlUpdate("UPDATE Book SET Title = :title, Author = :author, Isbn = :isbn, Publisher = :publisher, PublishedDate = :publishedDate, AdditionalInfo = :additionalInfo WHERE Id = :bookId")
    void update(@Bind("bookId") long id, @BindBean Book book);

    @SqlUpdate("DELETE FROM Book WHERE Id = :id")
    void delete(@Bind("id") long id);
}
