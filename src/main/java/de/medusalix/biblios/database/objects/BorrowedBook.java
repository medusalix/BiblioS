package de.medusalix.biblios.database.objects;

public class BorrowedBook
{
    private long id;

    private long studentId, bookId;
    private String borrowDate, returnDate;

    private String studentName, bookTitle;

    public BorrowedBook() {}

    public BorrowedBook(long studentId, long bookId, String borrowDate, String returnDate)
    {
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public long getId()
    {
        return id;
    }

    public long getStudentId()
    {
        return studentId;
    }

    public long getBookId()
    {
        return bookId;
    }

    public String getBorrowDate()
    {
        return borrowDate;
    }

    public String getReturnDate()
    {
        return returnDate;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public String getBookTitle()
    {
        return bookTitle;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setStudentId(long studentId)
    {
        this.studentId = studentId;
    }

    public void setBookId(long bookId)
    {
        this.bookId = bookId;
    }

    public void setBorrowDate(String borrowDate)
    {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(String returnDate)
    {
        this.returnDate = returnDate;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public void setBookTitle(String bookTitle)
    {
        this.bookTitle = bookTitle;
    }
}
