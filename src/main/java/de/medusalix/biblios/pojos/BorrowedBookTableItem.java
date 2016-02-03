package de.medusalix.biblios.pojos;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.core.Consts;

import java.time.LocalDate;

public class BorrowedBookTableItem
{
    private int bookId;

    private String title, borrowDate, returnDate;

    public BorrowedBookTableItem() {}

    public BorrowedBookTableItem(int bookId, String title, String borrowDate, String returnDate)
    {
        this.bookId = bookId;
        this.title = title;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int getBookId()
    {
        return bookId;
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
        return LocalDate.now().isAfter(LocalDate.parse(getReturnDate(), Consts.Misc.DATE_FORMATTER));
    }
}
