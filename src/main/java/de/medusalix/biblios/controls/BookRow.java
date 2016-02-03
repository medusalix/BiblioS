package de.medusalix.biblios.controls;

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.pojos.Book;
import de.medusalix.biblios.pojos.BookTableItem;
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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;
import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.pojos.Book;
import de.medusalix.biblios.pojos.BookTableItem;
import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.general.DeleteQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRow extends TableRow<BookTableItem>
{
    private MenuItem changeBookItem = new MenuItem(Consts.Messages.CHANGE_MENU_ITEM_TEXT, new ImageView(Consts.Images.CHANGE_MENU_ITEM_IMAGE));
    private MenuItem deleteBookItem = new MenuItem(Consts.Messages.DELETE_MENU_ITEM_TEXT, new ImageView(Consts.Images.DELETE_MENU_ITEM_IMAGE));
    private MenuItem showStudentItem = new MenuItem(Consts.Messages.SHOW_STUDENT_MENU_ITEM_TEXT, new ImageView(Consts.Images.SHOW_STUDENT_MENU_ITEM_IMAGE));

    private ContextMenu contextMenu = new ContextMenu(changeBookItem, deleteBookItem, showStudentItem);

    public BookRow(UpdatableController controller, TextField studentSearchField, ListView<StudentListItem> studentListView)
    {
        setOnDragDetected(event ->
        {
            if (getItem().getBorrowedBy() == null)
            {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(String.valueOf(getItem().getId()));

                dragboard.setContent(content);
            }
        });

        changeBookItem.setOnAction(event ->
        {
            Book book = Dialogs.showBookDialog(Consts.Messages.CHANGE_BOOK_TEXT, Consts.Images.CHANGE_HEADER_IMAGE, getItem());

            if (book != null)
            {
                try (Connection connection = DatabaseManager.openConnection())
                {
                    List<Pair<String, Object>> pairs = new ArrayList<>();

                    pairs.add(new Pair<>(Consts.Database.TITLE_COLUMN_NAME, book.getTitle()));
                    pairs.add(new Pair<>(Consts.Database.AUTHOR_COLUMN_NAME, book.getAuthor()));
                    pairs.add(new Pair<>(Consts.Database.ISBN_COLUMN_NAME, book.getIsbn()));
                    pairs.add(new Pair<>(Consts.Database.PUBLISHER_COLUMN_NAME, book.getPublisher()));
                    pairs.add(new Pair<>(Consts.Database.PUBLISHED_DATE_COLUMN_NAME, book.getPublishedDate()));
                    pairs.add(new Pair<>(Consts.Database.ADDITIONAL_INFO_COLUMN_NAME, book.getAdditionalInfo()));

                    ActionQuery changeQuery = new UpdateQuery(Consts.Database.BOOKS_TABLE_NAME, pairs);

                    changeQuery.addOperator(new WhereOperator(Consts.Database.ID_COLUMN_NAME, getItem().getId()));
                    changeQuery.execute(connection);

                    controller.updateData();
                }

                catch (SQLException e)
                {
                    ReportManager.reportException(e);
                }
            }
        });

        deleteBookItem.setOnAction(event ->
        {
            try (Connection connection = DatabaseManager.openConnection())
            {
                new DeleteQuery(Consts.Database.BOOKS_TABLE_NAME, Consts.Database.ID_COLUMN_NAME, getItem().getId()).execute(connection);

                controller.updateData();
            }

            catch (SQLException e)
            {
                ReportManager.reportException(e);
            }
        });

        showStudentItem.setOnAction(event ->
        {
            studentSearchField.clear();

            StudentListItem student = studentListView.getItems().stream().filter(student2 -> student2.getName().equals(getItem().getBorrowedBy())).findFirst().get();

            studentListView.getSelectionModel().select(student);
            studentListView.scrollTo(student);
        });
    }

    @Override
    protected void updateItem(BookTableItem item, boolean empty)
    {
        super.updateItem(item, empty);

        if (!empty)
        {
            if (item.getBorrowedBy() == null)
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
