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

import de.medusalix.biblios.database.objects.BorrowedBook;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(BorrowedBook.class)
public interface BorrowedBookDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS BorrowedBook(Id IDENTITY PRIMARY KEY, StudentId BIGINT, BookId BIGINT, BorrowDate VARCHAR(255), ReturnDate VARCHAR(255), Returned BOOL, FOREIGN KEY(StudentId) REFERENCES Student(Id), FOREIGN KEY(BookId) REFERENCES Book(Id))")
    void createTable();

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO BorrowedBook VALUES (NULL, :studentId, :bookId, :borrowDate, :returnDate, FALSE)")
    long save(@BindBean BorrowedBook borrowedBook);
    
    @SqlQuery("SELECT ReturnDate, Name AS StudentName, Title AS BookTitle FROM BorrowedBook JOIN Student ON StudentId = Student.Id JOIN Book ON BookId = Book.Id WHERE NOT Returned")
    List<BorrowedBook> findAllComplete();

    @SqlQuery("SELECT BorrowDate, ReturnDate, Name AS StudentName FROM BorrowedBook JOIN Student ON StudentId = Student.Id WHERE BookId = ? AND Returned ORDER BY BorrowedBook.Id DESC")
    List<BorrowedBook> findAllReturnedFromBook(long bookId);

    @SqlQuery("SELECT BorrowedBook.Id, StudentId, BookId, BorrowDate, ReturnDate, Title AS BookTitle FROM BorrowedBook JOIN Book ON BookId = Book.Id WHERE StudentId = ? AND NOT Returned")
    List<BorrowedBook> findAllFromStudent(long studentId);

    @SqlQuery("SELECT COUNT(*) FROM BorrowedBook")
    long count();

    @SqlUpdate("UPDATE BorrowedBook SET ReturnDate = :returnDate WHERE Id = :id")
    void updateReturnDate(@Bind("id") long id, @Bind("returnDate") String returnDate);

    @SqlUpdate("UPDATE BorrowedBook SET Returned = TRUE WHERE Id = ?")
    void updateSetReturned(long id);

    @SqlUpdate("DELETE FROM BorrowedBook AS b WHERE Id IN (SELECT Id FROM BorrowedBook WHERE BookId = b.BookId AND Returned ORDER BY Id DESC OFFSET ?)")
    void deleteOldestReturned(int rowsToKeep);

    @SqlUpdate("DELETE FROM BorrowedBook WHERE StudentId = ?")
    void deleteFromStudent(long studentId);

    @SqlUpdate("DELETE FROM BorrowedBook WHERE StudentId IN (SELECT Id FROM Student WHERE Grade LIKE '12_')")
    void deleteGraduatedStudents();
}
