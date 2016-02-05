package de.medusalix.biblios.dao;

import de.medusalix.biblios.dto.Student;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface StudentDao
{
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Students(Id IDENTITY PRIMARY KEY, Name VARCHAR(255) UNIQUE, Grade VARCHAR(255))")
    void createTable();

    @SqlUpdate("DELETE FROM Students WHERE Id = :id")
    void delete(@BindBean Student student);

    @SqlUpdate("INSERT INTO Students VALUES (NULL, :name, :grade)")
    void insert(@BindBean Student student);
}
