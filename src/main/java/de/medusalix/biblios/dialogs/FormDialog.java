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

package de.medusalix.biblios.dialogs;

import de.medusalix.biblios.Consts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public abstract class FormDialog<R> extends Dialog<R>
{
    private static final Logger logger = LogManager.getLogger(FormDialog.class);
    
    private Button okButton;
    
    protected FormDialog(String fxmlFileName, Image image, String text)
    {
        super();
    
        initLayout(fxmlFileName);
        initContent(image, text);
        initButtons();
        initResultConverter();
    }
    
    protected abstract boolean isFilled();
    protected abstract R getFormResult();
    
    private void initLayout(String fxmlFileName)
    {
        try
        {
            DialogPane pane = getDialogPane();

            pane.setContent(FXMLLoader.load(getClass().getResource(fxmlFileName)));
            pane.getStylesheets()
                .add(getClass().getResource(Consts.Paths.STYLESHEET).toExternalForm());
        }
    
        catch (IOException e)
        {
            logger.error("", e);
        }
    }
    
    private void initContent(Image image, String text)
    {
        Stage stage = (Stage)getDialogPane().getScene().getWindow();

        // Set the favicon
        stage.getIcons().add(Consts.Images.ICON);
    
        setGraphic(new ImageView(image));
        setTitle(text);
        setHeaderText(text);
    }
    
    private void initButtons()
    {
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        
        okButton = (Button)getDialogPane().lookupButton(ButtonType.OK);
                                
        okButton.setDisable(true);
    }
    
    private void initResultConverter()
    {
        setResultConverter(param -> param == ButtonType.OK ? getFormResult() : null);
    }
    
    protected Node lookupNode(String name)
    {
        return getDialogPane().lookup(name);
    }
    
    protected Button getOkButton()
    {
        return okButton;
    }
    
    protected void addTextFields(TextField... textFields)
    {
        for (TextField textField : textFields)
        {
            textField.textProperty().addListener((observable, oldValue, newValue) ->
                okButton.setDisable(!isFilled())
            );
        }
    
        textFields[0].requestFocus();
    }
}
