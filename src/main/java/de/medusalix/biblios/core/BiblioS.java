package de.medusalix.biblios.core;

import de.medusalix.biblios.helpers.RuntimeHelper;
import de.medusalix.biblios.managers.BackupManager;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.ExceptionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class BiblioS extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
		try
		{
			DatabaseManager.init();
            BackupManager.init();

            prepareAndShowMainWindow(primaryStage);
		}

		catch (IOException e)
		{
            ExceptionManager.log(e);
		}
	}
    
    private void prepareAndShowMainWindow(Stage stage) throws IOException
    {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(Consts.Paths.MAIN_WINDOW)));
        
        scene.getStylesheets().add(getClass().getResource(Consts.Paths.STYLESHEET).toExternalForm());
        
        stage.setTitle(Consts.TITLE);
        stage.getIcons().add(new Image(Consts.Images.FAVICON_IMAGE_PATH));
        stage.setMaximized(RuntimeHelper.isRelease());
    	stage.setScene(scene);
        stage.show();

        // Needs to be set after the stage has been shown
        stage.minWidthProperty().set(stage.getWidth());
        stage.minHeightProperty().set(stage.getHeight());
    }

	public static void main(String[] args)
    {
        launch(args);
    }
}
