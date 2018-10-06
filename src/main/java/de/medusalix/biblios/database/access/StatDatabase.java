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

import de.medusalix.biblios.database.objects.Stat;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterFieldMapper(Stat.class)
public interface StatDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Stat(BookId BIGINT UNIQUE, NumberOfBorrows INT, FOREIGN KEY (BookId) REFERENCES Book(Id))")
    void createTable();

    @SqlUpdate("INSERT INTO Stat VALUES (:bookId, :numberOfBorrows)")
    void save(@BindBean Stat stat);

    @SqlQuery("SELECT BookId, NumberOfBorrows FROM Stat")
    List<Stat> findAll();

    @SqlQuery("SELECT NumberOfBorrows, Title FROM Stat JOIN Book ON BookId = Book.Id ORDER BY NumberOfBorrows DESC LIMIT ?")
    List<Stat> findAllWithBookTitle(int limit);

    @SqlQuery("SELECT COUNT(*) FROM Stat WHERE BookId = ?")
    int countFromBook(long bookId);

    @SqlUpdate("UPDATE Stat SET NumberOfBorrows = NumberOfBorrows + 1 WHERE BookId = ?")
    void updateIncrementBorrows(long bookId);
    
    @SqlUpdate("DELETE FROM Stat WHERE BookId = ?")
    void deleteFromBook(long bookId);
    
    @SqlUpdate("DROP TABLE Stat")
    void deleteAll();
}
