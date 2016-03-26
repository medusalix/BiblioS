package de.medusalix.biblios.utils;

import de.medusalix.biblios.core.Reference;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class DialogUtils
{
    public static <T> Dialog<T> createCustomDialog(String fxml)
    {
        Dialog<T> dialog = new Dialog<>();

        try
        {
            dialog.getDialogPane().setContent(FXMLLoader.load(DialogUtils.class.getResource(fxml)));

            // Set the favicon
            ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(Reference.Images.FAVICON);
        }

        catch (IOException e)
        {
            ExceptionUtils.log(e);
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
