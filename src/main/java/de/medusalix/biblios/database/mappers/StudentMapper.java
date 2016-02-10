package de.medusalix.biblios.database.mappers;

import de.medusalix.biblios.database.objects.Student;
import org.skife.jdbi.v2.BeanMapper;

public class StudentMapper extends BeanMapper<Student>
{
    public StudentMapper()
    {
        super(Student.class);
    }
}
