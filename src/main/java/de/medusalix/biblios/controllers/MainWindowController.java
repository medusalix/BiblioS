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

package de.medusalix.biblios.controllers;

import de.medusalix.biblios.Consts;
import de.medusalix.biblios.controls.BookRow;
import de.medusalix.biblios.controls.BorrowedBookExceededCell;
import de.medusalix.biblios.controls.BorrowedBookRow;
import de.medusalix.biblios.controls.StudentCell;
import de.medusalix.biblios.database.Database;
import de.medusalix.biblios.database.SearchHelper;
import de.medusalix.biblios.database.access.BookDatabase;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.access.StatDatabase;
import de.medusalix.biblios.database.access.StudentDatabase;
import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.database.objects.BorrowedBook;
import de.medusalix.biblios.database.objects.Stat;
import de.medusalix.biblios.database.objects.Student;
import de.medusalix.biblios.dialogs.BookDialog;
import de.medusalix.biblios.dialogs.IsbnDialog;
import de.medusalix.biblios.dialogs.PasswordDialog;
import de.medusalix.biblios.dialogs.StudentDialog;
import de.medusalix.biblios.utils.AlertUtils;
import de.medusalix.biblios.utils.WindowUtils;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;

public class MainWindowController
{
    private static final Logger logger = LogManager.getLogger(MainWindowController.class);

    @FXML
    private MenuItem fullscreenMenuItem, aboutMenuItem;

    @FXML
    private TextField studentSearchField, bookSearchField;

    @FXML
    private Label studentLabel;

    @FXML
    private ListView<Student> studentListView;

    @FXML
    private TableView<BorrowedBook> borrowedBookTableView;

    @FXML
    private TableColumn<BorrowedBook, String> bookColumn, borrowDateColumn, returnDateColumn;

    @FXML
    private TableColumn<BorrowedBook, Boolean> exceededColumn;

    @FXML
    private TableView<Book> bookTableView;

    @FXML
    private TableColumn<Book, String> titleColumn, authorColumn, publisherColumn, additionalInfoColumn;

    @FXML
    private TableColumn<Book, Number> isbnColumn;

    @FXML
    private TableColumn<Book, Number> publishedDateColumn;

    private ObservableList<Student> students;
    private ObservableList<Book> books;

    private FilteredList<Student> filteredStudents;
    private FilteredList<Book> filteredBooks;

    private SearchHelper<Student> studentSearchHelper = new SearchHelper<>();
    private SearchHelper<Book> bookSearchHelper = new SearchHelper<>();

    private StudentDatabase studentDatabase = Database.get(StudentDatabase.class);
    private BookDatabase bookDatabase = Database.get(BookDatabase.class);
    private BorrowedBookDatabase borrowedBookDatabase = Database.get(BorrowedBookDatabase.class);
    private StatDatabase statDatabase = Database.get(StatDatabase.class);

    @FXML
    private void initialize()
    {
        initMenuItems();
        initStudentListView();
        initTableViews();
        initDragAndDrop();

        loadData();
        initSearchFields();
    }

    public void changeStudent(Student student)
    {
        new StudentDialog(student)
            .showAndWait()
            .ifPresent(newStudent ->
        {
            studentDatabase.update(student.getId(), newStudent);

            updateStudentLabel();

            logger.info("Changed " + newStudent);
        });
    }

    public void deleteStudent(Student student)
    {
        AlertUtils.showConfirmation(
            Consts.Dialogs.DELETE_STUDENT_TITLE,
            Consts.Dialogs.DELETE_STUDENT_MESSAGE,
            () ->
            {
                borrowedBookDatabase.deleteFromStudent(student.getId());
                studentDatabase.delete(student.getId());

                students.remove(student);
                updateStudentLabel();

                logger.info("Deleted " + student);
            }
        );
    }

    public void changeBook(Book book)
    {
        new BookDialog(book)
            .showAndWait()
            .ifPresent(newBook ->
            {
                bookDatabase.update(book.getId(), newBook);

                logger.info("Changed " + newBook);
            });
    }

