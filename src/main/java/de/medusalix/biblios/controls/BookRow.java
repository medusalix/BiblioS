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

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.BookDatabase;
import de.medusalix.biblios.database.access.StatDatabase;
import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.database.objects.Student;
import de.medusalix.biblios.dialogs.BookDialog;
import de.medusalix.biblios.utils.AlertUtils;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookRow extends TableRow<Book>
{
    private static final Logger logger = LogManager.getLogger(BookRow.class);
    
    private UpdatableController controller;
    private BookDatabase bookDatabase;
    private StatDatabase statDatabase;
    
    private TextField studentSearchField;
    private ListView<Student> studentListView;
    
    private MenuItem changeBookItem = new MenuItem(Consts.Strings.CHANGE_MENU_ITEM_TEXT, new ImageView(Consts.Images.CHANGE_MENU_ITEM));
    private MenuItem deleteBookItem = new MenuItem(Consts.Strings.DELETE_MENU_ITEM_TEXT, new ImageView(Consts.Images.DELETE_MENU_ITEM));
    private MenuItem showStudentItem = new MenuItem(Consts.Strings.SHOW_STUDENT_MENU_ITEM_TEXT, new ImageView(Consts.Images.SHOW_STUDENT_MENU_ITEM));

    private ContextMenu contextMenu = new ContextMenu(changeBookItem, deleteBookItem, showStudentItem);

    public BookRow(UpdatableController controller, BookDatabase bookDatabase, StatDatabase statDatabase, TextField studentSearchField, ListView<Student> studentListView)
    {
        this.controller = controller;
        this.bookDatabase = bookDatabase;
        this.statDatabase = statDatabase;
        
        this.studentSearchField = studentSearchField;
        this.studentListView = studentListView;
        
        setOnDragDetected(event ->
        {
            // Check if the book isn't borrowed by any student
            if (getItem() != null && getItem().getBorrowedBy() == 0)
            {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(String.valueOf(getItem().getId()));

                dragboard.setContent(content);
            }
        });
        
        changeBookItem.setOnAction(event -> change());
        deleteBookItem.setOnAction(event -> delete());
        showStudentItem.setOnAction(event -> showStudent());
    }
    
    private void change()
    {
        new BookDialog(getItem()).showAndWait().ifPresent(book ->
        {
            bookDatabase.update(getItem().getId(), book);
            
            logger.info("Changed " + getItem());
            
            controller.update();
        });
    }
    
    private void delete()
    {
        AlertUtils.showConfirmation(
                Consts.Dialogs.DELETE_BOOK_TITLE,
                Consts.Dialogs.DELETE_BOOK_MESSAGE,
                () ->
                {
                    long id = getItem().getId();
                    
                    statDatabase.deleteByBookId(id);
                    bookDatabase.delete(id);
    
                    logger.info("Deleted " + getItem());
    
                    controller.update();
                }
        );
    }
    
    private void showStudent()
    {
        studentSearchField.clear();
    
        long borrowedBy = getItem().getBorrowedBy();
    
        studentListView
                .getItems()
                .stream()
                .filter(student -> borrowedBy == student.getId())
                .findFirst()
                .ifPresent(student ->
                {
                    studentListView.getSelectionModel().select(student);
                    studentListView.scrollTo(student);
                });
    }
    
    @Override
    protected void updateItem(Book item, boolean empty)
    {
        super.updateItem(item, empty);

        if (!empty)
        {
            if (item.getBorrowedBy() == 0)
            {
                setBackground(Consts.Misc.BOOK_NOT_BORROWED_BACKGROUND);

                deleteBookItem.setDisable(false);
                showStudentItem.setDisable(true);
            }

            else
            {
                setBackground(Consts.Misc.BOOK_BORROWED_BACKGROUND);

                deleteBookItem.setDisable(true);
                showStudentItem.setDisable(false);
            }

            setContextMenu(contextMenu);
        }

        else
        {
            setItem(null);
            setBackground(null);
            setContextMenu(null);
        }
    }
}
