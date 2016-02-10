package de.medusalix.biblios.database.objects;

public class Student
{
    private long id;

    private String name, grade;

    public Student() {}

    public Student(String name, String grade)
    {
        this.name = name;
        this.grade = grade;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }
}
