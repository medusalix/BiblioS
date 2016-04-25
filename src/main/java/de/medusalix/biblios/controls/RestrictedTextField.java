package de.medusalix.biblios.controls;

import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class RestrictedTextField extends TextField
{
    private Predicate<String> restriction, submitRestriction;

    public void setRestriction(Predicate<String> restriction)
    {
        this.restriction = restriction;
    }

    public void setSubmitRestriction(Predicate<String> submitRestriction)
    {
        this.submitRestriction = submitRestriction;
    }

    public boolean isSubmitRestrictionMet()
    {
        return submitRestriction.test(getText());
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        // When the text in the box is null, use only the variable 'text'
        String finalText = getText() == null ? text : getText() + text;

        // The variable 'text' is empty when backspace is pressed
        if (text.isEmpty() || restriction.test(finalText))
        {
            super.replaceText(start, end, text);
        }
    }
}
