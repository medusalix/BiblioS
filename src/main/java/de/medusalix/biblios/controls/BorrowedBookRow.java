package de.medusalix.biblios.controls;

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Reference;
import de.medusalix.biblios.database.access.BorrowedBooks;
import de.medusalix.biblios.utils.ExceptionUtils;
import de.medusalix.biblios.pojos.BorrowedBookTableItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

import java.time.LocalDate;

public class BorrowedBookRow extends TableRow<BorrowedBookTableItem>
{
    private Logger logger = LogManager.getLogger(BorrowedBookRow.class);

    private MenuItem extendBookItem = new MenuItem(Reference.Strings.EXTEND_MENU_ITEM_TEXT, new ImageView(Reference.Images.EXTEND_MENU_ITEM));

    private ContextMenu contextMenu = new ContextMenu(extendBookItem);

    public BorrowedBookRow(UpdatableController controller, BorrowedBooks borrowedBooks)
    {
        setOnDragDetected(event ->
        {
            if (getItem() != null)
            {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(String.valueOf(getItem().getId()));

                dragboard.setContent(content);
            }
        });

        extendBookItem.setOnAction(event ->
        {
            String returnDate = LocalDate.parse(getItem().getReturnDate(), Reference.Misc.DATE_FORMATTER).plusDays(14).format(Reference.Misc.DATE_FORMATTER);

            try
            {
                borrowedBooks.updateReturnDate(getItem().getId(), returnDate);

                logger.info("Borrowed book extended");

                controller.updateData();
            }

            catch (DBIException e)
            {
                ExceptionUtils.log(e);
            }
        });
    }

    @Override
    protected void updateItem(BorrowedBookTableItem item, boolean empty)
    {
        super.updateItem(item, empty);

        if (!empty)
        {
            setContextMenu(contextMenu);
        }

        else
        {
            setContextMenu(null);
        }
    }
}
