package de.medusalix.biblios.pojos;

import de.medusalix.biblios.database.objects.Student;

public class StudentListItem implements Searchable
{
    private long id;

    private String name, grade;

    private boolean hasBorrowedBooks;

    public StudentListItem() {}

    public StudentListItem(Student student, boolean hasBorrowedBooks)
    {
        this.id = student.getId();
        this.name = student.getName();
        this.grade = student.getGrade();
        this.hasBorrowedBooks = hasBorrowedBooks;
    }

    @Override
    public boolean searchFor(String text)
    {
        return getName().toLowerCase().contains(text) || getGrade().toLowerCase().contains(text);
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

    public boolean hasBorrowedBooks()
    {
        return hasBorrowedBooks;
    }
}
