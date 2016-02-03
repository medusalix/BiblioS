package de.medusalix.biblios.helpers;

import de.medusalix.biblios.core.BiblioS;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.ReportManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.medusalix.biblios.core.BiblioS;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.ReportManager;

import java.io.IOException;

public class WindowHelper
{
    public static Stage openUtilityWindow(String title, String fxmlPath)
    {
        try
        {
            Scene scene = new Scene(FXMLLoader.load(WindowHelper.class.getResource(fxmlPath)));

            scene.getStylesheets().add(BiblioS.class.getResource(Consts.Resources.STYLESHEET_PATH).toExternalForm());

            Stage stage = new Stage(StageStyle.UTILITY);

            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            // Needs to be set after the stage has been shown
            stage.minWidthProperty().set(stage.getWidth());
            stage.minHeightProperty().set(stage.getHeight());

            return stage;
        }

        catch (IOException e)
        {
            ReportManager.reportException(e);
        }

        return null;
    }
}
