package de.medusalix.biblios.pojos;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.core.Consts;

import java.time.LocalDate;

public class BorrowListTableItem
{
    private String student, book, returnDate;

    public BorrowListTableItem(String student, String book, String returnDate)
    {
        this.student = student;
        this.book = book;
        this.returnDate = returnDate;
    }

    public String getStudent()
    {
        return student;
    }

    public String getBook()
    {
        return book;
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
