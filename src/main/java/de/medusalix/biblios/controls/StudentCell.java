package de.medusalix.biblios.controls;

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.Students;
import de.medusalix.biblios.database.objects.Student;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.managers.ExceptionManager;
import de.medusalix.biblios.pojos.StudentListItem;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

public class StudentCell extends ListCell<StudentListItem>
{
    private Logger logger = LogManager.getLogger(StudentCell.class);

    private MenuItem changeStudentItem = new MenuItem(Consts.Strings.CHANGE_MENU_ITEM_TEXT, new ImageView(Consts.Images.CHANGE_MENU_ITEM_IMAGE));
    private MenuItem deleteStudentItem = new MenuItem(Consts.Strings.DELETE_MENU_ITEM_TEXT, new ImageView(Consts.Images.DELETE_MENU_ITEM_IMAGE));

    private ContextMenu contextMenu = new ContextMenu(changeStudentItem, deleteStudentItem);

    public StudentCell(UpdatableController controller, Students students)
    {
        changeStudentItem.setOnAction(event ->
        {
            Student student = Dialogs.showStudentDialog(Consts.Dialogs.CHANGE_STUDENT_TEXT, Consts.Images.CHANGE_HEADER_IMAGE, getItem());

            if (student != null)
            {
                try
                {
                    students.update(getItem().getId(), student);

                    logger.info("Student information changed");

                    controller.updateData();
                }

                catch (DBIException e)
                {
                    ExceptionManager.log(e);
                }
            }
        });

        deleteStudentItem.setOnAction(event ->
        {
            try
            {
                students.delete(getItem().getId());

                logger.info("Student deleted");

                controller.updateData();
            }

            catch (DBIException e)
            {
                ExceptionManager.log(e);
            }
        });
    }

    @Override
    protected void updateItem(StudentListItem item, boolean empty)
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
