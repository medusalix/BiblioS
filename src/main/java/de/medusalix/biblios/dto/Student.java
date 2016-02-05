package de.medusalix.biblios.dto;

public class Student
{
    private int id;

    private String name, grade;

    public Student() {}

    public Student(int id, String name, String grade)
    {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public Student(String name, String grade)
    {
        this(0, name, grade);
    }

    public int getId()
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
}
