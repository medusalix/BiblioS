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
import de.medusalix.biblios.controls.BorrowedBookExceededCell;
import de.medusalix.biblios.database.Database;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.objects.BorrowedBook;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

public class BorrowListController
{
    @FXML
    private TableView<BorrowedBook> borrowListTableView;

    @FXML
    private TableColumn<BorrowedBook, String> studentColumn, bookColumn, returnDateColumn;

    @FXML
    private TableColumn<BorrowedBook, Boolean> exceededColumn;

    private BorrowedBookDatabase borrowedBookDatabase = Database.get(BorrowedBookDatabase.class);

    @FXML
    private void initialize()
    {
        borrowListTableView.setPlaceholder(new Label(Consts.Strings.BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER));

        studentColumn.setCellValueFactory(param -> param.getValue().studentNameProperty());
        bookColumn.setCellValueFactory(param -> param.getValue().bookTitleProperty());
        returnDateColumn.setCellValueFactory(param -> param.getValue().returnDateProperty());
        exceededColumn.setCellValueFactory(param -> param.getValue().exceededProperty());
        exceededColumn.setGraphic(new ImageView(Consts.Images.EXCEEDED_COLUMN));
        exceededColumn.setCellFactory(param -> new BorrowedBookExceededCell());

        borrowListTableView.getItems()
            .setAll(borrowedBookDatabase.findAllComplete());
    }
}
