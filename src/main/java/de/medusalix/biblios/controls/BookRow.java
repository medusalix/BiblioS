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

package de.medusalix.biblios.controls;

import de.medusalix.biblios.Consts;
import de.medusalix.biblios.controllers.MainWindowController;
import de.medusalix.biblios.database.objects.Book;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class BookRow extends TableRow<Book>
{
    private MenuItem changeBookItem = new MenuItem(
        Consts.Strings.CHANGE_MENU_ITEM_TEXT,
        new ImageView(Consts.Images.CHANGE_MENU_ITEM)
    );
    private MenuItem deleteBookItem = new MenuItem(
        Consts.Strings.DELETE_MENU_ITEM_TEXT,
        new ImageView(Consts.Images.DELETE_MENU_ITEM)
    );
    private MenuItem showStudentItem = new MenuItem(
        Consts.Strings.SHOW_STUDENT_MENU_ITEM_TEXT,
        new ImageView(Consts.Images.SHOW_STUDENT_MENU_ITEM)
    );
    private MenuItem showHistoryItem = new MenuItem(
        Consts.Strings.SHOW_HISTORY_MENU_ITEM_TEXT,
        new ImageView(Consts.Images.SHOW_HISTORY_MENU_ITEM)
    );

    private ContextMenu contextMenu = new ContextMenu(
        changeBookItem,
        deleteBookItem,
        showStudentItem,
        showHistoryItem
    );

    public BookRow(MainWindowController controller)
    {
        setOnDragDetected(event ->
        {
            // Check if the book isn't borrowed by any student
            if (getItem() == null || getItem().getBorrowedBy() != 0)
            {
                return;
            }

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            // Content is necessary for it to work
            content.putString("");

            dragboard.setContent(content);
            dragboard.setDragView(Consts.Images.BORROW_BOOK);
        });
        
        changeBookItem.setOnAction(event -> controller.changeBook(getItem()));
        deleteBookItem.setOnAction(event -> controller.deleteBook(getItem()));
        showStudentItem.setOnAction(event -> controller.selectBorrowedStudent(getItem()));
        showHistoryItem.setOnAction(event -> controller.showHistory(getItem()));
    }
    
    @Override
    protected void updateItem(Book item, boolean empty)
    {
        super.updateItem(item, empty);

        getStyleClass().removeAll(
            Consts.Misc.BOOK_NOT_BORROWED_CLASS,
            Consts.Misc.BOOK_BORROWED_CLASS
        );

        if (empty)
        {
            setItem(null);
            setContextMenu(null);

            return;
        }

        if (item.getBorrowedBy() == 0)
        {
            getStyleClass().add(Consts.Misc.BOOK_NOT_BORROWED_CLASS);

            deleteBookItem.setDisable(false);
            showStudentItem.setDisable(true);
        }

        else
        {
            getStyleClass().add(Consts.Misc.BOOK_BORROWED_CLASS);

            deleteBookItem.setDisable(true);
            showStudentItem.setDisable(false);
        }

        setContextMenu(contextMenu);
    }
}
