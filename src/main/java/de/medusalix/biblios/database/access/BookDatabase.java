/*
 * Copyright (C) 2016 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
public interface BookDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Book(Id IDENTITY PRIMARY KEY, Title VARCHAR(255), Author VARCHAR(255), Isbn BIGINT, Publisher VARCHAR(255), PublishedDate SMALLINT, AdditionalInfo VARCHAR(255))")
    void createTable();

    @SqlUpdate("INSERT INTO Book VALUES (NULL, :title, :author, :isbn, :publisher, :publishedDate, :additionalInfo)")
    void save(@BindBean Book book);
    
    @SqlQuery("SELECT Book.Id, Title, Author, Isbn, Publisher, PublishedDate, AdditionalInfo, StudentId AS BorrowedBy FROM Book LEFT JOIN BorrowedBook ON Book.Id = BookId")
    List<Book> findAllWithBorrowedBy();

    @SqlQuery("SELECT COUNT(*) FROM Book")
    long count();

    // The id needs to be bound to bookId so it doesn't collide with the bean
    @SqlUpdate("UPDATE Book SET Title = :title, Author = :author, Isbn = :isbn, Publisher = :publisher, PublishedDate = :publishedDate, AdditionalInfo = :additionalInfo WHERE Id = :bookId")
    void update(@Bind("bookId") long id, @BindBean Book book);

    @SqlUpdate("DELETE FROM Book WHERE Id = :id")
    void delete(@Bind("id") long id);
}
