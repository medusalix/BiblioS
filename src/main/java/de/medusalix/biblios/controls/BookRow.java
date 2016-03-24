package de.medusalix.biblios.controls;

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.database.access.Books;
import de.medusalix.biblios.database.access.Stats;
import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.pojos.BookTableItem;
import de.medusalix.biblios.core.Dialogs;
import de.medusalix.biblios.helpers.Exceptions;
import de.medusalix.biblios.pojos.StudentListItem;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

public class BookRow extends TableRow<BookTableItem>
{
    private Logger logger = LogManager.getLogger(BookRow.class);

    private MenuItem changeBookItem = new MenuItem(Reference.Strings.CHANGE_MENU_ITEM_TEXT, new ImageView(Reference.Images.CHANGE_MENU_ITEM));
    private MenuItem deleteBookItem = new MenuItem(Reference.Strings.DELETE_MENU_ITEM_TEXT, new ImageView(Reference.Images.DELETE_MENU_ITEM));
    private MenuItem showStudentItem = new MenuItem(Reference.Strings.SHOW_STUDENT_MENU_ITEM_TEXT, new ImageView(Reference.Images.SHOW_STUDENT_MENU_ITEM));

    private ContextMenu contextMenu = new ContextMenu(changeBookItem, deleteBookItem, showStudentItem);

    public BookRow(UpdatableController controller, Books books, Stats stats, TextField studentSearchField, ListView<StudentListItem> studentListView)
    {
        setOnDragDetected(event ->
        {
            if (getItem() != null && getItem().getBorrowedBy() == null)
            {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(String.valueOf(getItem().getId()));

                dragboard.setContent(content);
            }
        });

        changeBookItem.setOnAction(event ->
        {
            Book book = Dialogs.showBookDialog(Reference.Dialogs.CHANGE_BOOK_TEXT, Reference.Images.CHANGE_DIALOG_HEADER, getItem());

            if (book != null)
            {
                try
                {
                    books.update(getItem().getId(), book);

                    logger.info("Book information changed");

                    controller.updateData();
                }

                catch (DBIException e)
                {
                    Exceptions.log(e);
                }
            }
        });

        deleteBookItem.setOnAction(event ->
        {
            long id = getItem().getId();

            try
            {
                if (stats.countByBookId(id) == 1)
                {
                    stats.deleteByBookId(id);
                }

                books.delete(id);

                logger.info("Book deleted");

                controller.updateData();
            }

            catch (DBIException e)
            {
                Exceptions.log(e);
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
                setBackground(Reference.Misc.BOOK_NOT_BORROWED_BACKGROUND);

                deleteBookItem.setDisable(false);
                showStudentItem.setDisable(true);
            }

            else
            {
                setBackground(Reference.Misc.BOOK_BORROWED_BACKGROUND);

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
