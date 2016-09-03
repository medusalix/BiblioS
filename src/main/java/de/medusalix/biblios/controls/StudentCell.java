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
import de.medusalix.biblios.database.access.StudentDatabase;
import de.medusalix.biblios.database.objects.Student;
import de.medusalix.biblios.dialogs.StudentDialog;
import de.medusalix.biblios.utils.AlertUtils;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StudentCell extends ListCell<Student>
{
    private static final Logger logger = LogManager.getLogger(StudentCell.class);

    private UpdatableController controller;
    private StudentDatabase studentDatabase;
    
    private MenuItem changeStudentItem = new MenuItem(Consts.Strings.CHANGE_MENU_ITEM_TEXT, new ImageView(Consts.Images.CHANGE_MENU_ITEM));
    private MenuItem deleteStudentItem = new MenuItem(Consts.Strings.DELETE_MENU_ITEM_TEXT, new ImageView(Consts.Images.DELETE_MENU_ITEM));

    private ContextMenu contextMenu = new ContextMenu(changeStudentItem, deleteStudentItem);

    public StudentCell(UpdatableController controller, StudentDatabase studentDatabase)
    {
        this.controller = controller;
        this.studentDatabase = studentDatabase;
        
        changeStudentItem.setOnAction(event -> change());
        deleteStudentItem.setOnAction(event -> delete());
    }
    
    private void change()
    {
        new StudentDialog(getItem()).showAndWait().ifPresent(student ->
        {
            studentDatabase.update(getItem().getId(), student);
            
            logger.info("Changed " + getItem());
            
            controller.update();
        });
    }
    
    private void delete()
    {
        AlertUtils.showConfirmation(
                Consts.Dialogs.DELETE_STUDENT_TITLE,
                Consts.Dialogs.DELETE_STUDENT_MESSAGE,
                () ->
                {
                    studentDatabase.delete(getItem().getId());
            
                    logger.info("Deleted " + getItem());
            
                    controller.update();
                }
        );
    }
    
    @Override
    protected void updateItem(Student item, boolean empty)
    {
        super.updateItem(item, empty);

        if (!empty)
        {
            setText(item.getName());

            deleteStudentItem.setDisable(item.hasBorrowedBooks());

            setContextMenu(contextMenu);
        }

        else
        {
            setText(null);
            setContextMenu(null);
        }
    }
}
