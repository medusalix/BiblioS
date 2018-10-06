/*
 * Copyright (C) 2018 Medusalix
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
import de.medusalix.biblios.database.Database;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.database.objects.BorrowedBook;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HistoryController
{
    @FXML
    private TableView<BorrowedBook> historyTableView;

    @FXML
    private TableColumn<BorrowedBook, String> studentColumn, borrowDateColumn, returnDateColumn;

    private Book book;

    private BorrowedBookDatabase borrowedBookDatabase = Database.get(BorrowedBookDatabase.class);

    public HistoryController(Book book)
    {
        this.book = book;
    }

    @FXML
    private void initialize()
    {
        historyTableView.setPlaceholder(new Label(Consts.Strings.HISTORY_TABLE_VIEW_PLACEHOLDER));

        studentColumn.setCellValueFactory(param -> param.getValue().studentNameProperty());
        borrowDateColumn.setCellValueFactory(param -> param.getValue().borrowDateProperty());
        returnDateColumn.setCellValueFactory(param -> param.getValue().returnDateProperty());

        historyTableView.getItems()
            .setAll(borrowedBookDatabase.findAllReturnedFromBook(book.getId()));
    }
}
