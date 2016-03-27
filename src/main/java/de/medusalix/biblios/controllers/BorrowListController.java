package de.medusalix.biblios.controllers;

import de.medusalix.biblios.controls.BorrowListExceededCell;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.BorrowedBooks;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.utils.ExceptionUtils;
import de.medusalix.biblios.pojos.BorrowListTableItem;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

import java.util.stream.Collectors;

public class BorrowListController
{
    private Logger logger = LogManager.getLogger(BorrowListController.class);

    @FXML
    private TableView<BorrowListTableItem> borrowListTableView;

    @FXML
    private TableColumn<BorrowListTableItem, String> studentColumn, bookColumn, returnDateColumn;

    @FXML
    private TableColumn<BorrowListTableItem, Boolean> exceededColumn;

    private BorrowedBooks borrowedBooks = DatabaseManager.createDao(BorrowedBooks.class);

    @FXML
    private void initialize()
    {
        initBorrowListTableView();

        updateData();
    }

    private void updateData()
    {
        try
        {
            borrowListTableView.getItems().setAll(borrowedBooks.findAllWithStudentNameAndBookTitle()
                    .stream()
                    .map(BorrowListTableItem::new)
                    .collect(Collectors.toList()));
        }

        catch (DBIException e)
        {
            ExceptionUtils.log(e);
        }
    }

    private void initBorrowListTableView()
    {
        Label borrowListTableViewLabel = new Label(Consts.Strings.BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER);

        borrowListTableViewLabel.setFont(Font.font(16));

        borrowListTableView.setPlaceholder(borrowListTableViewLabel);

        studentColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStudentName()));
        bookColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBookTitle()));
        returnDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getReturnDate()));
        exceededColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().isExceeded()));
        exceededColumn.setGraphic(new ImageView(Consts.Images.EXCEEDED_COLUMN));
        exceededColumn.setCellFactory(param -> new BorrowListExceededCell());
    }
}
