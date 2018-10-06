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

import de.medusalix.biblios.BiblioS;
import de.medusalix.biblios.Consts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowUtils
{
    public static void openWindow(String title, String fxmlPath) throws IOException
    {
        openWindow(title, fxmlPath, null);
    }

    public static <T> void openWindow(String title, String fxmlPath, T controller) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource(fxmlPath));

        if (controller != null)
        {
            loader.setController(controller);
        }

        Scene scene = new Scene(loader.load());

        scene.getStylesheets()
            .add(BiblioS.class.getResource(Consts.Paths.STYLESHEET).toExternalForm());

        Stage stage = new Stage();
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.getIcons().add(Consts.Images.ICON);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
