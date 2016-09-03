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

package de.medusalix.biblios.database.access;

import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.database.objects.BorrowedBook;
import de.medusalix.biblios.database.objects.Stat;
import de.medusalix.biblios.database.objects.Student;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class DatabaseTests
{
    private DBI dbi;

    private StudentDatabase studentDatabase;
    private BookDatabase bookDatabase;
    private BorrowedBookDatabase borrowedBookDatabase;
    private StatDatabase statDatabase;
    
    private Student testStudent = new Student("Max Mustermann", "7a");
    private Book testBook = new Book("Java ist auch eine Insel", "Christian Ullenboom", 3836228734L, "Galileo Computing", (short)2014, null);
    private BorrowedBook testBorrowedBook = new BorrowedBook(1, 1, "01.04.1985", "11.08.2016");
    Stat testStat = new Stat(1, 7);

    @BeforeClass
    private void beforeClass()
    {
        dbi = new DBI("jdbc:h2:mem:temp;DATABASE_TO_UPPER=FALSE");

        studentDatabase = dbi.open(StudentDatabase.class);
        bookDatabase = dbi.open(BookDatabase.class);
        borrowedBookDatabase = dbi.open(BorrowedBookDatabase.class);
        statDatabase = dbi.open(StatDatabase.class);
    }

    @AfterClass
    private void afterClass()
    {
        dbi.close(studentDatabase);
        dbi.close(bookDatabase);
        dbi.close(borrowedBookDatabase);
        dbi.close(statDatabase);
    }

    @Test
    private void tablesShouldBeCreated()
    {
        studentDatabase.createTable();
        bookDatabase.createTable();
        borrowedBookDatabase.createTable();
        statDatabase.createTable();
    }

    @Test(dependsOnMethods = "tablesShouldBeCreated")
    private void studentShouldBeSaved()
    {
        studentDatabase.save(testStudent);

        List<Student> studentResults = studentDatabase.findAllWithHasBorrowedBooks();

        Assert.assertNotNull(studentResults);
        Assert.assertEquals(studentResults.size(), 1);

        Student student = studentResults.get(0);

        Assert.assertEquals(student.getId(), 1);
        Assert.assertEquals(student.getName(), testStudent.getName());
        Assert.assertEquals(student.getGrade(), testStudent.getGrade());
    }

    @Test(dependsOnMethods = "tablesShouldBeCreated")
    private void bookShouldBeSaved()
    {
        bookDatabase.save(testBook);

        List<Book> bookResults = bookDatabase.findAllWithBorrowedBy();

        Assert.assertNotNull(bookResults);
        Assert.assertEquals(bookResults.size(), 1);

        Book book = bookResults.get(0);

        Assert.assertEquals(book.getId(), 1);
        Assert.assertEquals(book.getTitle(), testBook.getTitle());
        Assert.assertEquals(book.getAuthor(), testBook.getAuthor());
        Assert.assertEquals(book.getIsbn(), testBook.getIsbn());
        Assert.assertEquals(book.getPublisher(), testBook.getPublisher());
        Assert.assertEquals(book.getPublishedDate(), testBook.getPublishedDate());
        Assert.assertEquals(book.getAdditionalInfo(), testBook.getAdditionalInfo());
        
        Assert.assertEquals(book.getBorrowedBy(), testStudent.getId());
    }

    @Test(dependsOnMethods = { "studentShouldBeSaved", "bookShouldBeSaved" })
    private void borrowedBookShouldBeSaved()
    {
        borrowedBookDatabase.save(testBorrowedBook);
        
        List<BorrowedBook> borrowedBookResults = borrowedBookDatabase.findAllWithBookTitleFromStudentId(1);

        Assert.assertNotNull(borrowedBookResults);
        Assert.assertEquals(borrowedBookResults.size(), 1);

        BorrowedBook borrowedBook = borrowedBookResults.get(0);

        Assert.assertEquals(borrowedBook.getId(), 1);
        Assert.assertEquals(borrowedBook.getBorrowDate(), testBorrowedBook.getBorrowDate());
        Assert.assertEquals(borrowedBook.getReturnDate(), testBorrowedBook.getReturnDate());
        Assert.assertEquals(borrowedBook.getBookTitle(), testBook.getTitle());
    }

    @Test(dependsOnMethods = "bookShouldBeSaved")
    private void statShouldBeSaved()
    {
        statDatabase.save(testStat);

        List<Stat> statResults = statDatabase.findAll();

        Assert.assertNotNull(statResults);
        Assert.assertEquals(statResults.size(), 1);

        Stat stat = statResults.get(0);

        Assert.assertEquals(stat.getBookId(), testStat.getBookId());
        Assert.assertEquals(stat.getNumberOfBorrows(), testStat.getNumberOfBorrows());
    }

    @Test(dependsOnMethods = "borrowedBookShouldBeSaved", expectedExceptions = DBIException.class)
    private void deletingStudentShouldThrowException()
    {
        studentDatabase.delete(1);
    }
    
    @Test(dependsOnMethods = "borrowedBookShouldBeSaved", expectedExceptions = DBIException.class)
    private void deletingBookShouldThrowException()
    {
        bookDatabase.delete(1);
    }
}
