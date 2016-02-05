package de.medusalix.biblios.pojos;

import de.medusalix.biblios.dto.Book;

public class BookTableItem implements Searchable
{
    private int id;

    private String title, author, isbn, publisher, publishedDate, additionalInfo, borrowedBy;

    public BookTableItem(Book book)
    {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.publisher = book.getPublisher();
        this.publishedDate = book.getPublishedDate();
        this.additionalInfo = book.getAdditionalInfo();
    }

    @Override
    public boolean searchFor(String text)
    {
        return getTitle().toLowerCase().contains(text) || getAuthor().toLowerCase().contains(text) || getIsbn().toLowerCase().contains(text) || getPublisher().toLowerCase().contains(text) || getPublishedDate().toLowerCase().contains(text) || getAdditionalInfo().toLowerCase().contains(text);
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

    public String getBorrowedBy()
    {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrowedBy)
    {
        this.borrowedBy = borrowedBy;
    }
}
