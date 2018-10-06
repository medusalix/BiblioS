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
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class DatabaseTests
{
    private static final Student student = new Student("Max Mustermann", "7a");
    private static final Book book = new Book(
        "Java ist auch eine Insel",
        "Christian Ullenboom",
        3836228734L,
        "Galileo Computing",
        2014,
        null
    );
    private static final BorrowedBook borrowedBook = new BorrowedBook(1, 1, "01.04.1985", "11.08.2016");
    private static final Stat stat = new Stat(1, 7);

    private Handle handle;

    private StudentDatabase studentDatabase;
    private BookDatabase bookDatabase;
    private BorrowedBookDatabase borrowedBookDatabase;
    private StatDatabase statDatabase;

    @BeforeClass
    private void beforeClass()
    {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:temp;DATABASE_TO_UPPER=FALSE");

        jdbi.installPlugin(new SqlObjectPlugin());

        handle = jdbi.open();

        studentDatabase = handle.attach(StudentDatabase.class);
        bookDatabase = handle.attach(BookDatabase.class);
        borrowedBookDatabase = handle.attach(BorrowedBookDatabase.class);
        statDatabase = handle.attach(StatDatabase.class);
    }

    @AfterClass
    private void afterClass()
    {
        handle.close();
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
        studentDatabase.save(student);

        List<Student> studentResults = studentDatabase.findAllWithBorrows();

        Assert.assertNotNull(studentResults);
        Assert.assertEquals(studentResults.size(), 1);

        Student student = studentResults.get(0);

        Assert.assertEquals(student.getId(), 1);
        Assert.assertEquals(student.getName(), DatabaseTests.student.getName());
        Assert.assertEquals(student.getGrade(), DatabaseTests.student.getGrade());
    }

    @Test(dependsOnMethods = "tablesShouldBeCreated")
    private void bookShouldBeSaved()
    {
        bookDatabase.save(book);

        List<Book> bookResults = bookDatabase.findAllWithBorrowedBy();

        Assert.assertNotNull(bookResults);
        Assert.assertEquals(bookResults.size(), 1);

        Book book = bookResults.get(0);

        Assert.assertEquals(book.getId(), 1);
        Assert.assertEquals(book.getTitle(), DatabaseTests.book.getTitle());
        Assert.assertEquals(book.getAuthor(), DatabaseTests.book.getAuthor());
        Assert.assertEquals(book.getIsbn(), DatabaseTests.book.getIsbn());
        Assert.assertEquals(book.getPublisher(), DatabaseTests.book.getPublisher());
        Assert.assertEquals(book.getPublishedDate(), DatabaseTests.book.getPublishedDate());
        Assert.assertEquals(book.getAdditionalInfo(), DatabaseTests.book.getAdditionalInfo());
        
        Assert.assertEquals(book.getBorrowedBy(), student.getId());
    }

    @Test(dependsOnMethods = { "studentShouldBeSaved", "bookShouldBeSaved" })
    private void borrowedBookShouldBeSaved()
    {
        borrowedBookDatabase.save(borrowedBook);
        
        List<BorrowedBook> borrowedBookResults = borrowedBookDatabase.findAllFromStudent(1);

        Assert.assertNotNull(borrowedBookResults);
        Assert.assertEquals(borrowedBookResults.size(), 1);

        BorrowedBook borrowedBook = borrowedBookResults.get(0);

        Assert.assertEquals(borrowedBook.getId(), 1);
        Assert.assertEquals(borrowedBook.getBorrowDate(), DatabaseTests.borrowedBook.getBorrowDate());
        Assert.assertEquals(borrowedBook.getReturnDate(), DatabaseTests.borrowedBook.getReturnDate());
        Assert.assertEquals(borrowedBook.getBookTitle(), book.getTitle());
    }

    @Test(dependsOnMethods = "bookShouldBeSaved")
    private void statShouldBeSaved()
    {
        statDatabase.save(stat);

        List<Stat> statResults = statDatabase.findAll();

        Assert.assertNotNull(statResults);
        Assert.assertEquals(statResults.size(), 1);

        Stat stat = statResults.get(0);

        Assert.assertEquals(stat.getBookId(), DatabaseTests.stat.getBookId());
        Assert.assertEquals(stat.getNumberOfBorrows(), DatabaseTests.stat.getNumberOfBorrows());
    }

    @Test(dependsOnMethods = "borrowedBookShouldBeSaved", expectedExceptions = JdbiException.class)
    private void deletingStudentShouldThrowException()
    {
        studentDatabase.delete(1);
    }
    
    @Test(dependsOnMethods = "borrowedBookShouldBeSaved", expectedExceptions = JdbiException.class)
    private void deletingBookShouldThrowException()
    {
        bookDatabase.delete(1);
    }
}
