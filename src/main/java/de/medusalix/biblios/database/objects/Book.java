package de.medusalix.biblios.database.objects;

public class Book
{
    private long id;

    private String title, author, publisher, additionalInfo;
    private long isbn;
    private short publishedDate;

    public Book() {}

    public Book(String title, String author, long isbn, String publisher, short publishedDate, String additionalInfo)
    {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.additionalInfo = additionalInfo;
    }

    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public long getIsbn()
    {
        return isbn;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public short getPublishedDate()
    {
        return publishedDate;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setIsbn(long isbn)
    {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public void setPublishedDate(short publishedDate)
    {
        this.publishedDate = publishedDate;
    }

    public void setAdditionalInfo(String additionalInfo)
    {
        this.additionalInfo = additionalInfo;
    }
}
