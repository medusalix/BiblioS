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
import javafx.scene.control.TextField;

public class PasswordDialog extends FormDialog<String>
{
    private TextField passwordField = (TextField)lookupNode(Consts.FxIds.PASSWORD_FIELD);
    
    public PasswordDialog()
    {
        super(
            Consts.Paths.PASSWORD_DIALOG,
            Consts.Images.PASSWORD_DIALOG_HEADER,
            Consts.Dialogs.PASSWORD_REQUIRED_TEXT
        );
        
        addTextFields(passwordField);
    }
    
    @Override
    protected boolean isFilled()
    {
        return !passwordField.getText().isEmpty();
    }
    
    @Override
    protected String getFormResult()
    {
        return passwordField.getText();
    }
}
