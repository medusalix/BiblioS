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

package de.medusalix.biblios.utils;

import de.medusalix.biblios.core.BiblioS;
import de.medusalix.biblios.core.Consts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class WindowUtils
{
    public static Stage openWindow(String title, String fxmlPath) throws IOException
    {
        Scene scene = new Scene(FXMLLoader.load(WindowUtils.class.getResource(fxmlPath)));

        scene.getStylesheets().add(BiblioS.class.getResource(Consts.Paths.STYLESHEET).toExternalForm());

        Stage stage = new Stage();
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.getIcons().add(Consts.Images.FAVICON);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        return stage;
    }
}
