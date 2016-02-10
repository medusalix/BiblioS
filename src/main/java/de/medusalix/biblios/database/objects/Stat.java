package de.medusalix.biblios.database.objects;

public class Stat
{
    private long bookId;
    private int numberOfBorrows;

    private String bookTitle;

    public Stat() {}

    public Stat(long bookId, int numberOfBorrows)
    {
        this.bookId = bookId;
        this.numberOfBorrows = numberOfBorrows;
    }

    public long getBookId()
    {
        return bookId;
    }

    public int getNumberOfBorrows()
    {
        return numberOfBorrows;
    }

    public String getBookTitle()
    {
        return bookTitle;
    }

    public void setBookId(long bookId)
    {
        this.bookId = bookId;
    }

    public void setNumberOfBorrows(int numberOfBorrows)
    {
        this.numberOfBorrows = numberOfBorrows;
    }

    public void setBookTitle(String bookTitle)
    {
        this.bookTitle = bookTitle;
    }
}
