package de.medusalix.biblios.controls;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.pojos.BorrowListTableItem;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;

public class BorrowListExceededCell extends TableCell<BorrowListTableItem, Boolean>
{
    private ImageView imageView = new ImageView();

    @Override
    protected void updateItem(Boolean item, boolean empty)
    {
        super.updateItem(item, empty);

        if (!empty)
        {
            setGraphic(imageView);

            imageView.setImage(item ? Consts.Images.EXCEEDED : Consts.Images.NOT_EXCEEDED);
        }

        else
        {
            setGraphic(null);
        }
    }
}
