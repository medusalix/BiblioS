package de.medusalix.biblios.core;

import de.medusalix.biblios.utils.Exceptions;
import de.medusalix.biblios.managers.BackupManager;
import de.medusalix.biblios.managers.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
            Exceptions.log(e);
		}
	}
    
    private void prepareAndShowMainWindow(Stage stage) throws IOException
    {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(Reference.Paths.MAIN_WINDOW)));
        
        scene.getStylesheets().add(getClass().getResource(Reference.Paths.STYLESHEET).toExternalForm());
        
        stage.setTitle(Reference.TITLE);
        stage.getIcons().add(Reference.Images.FAVICON);
        stage.setMaximized(System.getenv("debug") == null);
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
