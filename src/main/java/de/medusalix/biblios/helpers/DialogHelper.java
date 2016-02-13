package de.medusalix.biblios.helpers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.ExceptionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DialogHelper
{
    public static <T> Dialog<T> createCustomDialog(String fxml)
    {
        Dialog<T> dialog = new Dialog<>();

        try
        {
            dialog.getDialogPane().setContent(FXMLLoader.load(DialogHelper.class.getResource(fxml)));

            // Set the favicon
            ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(Consts.Images.FAVICON);
        }

        catch (IOException e)
        {
            ExceptionManager.log(e);
        }

        return dialog;
    }

    public static Dialog<ButtonType> createStandardDialog(String text, Image image, String fxml)
    {
        Dialog<ButtonType> dialog = createCustomDialog(fxml);

        dialog.setTitle(text);
        dialog.setHeaderText(text);
        dialog.setGraphic(new ImageView(image));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        return dialog;
    }

    public static Alert createAlert(Alert.AlertType type, String titleText, String contentText)
    {
        Alert alert = new Alert(type);

        alert.setTitle(titleText);
        alert.setHeaderText(titleText);
        alert.setContentText(contentText);

        return alert;
    }
}
