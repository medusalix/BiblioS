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

import de.medusalix.biblios.core.Consts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
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
    
    private void initLayout(String fxmlFileName)
    {
        try
        {
            getDialogPane().setContent(FXMLLoader.load(getClass().getResource(fxmlFileName)));
        }
    
        catch (IOException e)
        {
            logger.error("", e);
        }
    }
    
    private void initContent(Image image, String text)
    {
        // Set the favicon
        ((Stage)getDialogPane().getScene().getWindow()).getIcons().add(Consts.Images.FAVICON);
    
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
    
    protected abstract boolean isFilled();
    
    protected void addTextFields(TextField[] textFields)
    {
        for (TextField textField : textFields)
        {
            textField.textProperty().addListener((observable, oldValue, newValue) -> okButton.setDisable(!isFilled()));
        }
    
        textFields[0].requestFocus();
    }
    
    protected abstract R getFormResult();
}
