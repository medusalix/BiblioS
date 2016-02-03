package de.medusalix.biblios.controls;

import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class RestrictedTextField extends TextField
{
    private Predicate<String> restriction;
    private Predicate<String> submitRestriction;

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
        if (text.isEmpty() || restriction.test(text))
        {
            super.replaceText(start, end, text);
        }
    }
}
