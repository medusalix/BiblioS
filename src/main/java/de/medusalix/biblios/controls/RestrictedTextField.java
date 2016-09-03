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

package de.medusalix.biblios.controls;

import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class RestrictedTextField extends TextField
{
    private Predicate<String> inputRestriction, submitRestriction;

    public void setInputRestriction(Predicate<String> inputRestriction)
    {
        this.inputRestriction = inputRestriction;
    }

    public void setSubmitRestriction(Predicate<String> submitRestriction)
    {
        this.submitRestriction = submitRestriction;
    }

    public boolean isSubmitRestrictionMet()
    {
        return getText() != null && submitRestriction.test(getText());
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        // When the text in the box is null, use only the variable 'text'
        String finalText = getText() == null ? text : getText() + text;

        // The variable 'text' is empty when backspace is pressed
        if (text.isEmpty() || inputRestriction.test(finalText))
        {
            super.replaceText(start, end, text);
        }
    }
}