    public void deleteBook(Book book)
    {
        AlertUtils.showConfirmation(
            Consts.Dialogs.DELETE_BOOK_TITLE,
            Consts.Dialogs.DELETE_BOOK_MESSAGE,
            () ->
            {
                long id = book.getId();

                statDatabase.deleteFromBook(id);
                bookDatabase.delete(id);

                books.remove(book);

                logger.info("Deleted " + book);
            }
        );
    }

    public void selectBorrowedStudent(Book book)
    {
        studentSearchField.clear();

        long borrowedBy = book.getBorrowedBy();

        students.stream()
            .filter(student -> borrowedBy == student.getId())
            .findFirst()
            .ifPresent(student ->
            {
                studentListView.getSelectionModel().select(student);
                studentListView.scrollTo(student);
            });
    }

    public void showHistory(Book book)
    {
        try
        {
            String title = String.format(Consts.Strings.HISTORY_WINDOW_TITLE, book.getTitle());
            HistoryController controller = new HistoryController(book);

            WindowUtils.openWindow(title, Consts.Paths.HISTORY_WINDOW, controller);
        }

        catch (IOException e)
        {
            logger.error("", e);
        }
    }

    public void extendBorrowedBook(BorrowedBook borrowedBook)
    {
        String returnDate = LocalDate.parse(borrowedBook.getReturnDate(), Consts.Misc.DATE_FORMATTER)
            .plusDays(Consts.Misc.DAYS_TO_EXTEND)
            .format(Consts.Misc.DATE_FORMATTER);

        borrowedBookDatabase.updateReturnDate(borrowedBook.getId(), returnDate);
        borrowedBook.setReturnDate(returnDate);

        logger.info("Extended " + borrowedBook);
    }

    private void initMenuItems()
    {
        fullscreenMenuItem.setGraphic(new ImageView(Consts.Images.FULLSCREEN_MENU_ITEM));
        aboutMenuItem.setGraphic(new ImageView(Consts.Images.ABOUT_MENU_ITEM));
    }

    private void initStudentListView()
    {
        studentListView.setCellFactory(param -> new StudentCell(this));
        studentListView.setPlaceholder(new Label(Consts.Strings.STUDENT_LIST_VIEW_PLACEHOLDER));
        studentListView.getSelectionModel()
            .selectedItemProperty()
            .addListener((observable, oldValue, student) ->
            {
                if (student == null)
                {
                    return;
                }

                updateStudentLabel();

                borrowedBookTableView.getItems()
                    .setAll(borrowedBookDatabase.findAllFromStudent(student.getId()));

                logger.debug("Selected " + student);
            });
    }

    private void initTableViews()
    {
        borrowedBookTableView.setPlaceholder(new Label(Consts.Strings.BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER));
        bookTableView.setPlaceholder(new Label(Consts.Strings.BOOK_TABLE_VIEW_PLACEHOLDER));
    
        borrowedBookTableView.setRowFactory(param -> new BorrowedBookRow(this));

        bookColumn.setCellValueFactory(param -> param.getValue().bookTitleProperty());
        borrowDateColumn.setCellValueFactory(param -> param.getValue().borrowDateProperty());
        returnDateColumn.setCellValueFactory(param -> param.getValue().returnDateProperty());
        exceededColumn.setCellValueFactory(param -> param.getValue().exceededProperty());
        exceededColumn.setGraphic(new ImageView(Consts.Images.EXCEEDED_COLUMN));
        exceededColumn.setCellFactory(param -> new BorrowedBookExceededCell());
    
        bookTableView.setRowFactory(param -> new BookRow(this));

        titleColumn.setCellValueFactory(param -> param.getValue().titleProperty());
        authorColumn.setCellValueFactory(param -> param.getValue().authorProperty());
        isbnColumn.setCellValueFactory(param -> param.getValue().isbnProperty());
        publisherColumn.setCellValueFactory(param -> param.getValue().publisherProperty());
        publishedDateColumn.setCellValueFactory(param -> param.getValue().publishedDateProperty());
        additionalInfoColumn.setCellValueFactory(param -> param.getValue().additionalInfoProperty());
        
        titleColumn.setResizable(false);
        titleColumn.prefWidthProperty().bind(
            bookTableView.widthProperty()
                .subtract(authorColumn.widthProperty())
                .subtract(isbnColumn.widthProperty())
                .subtract(publisherColumn.widthProperty())
                .subtract(publishedDateColumn.widthProperty())
                .subtract(additionalInfoColumn.widthProperty())
        );

        borrowedBookTableView.setItems(FXCollections.observableArrayList(
            borrowedBook -> new Observable[] {
                borrowedBook.bookTitleProperty(),
                borrowedBook.borrowDateProperty(),
                borrowedBook.returnDateProperty(),
                borrowedBook.exceededProperty()
            }
        ));
    }

