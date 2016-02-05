package de.medusalix.biblios.dto;

public class Book
{
    private int id;

    private String title, author, isbn, publisher, publishedDate, additionalInfo;

    public Book() {}

    public Book(int id, String title, String author, String isbn, String publisher, String publishedDate, String additionalInfo)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.additionalInfo = additionalInfo;
    }

    public Book(String title, String author, String isbn, String publisher, String publishedDate, String additionalInfo)
    {
        this(0, title, author, isbn, publisher, publishedDate, additionalInfo);
    }

    public int getId()
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

    public String getIsbn()
    {
        return isbn;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public String getPublishedDate()
    {
        return publishedDate;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
    }
}
