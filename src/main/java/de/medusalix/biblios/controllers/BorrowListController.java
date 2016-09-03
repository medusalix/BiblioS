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

import de.medusalix.biblios.controls.BorrowedBookExceededCell;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.objects.BorrowedBook;
import de.medusalix.biblios.database.DatabaseManager;
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

public class BorrowListController
{
    private static final Logger logger = LogManager.getLogger(BorrowListController.class);

    @FXML
    private TableView<BorrowedBook> borrowListTableView;

    @FXML
    private TableColumn<BorrowedBook, String> studentColumn, bookColumn, returnDateColumn;

    @FXML
    private TableColumn<BorrowedBook, Boolean> exceededColumn;

    private BorrowedBookDatabase borrowedBookDatabase = DatabaseManager.createDao(BorrowedBookDatabase.class);

    @FXML
    private void initialize()
    {
        initBorrowListTableView();

        updateData();
    }

    private void updateData()
    {
        borrowListTableView
                .getItems()
                .setAll(borrowedBookDatabase.findAllWithStudentNameAndBookTitle());
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
        exceededColumn.setCellFactory(param -> new BorrowedBookExceededCell());
    }
}
