package de.medusalix.biblios.controls;

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.database.access.Students;
import de.medusalix.biblios.database.objects.Student;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.utils.ExceptionUtils;
import de.medusalix.biblios.pojos.StudentListItem;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

public class StudentCell extends ListCell<StudentListItem>
{
    private Logger logger = LogManager.getLogger(StudentCell.class);

    private MenuItem changeStudentItem = new MenuItem(Reference.Strings.CHANGE_MENU_ITEM_TEXT, new ImageView(Reference.Images.CHANGE_MENU_ITEM));
    private MenuItem deleteStudentItem = new MenuItem(Reference.Strings.DELETE_MENU_ITEM_TEXT, new ImageView(Reference.Images.DELETE_MENU_ITEM));

    private ContextMenu contextMenu = new ContextMenu(changeStudentItem, deleteStudentItem);

    public StudentCell(UpdatableController controller, Students students)
    {
        changeStudentItem.setOnAction(event ->
        {
            Student student = Dialogs.showStudentDialog(Reference.Dialogs.CHANGE_STUDENT_TEXT, Reference.Images.CHANGE_DIALOG_HEADER, getItem());

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
                    ExceptionUtils.log(e);
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
                ExceptionUtils.log(e);
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
