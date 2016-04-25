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
public interface Students
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Student(Id IDENTITY PRIMARY KEY, Name VARCHAR(255) UNIQUE, Grade VARCHAR(255))")
    void createTable();

    @SqlUpdate("INSERT INTO Student VALUES (NULL, :name, :grade)")
    void save(@BindBean Student student);

    @SqlQuery("SELECT Id, Name, Grade FROM Student")
    List<Student> findAll();

    @SqlQuery("SELECT COUNT(*) FROM Student")
    long count();

    // The id needs to be bound to studentId so it doesn't collide with the bean
    @SqlUpdate("UPDATE Student SET Name = :name, Grade = :grade WHERE Id = :studentId")
    void update(@Bind("studentId") long id, @BindBean Student student);

    @SqlUpdate("UPDATE Student SET Grade = CONCAT(SUBSTR(Grade, 0, LENGTH(Grade) - 1) + 1, SUBSTR(Grade, LENGTH(Grade))) WHERE Grade != ''")
    void updateIncrementGrade();

    @SqlUpdate("DELETE FROM Student WHERE Id = :id")
    void delete(@Bind("id") long id);

    @SqlUpdate("DELETE FROM Student WHERE Grade LIKE '12_'")
    void deleteWhereGrade12();
}
