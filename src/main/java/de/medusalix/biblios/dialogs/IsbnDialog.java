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

import de.medusalix.biblios.controls.RestrictedTextField;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.googlebooks.GoogleBooks;
import de.medusalix.biblios.googlebooks.VolumeInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsbnDialog extends FormDialog<Pair<String, VolumeInfo>>
{
    private RestrictedTextField isbnField = (RestrictedTextField)lookupNode(Consts.FxIds.ISBN_FIELD);
    
    private Matcher isbnMatcher = Pattern.compile("[0-9]{0,13}").matcher("");
    
    public IsbnDialog()
    {
        super(Consts.Paths.ISBN_DIALOG, Consts.Images.FETCH_DIALOG_HEADER, Consts.Dialogs.ADD_BOOK_TEXT);
    
        isbnField.setInputRestriction(isbn -> isbnMatcher.reset(isbn).matches());
        isbnField.setSubmitRestriction(isbn -> isbn.length() == 10 || isbn.length() == 13);
        
        initButtonBehavior();
        addTextFields(new TextField[] { isbnField });
    }
    
    private void initButtonBehavior()
    {
        getOkButton().addEventFilter(ActionEvent.ACTION, event ->
        {
            event.consume();
    
            getDialogPane().setDisable(true);
            getDialogPane().getScene().getWindow().setOnCloseRequest(Event::consume);
            
            GoogleBooks.findVolumeInfoForIsbn(
                    isbnField.getText(),
                    volumeInfo -> Platform.runLater(() ->
                    {
                        if (volumeInfo != null)
                        {
                            setResult(new Pair<>(isbnField.getText(), volumeInfo));
                        }
        
                        else
                        {
                            setResult(new Pair<>(isbnField.getText(), null));
                        }
                        
                        close();
                    })
            );
        });
    }
    
    @Override
    protected boolean isFilled()
    {
        return isbnField.isSubmitRestrictionMet();
    }
    
    // Handled by setResult
    @Override
    protected Pair<String, VolumeInfo> getFormResult()
    {
        return null;
    }
}
