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
import de.medusalix.biblios.database.objects.Student;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class StudentCell extends ListCell<Student>
{
    private MenuItem changeStudentItem = new MenuItem(
        Consts.Strings.CHANGE_MENU_ITEM_TEXT,
        new ImageView(Consts.Images.CHANGE_MENU_ITEM)
    );
    private MenuItem deleteStudentItem = new MenuItem(
        Consts.Strings.DELETE_MENU_ITEM_TEXT,
        new ImageView(Consts.Images.DELETE_MENU_ITEM)
    );

    private ContextMenu contextMenu = new ContextMenu(
        changeStudentItem,
        deleteStudentItem
    );

    public StudentCell(MainWindowController controller)
    {
        changeStudentItem.setOnAction(event -> controller.changeStudent(getItem()));
        deleteStudentItem.setOnAction(event -> controller.deleteStudent(getItem()));
    }
    
    @Override
    protected void updateItem(Student item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty)
        {
            setText(null);
            setContextMenu(null);

            return;
        }

        setText(item.getName());

        deleteStudentItem.setDisable(item.getHasBorrows());

        setContextMenu(contextMenu);
    }
}
