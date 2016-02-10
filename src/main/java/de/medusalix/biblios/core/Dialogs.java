package de.medusalix.biblios.core;

import de.medusalix.biblios.controls.RestrictedTextField;
import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.database.objects.Student;
import de.medusalix.biblios.helpers.DialogHelper;
import de.medusalix.biblios.helpers.GoogleBooksHelper;
import de.medusalix.biblios.helpers.ProgressHelper;
import de.medusalix.biblios.pojos.BookTableItem;
import de.medusalix.biblios.pojos.GoogleBook;
import de.medusalix.biblios.pojos.StudentListItem;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;

public class Dialogs
{
    public static String showPasswordDialog()
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Dialogs.PASSWORD_REQUIRED_TEXT, Consts.Images.PASSWORD_HEADER_IMAGE, Consts.Paths.PASSWORD_DIALOG);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        PasswordField passwordField = (PasswordField)dialog.getDialogPane().lookup(Consts.FxIds.PASSWORD_FIELD_ID);

        button.setDisable(true);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(passwordField.getText().trim().isEmpty()));
        passwordField.requestFocus();

        if (dialog.showAndWait().get() == ButtonType.OK)
            return passwordField.getText();

        return null;
    }

    public static Student showStudentDialog(String text, Image image, StudentListItem initialValues)
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(text, image, Consts.Paths.STUDENT_DIALOG);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        TextField nameField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.NAME_FIELD_ID);
        RestrictedTextField gradeField = (RestrictedTextField)dialog.getDialogPane().lookup(Consts.FxIds.GRADE_FIELD_ID);

        button.setDisable(true);

        Runnable enableButton = () -> button.setDisable(nameField.getText().trim().isEmpty() || !gradeField.isSubmitRestrictionMet());

        if (initialValues != null)
        {
            nameField.setText(initialValues.getName());
            gradeField.setText(initialValues.getGrade());
        }

        gradeField.setRestriction(grade -> grade.length() <= 3);
        gradeField.setSubmitRestriction(grade -> grade.matches("([5-9]|1[0-2]{1,2})[a-z]"));

        nameField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        gradeField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());

        enableButton.run();

        nameField.requestFocus();

        if (dialog.showAndWait().get() == ButtonType.OK)
            return new Student(nameField.getText(), gradeField.getText());

        return null;
    }

    public static Book showBookDialog(String text, Image image, BookTableItem initialValues)
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(text, image, Consts.Paths.BOOK_DIALOG);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        TextField titleField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.TITLE_FIELD_ID);
        TextField authorField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.AUTHOR_FIELD_ID);
        RestrictedTextField isbnField = (RestrictedTextField)dialog.getDialogPane().lookup(Consts.FxIds.ISBN_FIELD_ID);
        TextField publisherField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.PUBLISHER_FIELD_ID);
        RestrictedTextField publishedDateField = (RestrictedTextField)dialog.getDialogPane().lookup(Consts.FxIds.PUBLISHED_DATE_FIELD_ID);
        TextField additionalInfoField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.ADDITIONAL_INFO_FIELD_ID);

        button.setDisable(true);

        Runnable enableButton = () -> button.setDisable(titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty() || !isbnField.isSubmitRestrictionMet() || publisherField.getText().trim().isEmpty() || !publishedDateField.isSubmitRestrictionMet());

        if (initialValues != null)
        {
            titleField.setText(initialValues.getTitle());
            authorField.setText(initialValues.getAuthor());
            isbnField.setText(String.valueOf(initialValues.getIsbn()));
            publisherField.setText(initialValues.getPublisher());
            publishedDateField.setText(String.valueOf(initialValues.getPublishedDate()));
            additionalInfoField.setText(initialValues.getAdditionalInfo());
        }

        isbnField.setRestriction(isbn -> isbn.matches("\\d*") && isbn.length() <= 13);
        isbnField.setSubmitRestriction(isbn -> isbn.length() == 10 || isbn.length() == 13 || isbn.equals(Consts.Misc.ISBN_PLACEHOLDER));
        publishedDateField.setRestriction(publishedDate -> publishedDate.matches("\\d*") && publishedDate.length() <= 4);
        publishedDateField.setSubmitRestriction(publishedDate -> publishedDate.length() == 4);

        titleField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        authorField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publisherField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publishedDateField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());

        enableButton.run();

        titleField.requestFocus();

        if (dialog.showAndWait().get() == ButtonType.OK)
            return new Book(titleField.getText(), authorField.getText(), Long.parseLong(isbnField.getText()), publisherField.getText(), Short.parseShort(publishedDateField.getText()), additionalInfoField.getText());

        return null;
    }

    public static String showScanIsbnDialog()
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Dialogs.ADD_BOOK_TEXT, Consts.Images.FETCH_HEADER_IMAGE, Consts.Paths.SCAN_ISBN_DIALOG);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        RestrictedTextField isbnField = (RestrictedTextField)dialog.getDialogPane().lookup(Consts.FxIds.ISBN_FIELD_ID);

        button.setDisable(true);

        isbnField.setRestriction(isbn -> isbn.matches("\\d*") && isbn.length() <= 13);
        isbnField.setSubmitRestriction(isbn -> isbn.length() == 10 || isbn.length() == 13);
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(!isbnField.isSubmitRestrictionMet()));
        isbnField.requestFocus();

        if (dialog.showAndWait().orElse(null) == ButtonType.OK)
            return isbnField.getText();

        return null;
    }

    public static Book showBookIsbnDialog(String isbn)
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Dialogs.ADD_BOOK_TEXT, Consts.Images.FETCH_HEADER_IMAGE, Consts.Paths.BOOK_ISBN_DIALOG);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        TextField isbnField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.ISBN_FIELD_ID);
        TextField titleField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.TITLE_FIELD_ID);
        TextField authorField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.AUTHOR_FIELD_ID);
        TextField publisherField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.PUBLISHER_FIELD_ID);
        RestrictedTextField publishedDateField = (RestrictedTextField)dialog.getDialogPane().lookup(Consts.FxIds.PUBLISHED_DATE_FIELD_ID);
        TextField additionalInfoField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.ADDITIONAL_INFO_FIELD_ID);

        button.setDisable(true);

        Runnable enableButton = () -> button.setDisable(titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty() || publisherField.getText().trim().isEmpty() || !publishedDateField.isSubmitRestrictionMet());

        isbnField.setText(isbn);

        publishedDateField.setRestriction(publishedDate -> publishedDate.matches("\\d*") && publishedDate.length() <= 4);
        publishedDateField.setSubmitRestriction(publishedDate -> publishedDate.length() == 4);

        authorField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publisherField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publishedDateField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());

        ProgressHelper.showDialog(Consts.Dialogs.FETCHING_INFORMATION_TITLE);

        GoogleBook.VolumeInfo volumeInfo = GoogleBooksHelper.getVolumeInfoFromIsbn(isbn);

        ProgressHelper.hideDialog();

        if (volumeInfo != null)
        {
            titleField.setText(volumeInfo.getTitle());
            authorField.setText(volumeInfo.getAuthors());
            publisherField.setText(volumeInfo.getPublisher());
            publishedDateField.setText(volumeInfo.getPublishedDate());

            enableButton.run();
        }

        if (dialog.showAndWait().get() == ButtonType.OK)
            return new Book(titleField.getText(), authorField.getText(), Long.parseLong(isbnField.getText()), publisherField.getText(), Short.parseShort(publishedDateField.getText()), additionalInfoField.getText());

        return null;
    }
}
