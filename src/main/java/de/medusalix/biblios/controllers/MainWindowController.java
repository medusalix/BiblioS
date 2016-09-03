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

import de.medusalix.biblios.controls.BookRow;
import de.medusalix.biblios.controls.BorrowedBookExceededCell;
import de.medusalix.biblios.controls.BorrowedBookRow;
import de.medusalix.biblios.controls.StudentCell;
import de.medusalix.biblios.core.Consts;
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
import de.medusalix.biblios.database.DatabaseManager;
import de.medusalix.biblios.utils.WindowUtils;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainWindowController implements UpdatableController
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
    private TableColumn<Book, Long> isbnColumn;

    @FXML
    private TableColumn<Book, Short> publishedDateColumn;

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    private StudentDatabase studentDatabase = DatabaseManager.createDao(StudentDatabase.class);
    private BookDatabase bookDatabase = DatabaseManager.createDao(BookDatabase.class);
    private BorrowedBookDatabase borrowedBookDatabase = DatabaseManager.createDao(BorrowedBookDatabase.class);
    private StatDatabase statDatabase = DatabaseManager.createDao(StatDatabase.class);

    @FXML
    private void initialize()
    {
        initMenuItems();
        initStudentListView();
        initTableViews();
        initDragAndDrop();
        initSearchFields();

        update();
    }

    @Override
    public void update()
    {
        logger.info("Updating data");
        
        studentLabel.setText(null);

        List<TableColumn<Book, ?>> bookSortOrder = new ArrayList<>(bookTableView.getSortOrder());
    
        students.setAll(studentDatabase.findAllWithHasBorrowedBooks());
        books.setAll(bookDatabase.findAllWithBorrowedBy());
        
        // Save the selected index before we update the list view
        int selectedStudentIndex = studentListView.getSelectionModel().getSelectedIndex();
        
        updateStudentSearchItems();
        updateBookSearchItems();
        
        studentListView.getSelectionModel().select(selectedStudentIndex);

        bookTableView.getSortOrder().setAll(bookSortOrder);
    }

    private void initMenuItems()
    {
        fullscreenMenuItem.setGraphic(new ImageView(Consts.Images.FULLSCREEN_MENU_ITEM));
        aboutMenuItem.setGraphic(new ImageView(Consts.Images.ABOUT_MENU_ITEM));
    }

    private void initStudentListView()
    {
        Label studentListViewLabel = new Label(Consts.Strings.STUDENT_LIST_VIEW_PLACEHOLDER);

        studentListViewLabel.setFont(Font.font(16));
    
        studentListView.setCellFactory(param -> new StudentCell(this, studentDatabase));
        studentListView.setPlaceholder(studentListViewLabel);
        studentListView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, student) ->
                {
                    if (student == null)
                    {
                        return;
                    }
    
                    if (student.getGrade() == null)
                    {
                        studentLabel.setText(student.getName());
                    }
    
                    else
                    {
                        studentLabel.setText(student.getName() + " (" + student.getGrade() + ")");
                    }
    
                    List<TableColumn<BorrowedBook, ?>> borrowedBookSortOrder = new ArrayList<>(borrowedBookTableView.getSortOrder());
                    
                    List<BorrowedBook> studentBooks = borrowedBookDatabase.findAllWithBookTitleFromStudentId(student.getId());
    
                    borrowedBookTableView.getItems().setAll(studentBooks);
                    borrowedBookTableView.getSortOrder().setAll(borrowedBookSortOrder);
    
                    logger.debug("Selected " + student);
                });
    }

    private void initTableViews()
    {
        Label borrowedBookTableViewLabel = new Label(Consts.Strings.BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER);
        Label bookTableViewLabel = new Label(Consts.Strings.BOOK_TABLE_VIEW_PLACEHOLDER);

        borrowedBookTableViewLabel.setFont(Font.font(16));
        bookTableViewLabel.setFont(Font.font(16));

        borrowedBookTableView.setPlaceholder(borrowedBookTableViewLabel);
        bookTableView.setPlaceholder(bookTableViewLabel);
    
        borrowedBookTableView.setRowFactory(param -> new BorrowedBookRow(this, borrowedBookDatabase));
        
        bookColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBookTitle()));
        borrowDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBorrowDate()));
        returnDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getReturnDate()));
        exceededColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().isExceeded()));
        exceededColumn.setGraphic(new ImageView(Consts.Images.EXCEEDED_COLUMN));
        exceededColumn.setCellFactory(param -> new BorrowedBookExceededCell());
    
        bookTableView.setRowFactory(param -> new BookRow(this, bookDatabase, statDatabase, studentSearchField, studentListView));
        
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        authorColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthor()));
        isbnColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getIsbn()));
        publisherColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPublisher()));
        publishedDateColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPublishedDate()));
        additionalInfoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAdditionalInfo()));
    }

    private void initDragAndDrop()
    {
        borrowedBookTableView.setOnDragOver(event ->
        {
            if (event.getDragboard().hasString()
                    && !studentListView.getSelectionModel().isEmpty()
                    && !(event.getGestureSource() instanceof BorrowedBookRow))
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

            String borrowDate = currentDate.format(Consts.Misc.DATE_FORMATTER);
            String returnDate = currentDate.plusDays(14).format(Consts.Misc.DATE_FORMATTER);
            
            borrowedBookDatabase.save(new BorrowedBook(studentId, bookId, borrowDate, returnDate));

            if (statDatabase.countByBookId(bookId) == 0)
            {
                statDatabase.save(new Stat(bookId, 1));
            }

            else
            {
                statDatabase.updateIncrementBorrows(bookId);
            }

            logger.info("Borrowed book with ID " + bookId);

            update();
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
            
            borrowedBookDatabase.delete(id);

            logger.info("Returned book with ID " + id);

            update();
        });
    }

    private void initSearchFields()
    {
        studentSearchField.textProperty().addListener((observable, oldValue, newValue) -> updateStudentSearchItems());
        bookSearchField.textProperty().addListener((observable, oldValue, newValue) -> updateBookSearchItems());
    }
    
    private void updateStudentSearchItems()
    {
        String text = studentSearchField.getText().toLowerCase();
    
        studentListView.getItems().clear();
    
        students
                .stream()
                .filter(student -> student.contains(text))
                .forEach(student -> studentListView.getItems().add(student));
    }

    private void updateBookSearchItems()
    {
        String text = bookSearchField.getText().toLowerCase();

        bookTableView.getItems().clear();
        
        books
                .stream()
                .filter(book -> book.contains(text))
                .forEach(book -> bookTableView.getItems().add(book));
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
        try
        {
            WindowUtils.openWindow(((MenuItem)event.getSource()).getText(), Consts.Paths.ABOUT_WINDOW);
        }
        
        catch (IOException e)
        {
            logger.error("", e);
        }
    }

    @FXML
    private void onAdministrationClick(ActionEvent event)
    {
        new PasswordDialog()
                .showAndWait()
                .filter(password -> password.equals(Consts.ADMINISTRATION_PASSWORD))
                .ifPresent(password ->
                {
                    try
                    {
                        WindowUtils.openWindow(((Button)event.getSource()).getText(), Consts.Paths.ADMINISTRATION_WINDOW);
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
        try
        {
            WindowUtils.openWindow(((Button)event.getSource()).getText(), Consts.Paths.STATS_WINDOW);
        }
        
        catch (IOException e)
        {
            logger.error("", e);
        }
    }

    @FXML
    private void onBorrowListClick(ActionEvent event)
    {
        try
        {
            WindowUtils.openWindow(((Button)event.getSource()).getText(), Consts.Paths.BORROW_LIST_WINDOW);
        }
        
        catch (IOException e)
        {
            logger.error("", e);
        }
    }

    @FXML
    private void onAddStudentClick()
    {
        new StudentDialog().showAndWait().ifPresent(student ->
        {
            studentDatabase.save(student);

            logger.info("Added " + student);

            update();
        });
    }

    @FXML
    private void onAddBookClick()
    {
        new BookDialog().showAndWait().ifPresent(book ->
        {
            bookDatabase.save(book);
    
            logger.info("Added " + book);
    
            update();
        });
    }

    @FXML
    private void onAddBookByIsbnClick()
    {
        new IsbnDialog().showAndWait().ifPresent(
                pair -> new BookDialog(pair.getKey(), pair.getValue()).showAndWait().ifPresent(
                        book ->
                        {
                            bookDatabase.save(book);
                
                            logger.info("Added " + book);
                
                            update();
                        }
                )
        );
    }
}
