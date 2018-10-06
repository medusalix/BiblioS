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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AlertUtils
{
    public static void showWarning(String title, String message)
    {
        createAlert(Alert.AlertType.WARNING, title, message).showAndWait();
    }
    
    public static void showConfirmation(String title, String message, Runnable success)
    {
        createAlert(Alert.AlertType.CONFIRMATION, title, message, ButtonType.YES, ButtonType.NO)
            .showAndWait()
            .filter(buttonType -> buttonType == ButtonType.YES)
            .ifPresent(buttonType -> success.run());
    }
    
    private static Alert createAlert(Alert.AlertType type, String title, String message, ButtonType... buttons)
    {
        Alert alert = new Alert(type, message, buttons);

        alert.getDialogPane()
            .getStylesheets()
            .add(BiblioS.class.getResource(Consts.Paths.STYLESHEET).toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(title);
        
        ((Stage)alert.getDialogPane()
            .getScene()
            .getWindow())
            .getIcons()
            .add(Consts.Images.ICON);
        
        return alert;
    }
}
