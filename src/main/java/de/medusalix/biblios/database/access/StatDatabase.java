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

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.mappers.StatMapper;
import de.medusalix.biblios.database.objects.Stat;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(StatMapper.class)
public interface StatDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Stat(BookId INT UNIQUE, NumberOfBorrows INT, FOREIGN KEY(BookId) REFERENCES Book(Id))")
    void createTable();

    @SqlUpdate("INSERT INTO Stat VALUES (:bookId, :numberOfBorrows)")
    void save(@BindBean Stat stat);

    @SqlQuery("SELECT BookId, NumberOfBorrows FROM Stat")
    List<Stat> findAll();

    @SqlQuery("SELECT NumberOfBorrows, Title FROM Stat JOIN Book ON BookId = Book.Id ORDER BY NumberOfBorrows DESC LIMIT " + Consts.Misc.MAX_STATS_TO_DISPLAY)
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