    private void initDragAndDrop()
    {
        borrowedBookTableView.setOnDragOver(event ->
        {
            if (!studentListView.getSelectionModel().isEmpty()
                && !(event.getGestureSource() instanceof BorrowedBookRow))
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        borrowedBookTableView.setOnDragDropped(event ->
        {
            event.setDropCompleted(true);

            Student student = studentListView.getSelectionModel().getSelectedItem();
            Book book = bookTableView.getSelectionModel().getSelectedItem();

            borrowBook(student, book);
        });

        bookTableView.setOnDragOver(event ->
        {
            if (!(event.getGestureSource() instanceof BookRow))
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        bookTableView.setOnDragDropped(event ->
        {
            event.setDropCompleted(true);

            BorrowedBook borrowedBook = borrowedBookTableView.getSelectionModel().getSelectedItem();
            
            returnBook(borrowedBook);
        });
    }

    private void initSearchFields()
    {
        students.addListener((ListChangeListener<Student>)c ->
            studentSearchHelper.setObjects(students)
        );

        books.addListener((ListChangeListener<Book>)c ->
            bookSearchHelper.setObjects(books)
        );

        studentSearchField.textProperty().addListener((observable, oldValue, newValue) ->
            filteredStudents.setPredicate(student -> studentSearchHelper.matches(student, newValue))
        );

        bookSearchField.textProperty().addListener((observable, oldValue, newValue) ->
            filteredBooks.setPredicate(book -> bookSearchHelper.matches(book, newValue))
        );
    }

    private void loadData()
    {
        logger.info("Loading data");

        students = FXCollections.observableList(
            studentDatabase.findAllWithBorrows(),
            student -> new Observable[] {
                student.nameProperty(),
                student.gradeProperty(),
                student.hasBorrowsProperty()
            }
        );
        books = FXCollections.observableList(
            bookDatabase.findAllWithBorrowedBy(),
            book -> new Observable[] {
                book.titleProperty(),
                book.authorProperty(),
                book.isbnProperty(),
                book.publisherProperty(),
                book.publishedDateProperty(),
                book.additionalInfoProperty(),
                book.borrowedByProperty()
            }
        );

        studentSearchHelper.setObjects(students);
        bookSearchHelper.setObjects(books);

        filteredStudents = new FilteredList<>(students);
        filteredBooks = new FilteredList<>(books);

        studentListView.setItems(filteredStudents);
        bookTableView.setItems(filteredBooks);
    }

    private void updateStudentLabel()
    {
        Student student = studentListView.getSelectionModel().getSelectedItem();

        if (student == null)
        {
            studentLabel.setText("");
        }

        else if (student.getGrade() == null)
        {
            studentLabel.setText(student.getName());
        }

        else
        {
            studentLabel.setText(student.getName() + " (" + student.getGrade() + ")");
        }
    }

    private void borrowBook(Student student, Book book)
    {
        long studentId = student.getId();
        long bookId = book.getId();

        LocalDate currentDate = LocalDate.now();

        String borrowDate = currentDate.format(Consts.Misc.DATE_FORMATTER);
        String returnDate = currentDate.plusDays(14).format(Consts.Misc.DATE_FORMATTER);

        BorrowedBook borrowedBook = new BorrowedBook(
            studentId,
            bookId,
            borrowDate,
            returnDate
        );

        long id = borrowedBookDatabase.save(borrowedBook);

        // Set generated id
        borrowedBook.setId(id);

        if (statDatabase.countFromBook(bookId) == 0)
        {
            statDatabase.save(new Stat(bookId, 1));
        }

        else
        {
            statDatabase.updateIncrementBorrows(bookId);
        }

        // Update view
        student.setHasBorrows(true);
        book.setBorrowedBy(studentId);
        borrowedBook.setBookTitle(book.getTitle());

        books.set(books.indexOf(book), book);
        bookTableView.getSelectionModel().clearSelection();

        borrowedBookTableView.getItems().add(borrowedBook);

        logger.info("Borrowed " + book);
    }

    private void returnBook(BorrowedBook borrowedBook)
    {
        borrowedBookDatabase.updateSetReturned(borrowedBook.getId());

        books.stream()
            .filter(book -> book.getId() == borrowedBook.getBookId())
            .findFirst()
            .ifPresent(book ->
            {
                // Update view
                borrowedBookTableView.getItems().remove(borrowedBook);

                if (borrowedBookTableView.getItems().isEmpty())
                {
                    // Make student deletable
                    studentListView.getSelectionModel()
                        .getSelectedItem()
                        .setHasBorrows(false);
                }

                book.setBorrowedBy(0);

                books.set(books.indexOf(book), book);
                bookTableView.getSelectionModel().clearSelection();

                logger.info("Returned book " + book);
            });
    }
    
    @FXML
    private void onFullscreenClick()
    {
        // Get scene from any node
        Stage stage = (Stage)studentSearchField.getScene().getWindow();

        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void onAboutClick(ActionEvent event)
    {
        String text = ((MenuItem)event.getSource()).getText();

        try
        {
            WindowUtils.openWindow(text, Consts.Paths.ABOUT_WINDOW);
        }
        
        catch (IOException e)
        {
            logger.error("", e);
        }
    }

    @FXML
    private void onAdministrationClick(ActionEvent event)
    {
        new PasswordDialog().showAndWait()
            .filter(password -> password.equals(Consts.ADMINISTRATION_PASSWORD))
            .ifPresent(password ->
            {
                String text = ((Labeled)event.getSource()).getText();

                try
                {
                    WindowUtils.openWindow(text, Consts.Paths.ADMINISTRATION_WINDOW);
                }

                catch (IOException e)
                {
                    logger.error("", e);
                }
            });
    }

    @FXML
    private void onStatsClick(ActionEvent event)
    {
        String text = ((Labeled)event.getSource()).getText();

        try
        {
            WindowUtils.openWindow(text, Consts.Paths.STATS_WINDOW);
        }
        
        catch (IOException e)
        {
            logger.error("", e);
        }
    }

    @FXML
    private void onBorrowListClick(ActionEvent event)
    {
        String text = ((Labeled)event.getSource()).getText();

        try
        {
            WindowUtils.openWindow(text, Consts.Paths.BORROW_LIST_WINDOW);
        }
        
        catch (IOException e)
        {
            logger.error("", e);
        }
    }

    @FXML
    private void onAddStudentClick()
    {
        new StudentDialog()
            .showAndWait()
            .ifPresent(student ->
        {
            long id = studentDatabase.save(student);

            // Set generated id
            student.setId(id);

            students.add(student);

            logger.info("Added " + student);
        });
    }

    @FXML
    private void onAddBookClick()
    {
        new BookDialog()
            .showAndWait()
            .ifPresent(book ->
        {
            long id = bookDatabase.save(book);

            // Set generated id
            book.setId(id);

            books.add(book);
    
            logger.info("Added " + book);
        });
    }

    @FXML
    private void onAddBookByIsbnClick()
    {
        new IsbnDialog().showAndWait().ifPresent(pair ->
            new BookDialog(pair.getKey(), pair.getValue())
                .showAndWait()
                .ifPresent(book ->
                {
                    long id = bookDatabase.save(book);

                    // Set generated id
                    book.setId(id);

                    books.add(book);

                    logger.info("Added " + book);
                })
        );
    }
}
