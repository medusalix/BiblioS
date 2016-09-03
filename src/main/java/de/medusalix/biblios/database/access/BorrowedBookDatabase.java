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

import de.medusalix.biblios.database.mappers.BorrowedBookMapper;
import de.medusalix.biblios.database.objects.BorrowedBook;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(BorrowedBookMapper.class)
public interface BorrowedBookDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS BorrowedBook(Id IDENTITY PRIMARY KEY, StudentId INT, BookId INT UNIQUE, BorrowDate VARCHAR(255), ReturnDate VARCHAR(255), FOREIGN KEY(StudentId) REFERENCES Student(Id), FOREIGN KEY(BookId) REFERENCES Book(Id))")
    void createTable();

    @SqlUpdate("INSERT INTO BorrowedBook VALUES (NULL, :studentId, :bookId, :borrowDate, :returnDate)")
    void save(@BindBean BorrowedBook borrowedBook);
    
    @SqlQuery("SELECT ReturnDate, Name AS StudentName, Title AS BookTitle FROM BorrowedBook JOIN Student ON StudentId = Student.Id JOIN Book ON BookId = Book.Id")
    List<BorrowedBook> findAllWithStudentNameAndBookTitle();

    @SqlQuery("SELECT BorrowedBook.Id, BorrowDate, ReturnDate, Title AS BookTitle FROM BorrowedBook JOIN Book ON BookId = Book.Id WHERE StudentId = :studentId")
    List<BorrowedBook> findAllWithBookTitleFromStudentId(@Bind("studentId") long studentId);

    @SqlQuery("SELECT COUNT(*) FROM BorrowedBook")
    long count();

    @SqlUpdate("UPDATE BorrowedBook SET ReturnDate = :returnDate WHERE Id = :id")
    void updateReturnDate(@Bind("id") long id, @Bind("returnDate") String returnDate);

    @SqlUpdate("DELETE FROM BorrowedBook WHERE Id = :id")
    void delete(@Bind("id") long id);

    @SqlUpdate("DELETE FROM BorrowedBook WHERE StudentId IN (SELECT Id FROM Student WHERE Grade LIKE '12_')")
    void deleteWhereStudentGrade12();
}
