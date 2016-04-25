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

public class DaoTests
{
    private DBI dbi;

    private Students students;
    private Books books;
    private BorrowedBooks borrowedBooks;
    private Stats stats;

    @BeforeClass
    private void beforeClass()
    {
        dbi = new DBI("jdbc:h2:mem:temp;DATABASE_TO_UPPER=FALSE");

        students = dbi.open(Students.class);
        books = dbi.open(Books.class);
        borrowedBooks = dbi.open(BorrowedBooks.class);
        stats = dbi.open(Stats.class);
    }

    @AfterClass
    private void afterClass()
    {
        dbi.close(students);
        dbi.close(books);
        dbi.close(borrowedBooks);
        dbi.close(stats);
    }

    @Test
    private void tablesShouldBeCreated()
    {
        students.createTable();
        books.createTable();
        borrowedBooks.createTable();
        stats.createTable();
    }

    @Test(dependsOnMethods = "tablesShouldBeCreated")
    private void studentShouldBeSaved()
    {
        Student testStudent = new Student("Name", "Grade");

        students.save(testStudent);

        List<Student> studentResults = students.findAll();

        Assert.assertNotNull(studentResults);
        Assert.assertEquals(studentResults.size(), 1);

        Student foundStudent = studentResults.get(0);

        Assert.assertEquals(foundStudent.getName(), testStudent.getName());
        Assert.assertEquals(foundStudent.getGrade(), testStudent.getGrade());
    }

    @Test(dependsOnMethods = "tablesShouldBeCreated")
    private void bookShouldBeSaved()
    {
        Book testBook = new Book("Title", "Author", 1234567890, "Publisher", (short)2001, "AdditionalInfo");

        books.save(testBook);

        List<Book> bookResults = books.findAll();

        Assert.assertNotNull(bookResults);
        Assert.assertEquals(bookResults.size(), 1);

        Book foundBook = bookResults.get(0);

        Assert.assertEquals(foundBook.getTitle(), testBook.getTitle());
        Assert.assertEquals(foundBook.getAuthor(), testBook.getAuthor());
        Assert.assertEquals(foundBook.getIsbn(), testBook.getIsbn());
        Assert.assertEquals(foundBook.getPublisher(), testBook.getPublisher());
        Assert.assertEquals(foundBook.getPublishedDate(), testBook.getPublishedDate());
        Assert.assertEquals(foundBook.getAdditionalInfo(), testBook.getAdditionalInfo());
    }

    @Test(dependsOnMethods = {"studentShouldBeSaved", "bookShouldBeSaved"})
    private void borrowedBookShouldBeSaved()
    {
        BorrowedBook testBorrowedBook = new BorrowedBook(1, 1, "BorrowDate", "ReturnDate");

        borrowedBooks.save(testBorrowedBook);

        List<BorrowedBook> borrowedBookResults = borrowedBooks.findAll();

        Assert.assertNotNull(borrowedBookResults);
        Assert.assertEquals(borrowedBookResults.size(), 1);

        BorrowedBook foundBorrowedBook = borrowedBookResults.get(0);

        Assert.assertEquals(foundBorrowedBook.getStudentId(), testBorrowedBook.getStudentId());
        Assert.assertEquals(foundBorrowedBook.getBookId(), testBorrowedBook.getBookId());
        Assert.assertEquals(foundBorrowedBook.getBorrowDate(), testBorrowedBook.getBorrowDate());
        Assert.assertEquals(foundBorrowedBook.getReturnDate(), testBorrowedBook.getReturnDate());
    }

    @Test(dependsOnMethods = "bookShouldBeSaved")
    private void statShouldBeSaved()
    {
        Stat testStat = new Stat(1, 1);

        stats.save(testStat);

        List<Stat> statResults = stats.findAll();

        Assert.assertNotNull(statResults);
        Assert.assertEquals(statResults.size(), 1);

        Stat foundStat = statResults.get(0);

        Assert.assertEquals(foundStat.getBookId(), testStat.getBookId());
        Assert.assertEquals(foundStat.getNumberOfBorrows(), testStat.getNumberOfBorrows());
    }

    @Test(dependsOnMethods = "borrowedBookShouldBeSaved", expectedExceptions = DBIException.class)
    private void deletingStudentShouldThrowException()
    {
        students.delete(1);
    }
}
