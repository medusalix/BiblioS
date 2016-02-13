package de.medusalix.biblios.helpers;

import de.medusalix.biblios.core.BiblioS;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.ExceptionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class WindowHelper
{
    public static Stage openWindow(String title, String fxmlPath)
    {
        try
        {
            Scene scene = new Scene(FXMLLoader.load(WindowHelper.class.getResource(fxmlPath)));

            scene.getStylesheets().add(BiblioS.class.getResource(Consts.Paths.STYLESHEET).toExternalForm());

            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.getIcons().add(Consts.Images.FAVICON);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Needs to be set after the stage has been shown
            stage.minWidthProperty().set(stage.getWidth());
            stage.minHeightProperty().set(stage.getHeight());

            return stage;
        }

        catch (IOException e)
        {
            ExceptionManager.log(e);
        }

        return null;
    }
}
