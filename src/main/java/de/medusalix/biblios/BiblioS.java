/*
 * Copyright (C) 2017 Medusalix
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

package de.medusalix.biblios;

import de.medusalix.biblios.database.Database;
import de.medusalix.biblios.utils.BackupUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class BiblioS extends Application
{
    private static final Logger logger = LogManager.getLogger(BiblioS.class);
    
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            if (checkRunning())
            {
                Platform.exit();

                return;
            }

            Database.create();
    
            BackupUtils.createBackup();
            BackupUtils.deleteObsoleteBackups();

            createWindow(primaryStage);
        }

        catch (IOException e)
        {
            logger.error("", e);
        }
	}

    private boolean checkRunning() throws IOException
    {
        FileChannel channel = FileChannel.open(
            Consts.Paths.LOCK,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE
        );

        // Check if lock is already acquired
        return channel.tryLock() == null;
    }

    private void createWindow(Stage stage) throws IOException
    {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(Consts.Paths.MAIN_WINDOW)));
        
        scene.getStylesheets()
            .add(getClass().getResource(Consts.Paths.STYLESHEET).toExternalForm());
        
        stage.setTitle(Consts.Strings.MAIN_WINDOW_TITLE);
        stage.getIcons().add(Consts.Images.ICON);
    	stage.setScene(scene);
        stage.show();

        // Needs to be set after the stage has been shown
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.setMaximized(System.getenv("debug") == null);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
