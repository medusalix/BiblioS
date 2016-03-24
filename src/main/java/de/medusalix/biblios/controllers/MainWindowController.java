package de.medusalix.biblios.controllers;

import de.medusalix.biblios.controls.BookRow;
import de.medusalix.biblios.controls.BorrowedBookRow;
import de.medusalix.biblios.controls.ExceededCell;
import de.medusalix.biblios.controls.StudentCell;
import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.database.access.Books;
import de.medusalix.biblios.database.access.BorrowedBooks;
import de.medusalix.biblios.database.access.Stats;
import de.medusalix.biblios.database.access.Students;
import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.database.objects.BorrowedBook;
import de.medusalix.biblios.database.objects.Stat;
import de.medusalix.biblios.database.objects.Student;
import de.medusalix.biblios.helpers.Windows;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.helpers.Exceptions;
import de.medusalix.biblios.pojos.BookTableItem;
import de.medusalix.biblios.pojos.BorrowedBookTableItem;
import de.medusalix.biblios.pojos.StudentListItem;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindowController implements UpdatableController
{
    private Logger logger = LogManager.getLogger(MainWindowController.class);

    @FXML
    private MenuItem fullscreenMenuItem, aboutMenuItem;

    @FXML
    private TextField studentSearchField, bookSearchField;

    @FXML
    private Label studentLabel;

    @FXML
    private ListView<StudentListItem> studentListView;

    @FXML
    private TableView<BorrowedBookTableItem> borrowedBookTableView;

    @FXML
    private TableColumn<BorrowedBookTableItem, String> bookColumn, borrowDateColumn, returnDateColumn;

    @FXML
    private TableColumn<BorrowedBookTableItem, Boolean> exceededColumn;

    @FXML
    private TableView<BookTableItem> bookTableView;

    @FXML
    private TableColumn<BookTableItem, String> titleColumn, authorColumn, publisherColumn, additionalInfoColumn;

    @FXML
    private TableColumn<BookTableItem, Long> isbnColumn;

    @FXML
    private TableColumn<BookTableItem, Short> publishedDateColumn;

    private ObservableList<StudentListItem> originalStudentList = FXCollections.observableArrayList();
    private ObservableList<BookTableItem> originalBookList = FXCollections.observableArrayList();

    private Students students = DatabaseManager.createDao(Students.class);
    private Books books = DatabaseManager.createDao(Books.class);
    private BorrowedBooks borrowedBooks = DatabaseManager.createDao(BorrowedBooks.class);
    private Stats stats = DatabaseManager.createDao(Stats.class);

    @FXML
    private void initialize()
    {
        initMenuItems();
        initStudentListView();
        initTableViews();
        initFactories();
        initDragAndDrop();
        initSearchFields();

        updateData();
    }

    private List<BorrowedBook> getBorrowedBooks()
    {
        try
        {
            return borrowedBooks.findAllWithStudentName();
        }

        catch (DBIException e)
        {
            Exceptions.log(e);
        }

        return null;
    }

    private void updateStudents(List<BorrowedBook> borrowedBooks)
    {
        try
        {
            List<Student> studentResults = students.findAll();

            for (Student student : studentResults)
            {
                long id = student.getId();

                boolean hasBorrowedBooks = borrowedBooks.stream().filter(borrowedBook -> borrowedBook.getBookId() == id).findFirst().isPresent();

                StudentListItem item = new StudentListItem(student, hasBorrowedBooks);

                originalStudentList.add(item);
            }
        }

        catch (DBIException e)
        {
            Exceptions.log(e);
        }
    }

    private void updateBooks(List<BorrowedBook> borrowedBooks)
    {
        try
        {
            List<Book> bookResults = books.findAll();

            for (Book book : bookResults)
            {
                BorrowedBook borrowedBook = borrowedBooks.stream().filter(borrowedBook2 -> borrowedBook2.getBookId() == book.getId()).findFirst().orElse(null);

                BookTableItem item = new BookTableItem(book);

                if (borrowedBook != null)
                {
                    item.setBorrowedBy(borrowedBook.getStudentName());
                }

                originalBookList.add(item);
            }
        }

        catch (DBIException e)
        {
            Exceptions.log(e);
        }
    }

    @Override
    public void updateData()
    {
        originalStudentList.clear();
        originalBookList.clear();

        List<TableColumn<BookTableItem, ?>> bookSortOrder = new ArrayList<>(bookTableView.getSortOrder());

        List<BorrowedBook> borrowedBooks = getBorrowedBooks();

        if (borrowedBooks != null)
        {
            updateStudents(borrowedBooks);
            updateBooks(borrowedBooks);

            int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();

            reloadStudentSearchItems();
            reloadBookSearchItems();

            studentListView.getSelectionModel().select(selectedIndex);

            onStudentSelected();

            bookTableView.getSortOrder().setAll(bookSortOrder);

            logger.info("Data updated");
        }
    }

    private void initMenuItems()
    {
        fullscreenMenuItem.setGraphic(new ImageView(Reference.Images.FULLSCREEN_MENU_ITEM));
        aboutMenuItem.setGraphic(new ImageView(Reference.Images.ABOUT_MENU_ITEM));
    }

    private void initStudentListView()
    {
        Label studentListViewLabel = new Label(Reference.Strings.STUDENT_LIST_VIEW_PLACEHOLDER);

        studentListViewLabel.setFont(Font.font(16));

        studentListView.setPlaceholder(studentListViewLabel);
        studentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::onStudentSelected));
    }

    private void initTableViews()
    {
        Label borrowedBookTableViewLabel = new Label(Reference.Strings.BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER);
        Label bookTableViewLabel = new Label(Reference.Strings.BOOK_TABLE_VIEW_PLACEHOLDER);

        borrowedBookTableViewLabel.setFont(Font.font(16));
        bookTableViewLabel.setFont(Font.font(16));

        borrowedBookTableView.setPlaceholder(borrowedBookTableViewLabel);
        bookTableView.setPlaceholder(bookTableViewLabel);

        bookColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        borrowDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBorrowDate()));
        returnDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getReturnDate()));
        exceededColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().isExceeded()));
        exceededColumn.setGraphic(new ImageView(Reference.Images.EXCEEDED_COLUMN));
        exceededColumn.setCellFactory(param -> new ExceededCell());

        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        authorColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthor()));
        isbnColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getIsbn()));
        publisherColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPublisher()));
        publishedDateColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPublishedDate()));
        additionalInfoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAdditionalInfo()));
    }

    private void initFactories()
    {
        studentListView.setCellFactory(param -> new StudentCell(this, students));
        borrowedBookTableView.setRowFactory(param -> new BorrowedBookRow(this, borrowedBooks));
        bookTableView.setRowFactory(param -> new BookRow(this, books, stats, studentSearchField, studentListView));
    }

    private void initDragAndDrop()
    {
        borrowedBookTableView.setOnDragOver(event ->
        {
            if (event.getDragboard().hasString() && !studentListView.getSelectionModel().isEmpty() && !(event.getGestureSource() instanceof BorrowedBookRow))
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        borrowedBookTableView.setOnDragDropped(event ->
        {
            event.setDropCompleted(true);

            long studentId = studentListView.getSelectionModel().getSelectedItem().getId();
            long bookId = Long.parseLong(event.getDragboard().getString());

            LocalDate currentDate = LocalDate.now();

            String borrowDate = currentDate.format(Reference.Misc.DATE_FORMATTER);
            String returnDate = currentDate.plusDays(14).format(Reference.Misc.DATE_FORMATTER);

            try
            {
                borrowedBooks.save(new BorrowedBook(studentId, bookId, borrowDate, returnDate));

                if (stats.countByBookId(bookId) == 0)
                {
                    stats.save(new Stat(bookId, 1));
                }

                else
                {
                    stats.updateIncrementBorrows(bookId);
                }

                logger.info("Book borrowed");

                updateData();
            }

            catch (DBIException e)
            {
                Exceptions.log(e);
            }
        });

        bookTableView.setOnDragOver(event ->
        {
            if (event.getDragboard().hasString() && !(event.getGestureSource() instanceof BookRow))
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        bookTableView.setOnDragDropped(event ->
        {
            event.setDropCompleted(true);

            long id = Long.parseLong(event.getDragboard().getString());

            try
            {
                borrowedBooks.delete(id);

                logger.info("Book returned");

                updateData();
            }

            catch (DBIException e)
            {
                Exceptions.log(e);
            }
        });
    }

    private void initSearchFields()
    {
        studentSearchField.textProperty().addListener((observable, oldValue, newValue) -> reloadStudentSearchItems());
        bookSearchField.textProperty().addListener((observable, oldValue, newValue) -> reloadBookSearchItems());
    }

    private void onStudentSelected()
    {
        StudentListItem student = studentListView.getSelectionModel().getSelectedItem();

        if (student != null)
        {
            if (student.getGrade() == null)
            {
                studentLabel.setText(student.getName());
            }

            else
            {
                studentLabel.setText(String.format("%s (%s)", student.getName(), student.getGrade()));
            }

            List<TableColumn<BorrowedBookTableItem, ?>> borrowedBookSortOrder = new ArrayList<>(borrowedBookTableView.getSortOrder());

            try
            {
                List<BorrowedBook> studentBooks = borrowedBooks.findAllWithBookTitleFromStudentId(student.getId());

                borrowedBookTableView.getItems().setAll(studentBooks.stream().map(BorrowedBookTableItem::new).collect(Collectors.toList()));
                borrowedBookTableView.getSortOrder().setAll(borrowedBookSortOrder);
            }

            catch (DBIException e)
            {
                Exceptions.log(e);
            }
        }

        logger.debug("Student selected");
    }

    private void reloadStudentSearchItems()
    {
        String text = studentSearchField.getText().toLowerCase();

        studentListView.getItems().setAll(originalStudentList.stream().filter(student -> student.searchFor(text)).collect(Collectors.toList()));
    }

    private void reloadBookSearchItems()
    {
        String text = bookSearchField.getText().toLowerCase();

        bookTableView.getItems().setAll(originalBookList.stream().filter(book -> book.searchFor(text)).collect(Collectors.toList()));
    }

    @FXML
    private void onFullscreenClick()
    {
        Stage stage = (Stage)studentSearchField.getScene().getWindow();

        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void onAboutClick(ActionEvent event)
    {
        Windows.openWindow(((MenuItem)event.getSource()).getText(), Reference.Paths.ABOUT_WINDOW);
    }

    @FXML
    private void onAdministrationClick(ActionEvent event)
    {
        String password = Dialogs.showPasswordDialog();

        if (password != null && password.equals(Reference.ADMINISTRATION_PASSWORD))
        {
            Windows.openWindow(((Button)event.getSource()).getText(), Reference.Paths.ADMINISTRATION_WINDOW);
        }
    }

    @FXML
    private void onStatsClick(ActionEvent event)
    {
        Windows.openWindow(((Button)event.getSource()).getText(), Reference.Paths.STATS_WINDOW);
    }

    @FXML
    private void onBorrowListClick(ActionEvent event)
    {
        Windows.openWindow(((Button)event.getSource()).getText(), Reference.Paths.BORROW_LIST_WINDOW);
    }

    @FXML
    private void onAddStudentClick()
    {
        Student student = Dialogs.showStudentDialog(Reference.Dialogs.ADD_STUDENT_TEXT, Reference.Images.ADD_DIALOG_HEADER, null);

        if (student != null)
        {
            try
            {
                students.save(student);

                logger.info("Student added");

                updateData();
            }

            catch (DBIException e)
            {
                Exceptions.log(e);
            }
        }
    }

    @FXML
    private void onAddBookClick()
    {
        Book book = Dialogs.showBookDialog(Reference.Dialogs.ADD_BOOK_TEXT, Reference.Images.ADD_DIALOG_HEADER, null);

        if (book != null)
        {
            try
            {
                books.save(book);

                logger.info("Book added");

                updateData();
            }

            catch (DBIException e)
            {
                Exceptions.log(e);
            }
        }
    }

    @FXML
    private void onAddBookByIsbnClick()
    {
        String isbn = Dialogs.showScanIsbnDialog();

        if (isbn != null)
        {
            Book book = Dialogs.showBookIsbnDialog(isbn);

            if (book != null)
            {
                try
                {
                    books.save(book);

                    logger.info("Book added by ISBN");

                    updateData();
                }

                catch (DBIException e)
                {
                    Exceptions.log(e);
                }
            }
        }
    }
}
