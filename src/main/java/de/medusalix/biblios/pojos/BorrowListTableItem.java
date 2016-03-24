package de.medusalix.biblios.pojos;

import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.database.objects.BorrowedBook;

import java.time.LocalDate;

public class BorrowListTableItem
{
    private String studentName, bookTitle, returnDate;

    public BorrowListTableItem(BorrowedBook borrowedBook)
    {
        studentName = borrowedBook.getStudentName();
        bookTitle = borrowedBook.getBookTitle();
        returnDate = borrowedBook.getReturnDate();
    }

    public String getStudentName()
    {
        return studentName;
    }

    public String getBookTitle()
    {
        return bookTitle;
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
