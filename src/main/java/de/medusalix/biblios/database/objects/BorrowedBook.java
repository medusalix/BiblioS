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

import de.medusalix.biblios.Consts;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class BorrowedBook
{
    private long id;

    private long studentId, bookId;

    private StringProperty bookTitleProperty = new SimpleStringProperty();
    private StringProperty studentNameProperty = new SimpleStringProperty();

    private StringProperty borrowDateProperty = new SimpleStringProperty();
    private StringProperty returnDateProperty = new SimpleStringProperty();

    private BooleanProperty exceededProperty = new SimpleBooleanProperty();

    public BorrowedBook()
    {
        returnDateProperty.addListener(listener ->
        {
            LocalDate date = LocalDate.parse(returnDateProperty.get(), Consts.Misc.DATE_FORMATTER);

            exceededProperty.set(LocalDate.now().isAfter(date));
        });
    }

    public BorrowedBook(long studentId, long bookId, String borrowDate, String returnDate)
    {
        this();

        this.studentId = studentId;
        this.bookId = bookId;

        borrowDateProperty.set(borrowDate);
        returnDateProperty.set(returnDate);
    }

    @Override
    public String toString()
    {
        return "BorrowedBook{" +
            "id=" + id +
            ", studentId=" + studentId +
            ", bookId=" + bookId +
            ", borrowDate='" + borrowDateProperty.get() + '\'' +
            ", returnDate='" + returnDateProperty.get() + '\'' +
            ", studentName='" + studentNameProperty.get() + '\'' +
            ", bookTitle='" + bookTitleProperty.get() + '\'' +
            '}';
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

    public String getBookTitle()
    {
        return bookTitleProperty.get();
    }

    public String getStudentName()
    {
        return studentNameProperty.get();
    }

    public String getBorrowDate()
    {
        return borrowDateProperty.get();
    }

    public String getReturnDate()
    {
        return returnDateProperty.get();
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

    public void setBookTitle(String bookTitle)
    {
        bookTitleProperty.set(bookTitle);
    }

    public void setStudentName(String studentName)
    {
        studentNameProperty.set(studentName);
    }

    public void setBorrowDate(String borrowDate)
    {
        borrowDateProperty.set(borrowDate);
    }

    public void setReturnDate(String returnDate)
    {
        returnDateProperty.set(returnDate);
    }

    public StringProperty bookTitleProperty()
    {
        return bookTitleProperty;
    }

    public StringProperty studentNameProperty()
    {
        return studentNameProperty;
    }

    public StringProperty borrowDateProperty()
    {
        return borrowDateProperty;
    }

    public StringProperty returnDateProperty()
    {
        return returnDateProperty;
    }

    public BooleanProperty exceededProperty()
    {
        return exceededProperty;
    }
}
