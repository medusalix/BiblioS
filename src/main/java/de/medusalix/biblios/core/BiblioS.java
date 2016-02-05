package de.medusalix.biblios.core;

import de.medusalix.biblios.dao.StudentDao;
import de.medusalix.biblios.dto.Student;
import de.medusalix.biblios.managers.BackupManager;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.UpdateManager;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.helpers.RuntimeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import de.medusalix.biblios.helpers.RuntimeHelper;
import de.medusalix.biblios.managers.BackupManager;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.managers.UpdateManager;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.io.*;
import java.time.DateTimeException;

public class BiblioS extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
		try
		{
            DBI dbi = new DBI("jdbc:h2:file:./test");
            StudentDao dao = dbi.onDemand(StudentDao.class);

            dao.createTable();
            dao.insert(new Student("Severin", "9a"));

            UpdateManager.update();
			//DatabaseManager.init();
            BackupManager.init();

            prepareAndShowMainWindow(primaryStage);
		}

		catch (IOException e)
		{
			ReportManager.reportException(e);
		}
	}
    
    private void prepareAndShowMainWindow(Stage stage) throws IOException
    {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(Consts.Resources.MAIN_WINDOW_PATH)));
        
        scene.getStylesheets().add(getClass().getResource(Consts.Resources.STYLESHEET_PATH).toExternalForm());
        
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
