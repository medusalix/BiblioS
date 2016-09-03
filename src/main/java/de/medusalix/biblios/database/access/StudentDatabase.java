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

import de.medusalix.biblios.database.mappers.StudentMapper;
import de.medusalix.biblios.database.objects.Student;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(StudentMapper.class)
public interface StudentDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Student(Id IDENTITY PRIMARY KEY, Name VARCHAR(255) UNIQUE, Grade VARCHAR(255))")
    void createTable();

    @SqlUpdate("INSERT INTO Student VALUES (NULL, :name, :grade)")
    void save(@BindBean Student student);

    @SqlQuery("SELECT Id, Name, Grade, (SELECT CASEWHEN(COUNT(*) > 0, TRUE, FALSE) FROM BorrowedBook WHERE StudentId = Student.Id) AS HasBorrowedBooks FROM Student")
    List<Student> findAllWithHasBorrowedBooks();

    @SqlQuery("SELECT COUNT(*) FROM Student")
    long count();

    // The id needs to be bound to studentId so it doesn't collide with the bean
    @SqlUpdate("UPDATE Student SET Name = :name, Grade = :grade WHERE Id = :studentId")
    void update(@Bind("studentId") long id, @BindBean Student student);
    
    @SqlUpdate("UPDATE Student SET Grade = Grade + 1 WHERE Grade REGEXP '^\\d*$'")
    void updateIncrementGradeSimple();
    
    @SqlUpdate("UPDATE Student SET Grade = CONCAT(LEFT(Grade, LENGTH(Grade) - 1) + 1, RIGHT(Grade, 1)) WHERE Grade NOT REGEXP '^\\d*$'")
    void updateIncrementGradeComplex();

    @SqlUpdate("DELETE FROM Student WHERE Id = :id")
    void delete(@Bind("id") long id);

    @SqlUpdate("DELETE FROM Student WHERE Grade = '12' OR Grade LIKE '12_'")
    void deleteWhereGrade12();
}
