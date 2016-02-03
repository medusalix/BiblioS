package de.medusalix.biblios.controllers;

import de.medusalix.biblios.controls.BookRow;
import de.medusalix.biblios.controls.BorrowedBookRow;
import de.medusalix.biblios.controls.ExceededCell;
import de.medusalix.biblios.controls.StudentCell;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.pojos.*;
import de.medusalix.biblios.sql.operator.JoinOnOperator;
import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.base.ActionQuery;
import de.medusalix.biblios.sql.query.base.ResultQuery;
import de.medusalix.biblios.sql.query.general.DeleteQuery;
import de.medusalix.biblios.sql.query.general.InsertQuery;
import de.medusalix.biblios.sql.query.general.SelectQuery;
import de.medusalix.biblios.sql.query.specific.IncrementQuery;
import de.medusalix.biblios.sql.query.specific.InsertOrIgnoreQuery;
import de.medusalix.biblios.helpers.ProgressHelper;
import de.medusalix.biblios.helpers.WindowHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.helpers.ProgressHelper;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.base.ResultQuery;
import de.medusalix.biblios.sql.query.general.DeleteQuery;
import de.medusalix.biblios.sql.query.specific.IncrementQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindowController implements UpdatableController
{
    @FXML
    private MenuItem fullscreenMenuItem, reportProblemMenuItem, suggestFeatureMenuItem, aboutMenuItem;

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
    private TableColumn<BookTableItem, String> titleColumn, authorColumn, isbnColumn, publisherColumn, publishedDateColumn, additionalInfoColumn;

    private ObservableList<StudentListItem> originalStudentList = FXCollections.observableArrayList();
    private ObservableList<BookTableItem> originalBookList = FXCollections.observableArrayList();

    @FXML
    private void initialize()
    {
        ProgressHelper.showDialog(Consts.Messages.PROGRESS_LOADING_DATABASE_TITLE);

        initMenuItems();
        initStudentListView();
        initTableViews();
        initFactories();
        initDragAndDrop();
        initSearchFields();

        updateData();

        ProgressHelper.hideDialog();
    }

    private List<HashMap<String, Object>> getBorrowedBooks(Connection connection)
    {
        ResultQuery borrowedBookQuery = new SelectQuery(Consts.Database.BORROWED_BOOKS_TABLE_NAME, Consts.Database.BOOK_ID_COLUMN_NAME, Consts.Database.NAME_COLUMN_NAME);

        borrowedBookQuery.addOperator(new JoinOnOperator(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.STUDENT_ID_COLUMN_NAME, Consts.Database.ID_COLUMN_NAME));

        return borrowedBookQuery.execute(connection);
    }

    private void updateStudents(Connection connection, List<HashMap<String, Object>> borrowedBooks)
    {
        ResultQuery studentQuery = new SelectQuery(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.ID_COLUMN_NAME, Consts.Database.NAME_COLUMN_NAME, Consts.Database.GRADE_COLUMN_NAME);

        List<Student> studentResults = studentQuery.execute(connection, Student.class);

        for (Student student : studentResults)
        {
            int id = student.getId();

            boolean hasBorrowedBooks = borrowedBooks.stream().filter(borrowedBook -> (int)borrowedBook.get(Consts.Database.BOOK_ID_COLUMN_NAME) == id).findFirst().isPresent();

            StudentListItem item = new StudentListItem(student, hasBorrowedBooks);

            originalStudentList.add(item);
        }
    }

    private void updateBooks(Connection connection, List<HashMap<String, Object>> borrowedBooks)
    {
        ResultQuery bookQuery = new SelectQuery(Consts.Database.BOOKS_TABLE_NAME, Consts.Database.ID_COLUMN_NAME, Consts.Database.TITLE_COLUMN_NAME, Consts.Database.AUTHOR_COLUMN_NAME, Consts.Database.ISBN_COLUMN_NAME, Consts.Database.PUBLISHER_COLUMN_NAME, Consts.Database.PUBLISHED_DATE_COLUMN_NAME, Consts.Database.ADDITIONAL_INFO_COLUMN_NAME);

        List<Book> bookResults = bookQuery.execute(connection, Book.class);

        for (Book book : bookResults)
        {
            int id = book.getId();

            HashMap<String, Object> borrowedBook = borrowedBooks.stream().filter(borrowedBook2 -> (int)borrowedBook2.get(Consts.Database.BOOK_ID_COLUMN_NAME) == id).findFirst().orElse(null);

            BookTableItem item = new BookTableItem(book);

            if (borrowedBook != null)
            {
                item.setBorrowedBy(borrowedBook.get(Consts.Database.NAME_COLUMN_NAME).toString());
            }

            originalBookList.add(item);
        }
    }

    @Override
    public void updateData()
    {
        originalStudentList.clear();
        originalBookList.clear();

        try (Connection connection = DatabaseManager.openConnection())
        {
            List<TableColumn<BookTableItem, ?>> bookSortOrder = new ArrayList<>(bookTableView.getSortOrder());

            List<HashMap<String, Object>> borrowedBooks = getBorrowedBooks(connection);

            updateStudents(connection, borrowedBooks);
            updateBooks(connection, borrowedBooks);

            int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();

            reloadStudentSearchItems();
            reloadBookSearchItems();

            studentListView.getSelectionModel().select(selectedIndex);

            onStudentSelected();

            bookTableView.getSortOrder().setAll(bookSortOrder);
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    private void initMenuItems()
    {
        fullscreenMenuItem.setGraphic(new ImageView(Consts.Images.FULLSCREEN_MENU_ITEM_IMAGE));
        reportProblemMenuItem.setGraphic(new ImageView(Consts.Images.REPORT_PROBLEM_MENU_ITEM_IMAGE));
        suggestFeatureMenuItem.setGraphic(new ImageView(Consts.Images.SUGGEST_FEATURE_MENU_ITEM_IMAGE));
        aboutMenuItem.setGraphic(new ImageView(Consts.Images.ABOUT_MENU_ITEM_IMAGE));
    }

    private void initStudentListView()
    {
        studentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::onStudentSelected));
    }

    private void initTableViews()
    {
        Label borrowedBookTableViewLabel = new Label(Consts.Messages.BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER);
        Label bookTableViewLabel = new Label(Consts.Messages.BOOK_TABLE_VIEW_PLACEHOLDER);

        borrowedBookTableViewLabel.setFont(Font.font(16));
        bookTableViewLabel.setFont(Font.font(16));

        borrowedBookTableView.setPlaceholder(borrowedBookTableViewLabel);
        bookTableView.setPlaceholder(bookTableViewLabel);

        bookColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        borrowDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBorrowDate()));
        returnDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getReturnDate()));
        exceededColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().isExceeded()));
        exceededColumn.setGraphic(new ImageView(Consts.Images.EXCEEDED_COLUMN_IMAGE));
        exceededColumn.setCellFactory(param -> new ExceededCell());

        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        authorColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthor()));
        isbnColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getIsbn()));
        publisherColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPublisher()));
        publishedDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPublishedDate()));
        additionalInfoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAdditionalInfo()));
    }

    private void initFactories()
    {
        studentListView.setCellFactory(param -> new StudentCell(this));
        borrowedBookTableView.setRowFactory(param -> new BorrowedBookRow(this));
        bookTableView.setRowFactory(param -> new BookRow(this, studentSearchField, studentListView));
    }

    private void initDragAndDrop()
    {
        borrowedBookTableView.setOnDragOver(event ->
        {
            if (event.getDragboard().hasString() && !(event.getGestureSource() instanceof BorrowedBookRow) && !studentListView.getSelectionModel().isEmpty())
            {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        borrowedBookTableView.setOnDragDropped(event ->
        {
            event.setDropCompleted(true);

            int bookId = Integer.parseInt(event.getDragboard().getString());
            StudentListItem selectedStudent = studentListView.getSelectionModel().getSelectedItem();

            borrowBook(selectedStudent.getId(), bookId);
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

            int bookId = Integer.parseInt(event.getDragboard().getString());

            returnBook(bookId);
        });
    }

    private void initSearchFields()
    {
        studentSearchField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            reloadStudentSearchItems();
        });

        bookSearchField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            reloadBookSearchItems();
        });
    }

    private void onStudentSelected()
    {
        StudentListItem student = studentListView.getSelectionModel().getSelectedItem();

        if (student != null)
        {
            try (Connection connection = DatabaseManager.openConnection())
            {
                if (student.getGrade().isEmpty())
                {
                    studentLabel.setText(student.getName());
                }

                else
                {
                    studentLabel.setText(String.format("%s (%s)", student.getName(), student.getGrade()));
                }

                List<TableColumn<BorrowedBookTableItem, ?>> borrowedBookSortOrder = new ArrayList<>(borrowedBookTableView.getSortOrder());

                ResultQuery borrowedBookQuery = new SelectQuery(Consts.Database.BORROWED_BOOKS_TABLE_NAME, Consts.Database.BOOK_ID_COLUMN_NAME, Consts.Database.TITLE_COLUMN_NAME, Consts.Database.BORROW_DATE_COLUMN_NAME, Consts.Database.RETURN_DATE_COLUMN_NAME);

                borrowedBookQuery.addOperator(new JoinOnOperator(Consts.Database.BOOKS_TABLE_NAME, Consts.Database.BOOK_ID_COLUMN_NAME, Consts.Database.ID_COLUMN_NAME));
                borrowedBookQuery.addOperator(new WhereOperator(Consts.Database.STUDENT_ID_COLUMN_NAME, student.getId()));

                borrowedBookTableView.getItems().setAll(borrowedBookQuery.execute(connection, BorrowedBookTableItem.class));
                borrowedBookTableView.getSortOrder().setAll(borrowedBookSortOrder);
            }

            catch (SQLException e)
            {
                ReportManager.reportException(e);
            }
        }
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
    private void onReportProblemClick()
    {
        String text = Dialogs.showReportProblemDialog();

        if (text != null)
        {
            ReportManager.reportProblem(text);
        }
    }

    @FXML
    private void onSuggestFeatureClick()
    {
        String text = Dialogs.showSuggestFeatureDialog();

        if (text != null)
        {
            ReportManager.suggestFeature(text);
        }
    }

    @FXML
    private void onAboutClick(ActionEvent event)
    {
        Stage stage = WindowHelper.openUtilityWindow(((MenuItem)event.getSource()).getText(), Consts.Resources.ABOUT_WINDOW_PATH);

        if (stage != null)
        {
            stage.setResizable(false);
        }
    }

    @FXML
    private void onAdministrationClick(ActionEvent event)
    {
        String password = Dialogs.showPasswordDialog();

        if (password != null && password.equals(Consts.ADMINISTRATION_PASSWORD))
        {
            WindowHelper.openUtilityWindow(((Button)event.getSource()).getText(), Consts.Resources.ADMINISTRATION_WINDOW_PATH);
        }
    }

    @FXML
    private void onStatsClick(ActionEvent event)
    {
        WindowHelper.openUtilityWindow(((Button)event.getSource()).getText(), Consts.Resources.STATS_WINDOW_PATH);
    }

    @FXML
    private void onBorrowListClick(ActionEvent event)
    {
        WindowHelper.openUtilityWindow(((Button)event.getSource()).getText(), Consts.Resources.BORROW_LIST_WINDOW_PATH);
    }

    @FXML
    private void onAddStudentClick()
    {
        Student student = Dialogs.showStudentDialog(Consts.Messages.ADD_STUDENT_TEXT, Consts.Images.ADD_HEADER_IMAGE, null);

        if (student != null)
        {
            try (Connection connection = DatabaseManager.openConnection())
            {
                new InsertQuery(Consts.Database.STUDENTS_TABLE_NAME, student.getName(), student.getGrade()).execute(connection);

                updateData();
            }

            catch (SQLException e)
            {
                ReportManager.reportException(e);
            }
        }
    }

    @FXML
    private void onAddBookClick()
    {
        Book book = Dialogs.showBookDialog(Consts.Messages.ADD_BOOK_TEXT, Consts.Images.ADD_HEADER_IMAGE, null);

        if (book != null)
        {
            addBook(book);
        }
    }

    @FXML
    private void onAddBookWithIsbnClick()
    {
        String isbn = Dialogs.showScanIsbnDialog();

        if (isbn != null)
        {
            Book book = Dialogs.showBookIsbnDialog(isbn);

            if (book != null)
            {
                addBook(book);
            }
        }
    }

    private void addBook(Book book)
    {
        try (Connection connection = DatabaseManager.openConnection())
        {
            new InsertQuery(Consts.Database.BOOKS_TABLE_NAME, book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublisher(), book.getPublishedDate(), book.getAdditionalInfo()).execute(connection);

            updateData();
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    private void borrowBook(int studentId, int bookId)
    {
        Connection connection = DatabaseManager.openConnection();

        ActionQuery insertBookQuery = new InsertQuery(true, Consts.Database.BORROWED_BOOKS_TABLE_NAME, studentId, bookId, LocalDate.now().format(Consts.Misc.DATE_FORMATTER), LocalDate.now().plusDays(14).format(Consts.Misc.DATE_FORMATTER));
        ActionQuery insertStatQuery = new InsertOrIgnoreQuery(Consts.Database.STATS_TABLE_NAME, bookId, 0);
        ActionQuery incrementStatQuery = new IncrementQuery(Consts.Database.STATS_TABLE_NAME, Consts.Database.NUMBER_OF_BORROWS_COLUMN_NAME);

        incrementStatQuery.addOperator(new WhereOperator(Consts.Database.BOOK_ID_COLUMN_NAME, bookId));

        insertBookQuery.executeBatchAsync(connection, () ->
        {
            try
            {
                if (connection != null)
                {
                    connection.close();

                    Platform.runLater(this::updateData);
                }
            }

            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }, insertStatQuery, incrementStatQuery);
    }

    private void returnBook(int bookId)
    {
        Connection connection = DatabaseManager.openConnection();

        ActionQuery deleteQuery = new DeleteQuery(Consts.Database.BORROWED_BOOKS_TABLE_NAME, Consts.Database.BOOK_ID_COLUMN_NAME, bookId);

        deleteQuery.executeAsync(connection, () ->
        {
            try
            {
                if (connection != null)
                {
                    connection.close();

                    updateData();
                }
            }

            catch (SQLException e)
            {
                e.printStackTrace();
            }
        });
    }
}
