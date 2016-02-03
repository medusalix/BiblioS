package de.medusalix.biblios.controls;

import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.pojos.BorrowedBookTableItem;
import de.medusalix.biblios.sql.query.general.UpdateQuery;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import de.medusalix.biblios.controllers.UpdatableController;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class BorrowedBookRow extends TableRow<BorrowedBookTableItem>
{
    private MenuItem extendBookItem = new MenuItem(Consts.Messages.EXTEND_MENU_ITEM_TEXT, new ImageView(Consts.Images.EXTEND_MENU_ITEM_IMAGE));

    private ContextMenu contextMenu = new ContextMenu(extendBookItem);

    public BorrowedBookRow(UpdatableController controller)
    {
        setOnDragDetected(event ->
        {
            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            content.putString(String.valueOf(getItem().getBookId()));

            dragboard.setContent(content);
        });

        extendBookItem.setOnAction(event ->
        {
            try (Connection connection = DatabaseManager.openConnection())
            {
                String returnDate = LocalDate.parse(getItem().getReturnDate(), Consts.Misc.DATE_FORMATTER).plusDays(14).format(Consts.Misc.DATE_FORMATTER);

                new UpdateQuery(Consts.Database.BORROWED_BOOKS_TABLE_NAME, Consts.Database.RETURN_DATE_COLUMN_NAME, returnDate).execute(connection);

                controller.updateData();
            }

            catch (SQLException e)
            {
                ReportManager.reportException(e);
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
