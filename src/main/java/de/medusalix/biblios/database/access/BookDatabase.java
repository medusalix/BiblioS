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

import de.medusalix.biblios.database.objects.Book;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Book.class)
public interface BookDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Book(Id IDENTITY PRIMARY KEY, Title VARCHAR(255), Author VARCHAR(255), Isbn BIGINT, Publisher VARCHAR(255), PublishedDate SMALLINT, AdditionalInfo VARCHAR(255))")
    void createTable();

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO Book VALUES (NULL, :title, :author, :isbn, :publisher, :publishedDate, :additionalInfo)")
    long save(@BindBean Book book);
    
    @SqlQuery("SELECT Book.Id, Title, Author, Isbn, Publisher, PublishedDate, AdditionalInfo, StudentId AS BorrowedBy FROM Book LEFT JOIN BorrowedBook ON Book.Id = BookId AND NOT Returned")
    List<Book> findAllWithBorrowedBy();

    @SqlQuery("SELECT COUNT(*) FROM Book")
    long count();

    @SqlUpdate("UPDATE Book SET Title = :title, Author = :author, Isbn = :isbn, Publisher = :publisher, PublishedDate = :publishedDate, AdditionalInfo = :additionalInfo WHERE Id = :id")
    void update(@Bind("id") long id, @BindBean Book book);

    @SqlUpdate("DELETE FROM Book WHERE Id = ?")
    void delete(long id);
}
