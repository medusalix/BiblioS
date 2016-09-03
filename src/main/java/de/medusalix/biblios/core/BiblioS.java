/*
 * Copyright (C) 2016 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.medusalix.biblios.core;

import de.medusalix.biblios.database.DatabaseManager;
import de.medusalix.biblios.utils.BackupUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class BiblioS extends Application
{
    private static final Logger logger = LogManager.getLogger(BiblioS.class);
    
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            DatabaseManager.createDatabase();
    
            BackupUtils.createBackup();
            BackupUtils.deleteObsoleteBackups();

            prepareAndShowMainWindow(primaryStage);
        }

        catch (IOException e)
        {
            logger.error("", e);
        }
	}
    
    private void prepareAndShowMainWindow(Stage stage) throws IOException
    {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(Consts.Paths.MAIN_WINDOW)));
        
        scene.getStylesheets().add(getClass().getResource(Consts.Paths.STYLESHEET).toExternalForm());
        
        stage.setTitle(Consts.WINDOW_TITLE);
        stage.getIcons().add(Consts.Images.FAVICON);
        stage.setMaximized(System.getenv("debug") == null);
    	stage.setScene(scene);
        stage.show();

        // Needs to be set after the stage has been shown
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
