package de.medusalix.biblios.helpers;

import de.medusalix.biblios.core.Consts;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ProgressHelper
{
    private static Dialog<Void> dialog = DialogHelper.createCustomDialog(Consts.Paths.PROGRESS_DIALOG);

    static
    {
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // Without a button we can't close the dialog
        Node button = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        // Hide the button and remove the occupied space
        button.setManaged(false);
        button.setVisible(false);
    }

    public static void showDialog(String title)
    {
        dialog.setTitle(title);
        dialog.show();
    }

    public static void hideDialog()
    {
        dialog.hide();
    }
}
