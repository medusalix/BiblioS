package de.medusalix.biblios.pojos;

import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.database.objects.BorrowedBook;

import java.time.LocalDate;

public class BorrowedBookTableItem
{
    private long id;

    private String title, borrowDate, returnDate;

    public BorrowedBookTableItem(BorrowedBook borrowedBook)
    {
        id = borrowedBook.getId();
        title = borrowedBook.getBookTitle();
        borrowDate = borrowedBook.getBorrowDate();
        returnDate = borrowedBook.getReturnDate();
    }

    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getBorrowDate()
    {
        return borrowDate;
    }

    public String getReturnDate()
    {
        return returnDate;
    }

    public boolean isExceeded()
    {
        return LocalDate.now().isAfter(LocalDate.parse(getReturnDate(), Reference.Misc.DATE_FORMATTER));
    }
}
