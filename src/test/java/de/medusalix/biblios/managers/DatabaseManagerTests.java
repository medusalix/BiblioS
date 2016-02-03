package de.medusalix.biblios.managers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.pojos.Book;
import de.medusalix.biblios.pojos.Student;
import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.base.ActionQuery;
import de.medusalix.biblios.sql.query.base.ResultQuery;
import de.medusalix.biblios.sql.query.general.InsertQuery;
import de.medusalix.biblios.sql.query.general.SelectQuery;
import de.medusalix.biblios.sql.query.specific.IncrementQuery;
import de.medusalix.biblios.sql.query.specific.InsertOrIgnoreQuery;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DatabaseManagerTests
{
    @BeforeClass
    private void beforeClass()
    {
        DatabaseManager.init();

        Assert.assertTrue(Files.exists(Paths.get(Consts.Resources.DATA_FOLDER_PATH)));
    }

    @AfterClass
    private void afterClass()
    {
        try
        {
            Files.walkFileTree(Paths.get(Consts.Resources.DATA_FOLDER_PATH), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    Files.delete(file);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
                {
                    Files.delete(dir);

                    return FileVisitResult.CONTINUE;
                }
            });
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    private void studentShouldBeAdded()
    {
        try (Connection connection = DatabaseManager.openConnection())
        {
            new InsertQuery(Consts.Database.STUDENTS_TABLE_NAME, "Name", "Grade").execute(connection);

            ResultQuery studentQuery = new SelectQuery(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.ID_COLUMN_NAME, Consts.Database.NAME_COLUMN_NAME, Consts.Database.GRADE_COLUMN_NAME);

            List<Student> students = studentQuery.execute(connection, Student.class);

            Assert.assertTrue(students.size() > 0);

            Student student = students.get(0);

            Assert.assertEquals(student.getName(), "Name");
            Assert.assertEquals(student.getGrade(), "Grade");
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    @Test
    private void bookShouldBeAdded()
    {
        try (Connection connection = DatabaseManager.openConnection())
        {
            new InsertQuery(Consts.Database.BOOKS_TABLE_NAME, "Title", "Author", "123", "Publisher", "2001", "CD").execute(connection);

            ResultQuery bookQuery = new SelectQuery(Consts.Database.BOOKS_TABLE_NAME, Consts.Database.ID_COLUMN_NAME, Consts.Database.TITLE_COLUMN_NAME, Consts.Database.AUTHOR_COLUMN_NAME, Consts.Database.ISBN_COLUMN_NAME, Consts.Database.PUBLISHER_COLUMN_NAME, Consts.Database.PUBLISHED_DATE_COLUMN_NAME, Consts.Database.ADDITIONAL_INFO_COLUMN_NAME);

            List<Book> books = bookQuery.execute(connection, Book.class);

            Assert.assertTrue(books.size() > 0);

            Book book = books.get(0);

            Assert.assertEquals(book.getTitle(), "Title");
            Assert.assertEquals(book.getAuthor(), "Author");
            Assert.assertEquals(book.getIsbn(), "123");
            Assert.assertEquals(book.getPublisher(), "Publisher");
            Assert.assertEquals(book.getPublishedDate(), "2001");
            Assert.assertEquals(book.getAdditionalInfo(), "CD");
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    @Test
    private void backupShouldBeCreated()
    {
        Path backupPath = BackupManager.createBackup("-Test");

        try
        {
            byte[] databaseContents = Files.readAllBytes(Paths.get(Consts.Database.DATABASE_PATH));
            byte[] backupContents = Files.readAllBytes(backupPath);

            Assert.assertTrue(Arrays.equals(databaseContents, backupContents));
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
