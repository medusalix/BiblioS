package de.medusalix.biblios.controls;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.pojos.BorrowedBookTableItem;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;

public class ExceededCell extends TableCell<BorrowedBookTableItem, Boolean>
{
    private ImageView imageView = new ImageView();

    @Override
    protected void updateItem(Boolean item, boolean empty)
    {
        super.updateItem(item, empty);

        if (!empty)
        {
            setGraphic(imageView);

            imageView.setImage(item ? Consts.Images.EXCEEDED_IMAGE : Consts.Images.NOT_EXCEEDED_IMAGE);
        }

        else
        {
            setGraphic(null);
        }
    }
}
