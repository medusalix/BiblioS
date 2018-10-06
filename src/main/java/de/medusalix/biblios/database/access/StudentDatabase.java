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

import de.medusalix.biblios.database.objects.Student;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Student.class)
public interface StudentDatabase
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Student(Id IDENTITY PRIMARY KEY, Name VARCHAR(255) UNIQUE, Grade VARCHAR(255))")
    void createTable();

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO Student VALUES (NULL, :name, :grade)")
    long save(@BindBean Student student);

    @SqlQuery("SELECT Id, Name, Grade, (SELECT CASEWHEN(COUNT(*) > 0, TRUE, FALSE) FROM BorrowedBook WHERE StudentId = Student.Id AND NOT Returned) AS HasBorrows FROM Student")
    List<Student> findAllWithBorrows();

    @SqlQuery("SELECT COUNT(*) FROM Student")
    long count();

    @SqlUpdate("UPDATE Student SET Name = :name, Grade = :grade WHERE Id = :id")
    void update(@Bind("id") long id, @BindBean Student student);
    
    @SqlUpdate("UPDATE Student SET Grade = Grade + 1 WHERE Grade REGEXP '^\\d*$'")
    void updateIncrementGradeSimple();
    
    @SqlUpdate("UPDATE Student SET Grade = CONCAT(LEFT(Grade, LENGTH(Grade) - 1) + 1, RIGHT(Grade, 1)) WHERE Grade NOT REGEXP '^\\d*$'")
    void updateIncrementGradeComplex();

    @SqlUpdate("DELETE FROM Student WHERE Id = ?")
    void delete(long id);

    @SqlUpdate("DELETE FROM Student WHERE Grade = '12' OR Grade LIKE '12_'")
    void deleteWhereGrade12();
}
