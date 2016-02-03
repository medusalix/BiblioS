package de.medusalix.biblios.controls;

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.pojos.Student;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.pojos.StudentListItem;
import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.base.ActionQuery;
import de.medusalix.biblios.sql.query.general.DeleteQuery;
import de.medusalix.biblios.sql.query.general.UpdateQuery;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.pojos.Student;
import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.general.DeleteQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentCell extends ListCell<StudentListItem>
{
    private MenuItem changeStudentItem = new MenuItem(Consts.Messages.CHANGE_MENU_ITEM_TEXT, new ImageView(Consts.Images.CHANGE_MENU_ITEM_IMAGE));
    private MenuItem deleteStudentItem = new MenuItem(Consts.Messages.DELETE_MENU_ITEM_TEXT, new ImageView(Consts.Images.DELETE_MENU_ITEM_IMAGE));

    private ContextMenu contextMenu = new ContextMenu(changeStudentItem, deleteStudentItem);

    public StudentCell(UpdatableController controller)
    {
        changeStudentItem.setOnAction(event ->
        {
            Student student = Dialogs.showStudentDialog(Consts.Messages.CHANGE_STUDENT_TEXT, Consts.Images.CHANGE_HEADER_IMAGE, getItem());

            if (student != null)
            {
                try (Connection connection = DatabaseManager.openConnection())
                {
                    List<Pair<String, Object>> pairs = new ArrayList<>();

                    pairs.add(new Pair<>(Consts.Database.NAME_COLUMN_NAME, student.getName()));
                    pairs.add(new Pair<>(Consts.Database.GRADE_COLUMN_NAME, student.getGrade()));

                    ActionQuery updateQuery = new UpdateQuery(Consts.Database.STUDENTS_TABLE_NAME, pairs);

                    updateQuery.addOperator(new WhereOperator(Consts.Database.ID_COLUMN_NAME, getItem().getId()));
                    updateQuery.execute(connection);

                    controller.updateData();
                }

                catch (SQLException e)
                {
                    ReportManager.reportException(e);
                }
            }
        });

        deleteStudentItem.setOnAction(event ->
        {
            try (Connection connection = DatabaseManager.openConnection())
            {
                new DeleteQuery(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.ID_COLUMN_NAME, getItem().getId()).execute(connection);

                controller.updateData();
            }

            catch (SQLException e)
            {
                ReportManager.reportException(e);
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
