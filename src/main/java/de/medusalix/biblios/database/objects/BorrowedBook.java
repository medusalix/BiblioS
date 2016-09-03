/*
 * Copyright (C) 2016 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.medusalix.biblios.database.objects;

import de.medusalix.biblios.core.Consts;

import java.time.LocalDate;

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
    
    public boolean isExceeded()
    {
        return LocalDate.now().isAfter(LocalDate.parse(returnDate, Consts.Misc.DATE_FORMATTER));
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
