package de.medusalix.biblios.database.mappers;

import de.medusalix.biblios.database.objects.Book;
import org.skife.jdbi.v2.BeanMapper;

public class BookMapper extends BeanMapper<Book>
{
    public BookMapper()
    {
        super(Book.class);
    }
}
