package de.medusalix.biblios.controllers;

import de.medusalix.biblios.controls.BorrowListExceededCell;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.pojos.BorrowListTableItem;
import de.medusalix.biblios.sql.operator.JoinOnOperator;
import de.medusalix.biblios.sql.query.base.ResultQuery;
import de.medusalix.biblios.sql.query.general.SelectQuery;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class BorrowListController
{
    @FXML
    private TableView<BorrowListTableItem> borrowListTableView;

    @FXML
    private TableColumn<BorrowListTableItem, String> studentColumn, bookColumn, returnDateColumn;

    @FXML
    private TableColumn<BorrowListTableItem, Boolean> exceededColumn;

    @FXML
    private void initialize()
    {
        initBorrowListTableView();

        updateData();
    }

    private void updateData()
    {
        borrowListTableView.getItems().clear();

        try (Connection connection = DatabaseManager.openConnection())
        {
            ResultQuery borrowedBookQuery = new SelectQuery(Consts.Database.BORROWED_BOOKS_TABLE_NAME, Consts.Database.NAME_COLUMN_NAME, Consts.Database.TITLE_COLUMN_NAME, Consts.Database.RETURN_DATE_COLUMN_NAME);

            borrowedBookQuery.addOperator(new JoinOnOperator(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.STUDENT_ID_COLUMN_NAME, Consts.Database.ID_COLUMN_NAME));
            borrowedBookQuery.addOperator(new JoinOnOperator(Consts.Database.BOOKS_TABLE_NAME, Consts.Database.BOOK_ID_COLUMN_NAME, Consts.Database.ID_COLUMN_NAME));

            List<HashMap<String, Object>> borrowedBooks = borrowedBookQuery.execute(connection);

            for (HashMap<String, Object> borrowedBook : borrowedBooks)
            {
                borrowListTableView.getItems().add(new BorrowListTableItem(borrowedBook.get(Consts.Database.NAME_COLUMN_NAME).toString(), borrowedBook.get(Consts.Database.TITLE_COLUMN_NAME).toString(), borrowedBook.get(Consts.Database.RETURN_DATE_COLUMN_NAME).toString()));
            }
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }
    }

    private void initBorrowListTableView()
    {
        studentColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStudent()));
        bookColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBook()));
        returnDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getReturnDate()));
        exceededColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().isExceeded()));
        exceededColumn.setGraphic(new ImageView(Consts.Images.EXCEEDED_COLUMN_IMAGE));
        exceededColumn.setCellFactory(param -> new BorrowListExceededCell());
    }
}
