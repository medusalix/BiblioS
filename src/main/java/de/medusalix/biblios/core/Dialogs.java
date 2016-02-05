package de.medusalix.biblios.core;

import de.medusalix.biblios.controls.RestrictedTextField;
import de.medusalix.biblios.dto.Book;
import de.medusalix.biblios.dto.Student;
import de.medusalix.biblios.helpers.DialogHelper;
import de.medusalix.biblios.helpers.GoogleBooksHelper;
import de.medusalix.biblios.helpers.ProgressHelper;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import de.medusalix.biblios.pojos.*;

public class Dialogs
{
    public static String showReportProblemDialog()
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Messages.REPORT_PROBLEM_TEXT, Consts.Images.REPORT_PROBLEM_HEADER_IMAGE, Consts.Resources.REPORT_PROBLEM_DIALOG_PATH);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        TextArea problemArea = (TextArea)dialog.getDialogPane().lookup(Consts.FxIds.PROBLEM_AREA_ID);

        button.setDisable(true);

        problemArea.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(problemArea.getText().trim().isEmpty()));

        if (dialog.showAndWait().get() == ButtonType.OK)
        {
            return problemArea.getText();
        }

        return null;
    }

    public static String showSuggestFeatureDialog()
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Messages.SUGGEST_FEATURE_TEXT, Consts.Images.SUGGEST_FEATURE_HEADER_IMAGE, Consts.Resources.SUGGEST_FEATURE_DIALOG_PATH);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        TextArea featureArea = (TextArea)dialog.getDialogPane().lookup(Consts.FxIds.FEATURE_AREA_ID);

        button.setDisable(true);

        featureArea.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(featureArea.getText().trim().isEmpty()));

        if (dialog.showAndWait().get() == ButtonType.OK)
        {
            return featureArea.getText();
        }

        return null;
    }

    public static String showPasswordDialog()
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Messages.PASSWORD_REQUIRED_TEXT, Consts.Images.PASSWORD_HEADER_IMAGE, Consts.Resources.PASSWORD_DIALOG_PATH);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        PasswordField passwordField = (PasswordField)dialog.getDialogPane().lookup(Consts.FxIds.PASSWORD_FIELD_ID);

        button.setDisable(true);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(passwordField.getText().trim().isEmpty()));
        passwordField.requestFocus();

        if (dialog.showAndWait().get() == ButtonType.OK)
        {
            return passwordField.getText();
        }

        return null;
    }

    public static Student showStudentDialog(String text, Image image, StudentListItem initialValues)
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(text, image, Consts.Resources.STUDENT_DIALOG_PATH);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        TextField nameField = (TextField)dialog.getDialogPane().lookup(Consts.FxIds.NAME_FIELD_ID);
        RestrictedTextField gradeField = (RestrictedTextField)dialog.getDialogPane().lookup(Consts.FxIds.GRADE_FIELD_ID);

        button.setDisable(true);

        Runnable enableButton = () -> button.setDisable(nameField.getText().trim().isEmpty() || gradeField.getText().trim().isEmpty());

        if (initialValues != null)
        {
            nameField.setText(initialValues.getName());
            gradeField.setText(initialValues.getGrade());
        }

        gradeField.setRestriction(keyEvent -> gradeField.getLength() < 3);

        nameField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        gradeField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());

        enableButton.run();

        nameField.requestFocus();

        if (dialog.showAndWait().get() == ButtonType.OK)
        {
            return new Student(nameField.getText(), gradeField.getText());
        }

        return null;
    }

    public static Book showBookDialog(String text, Image image, BookTableItem initialValues)
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(text, image, Consts.Resources.BOOK_DIALOG_PATH);

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
            isbnField.setText(initialValues.getIsbn());
            publisherField.setText(initialValues.getPublisher());
            publishedDateField.setText(initialValues.getPublishedDate());
            additionalInfoField.setText(initialValues.getAdditionalInfo());
        }

        isbnField.setRestriction(isbn -> isbn.matches("\\d*") && isbnField.getLength() < 13);
        isbnField.setSubmitRestriction(aVoid -> isbnField.getText().trim().length() == 10 || isbnField.getText().trim().length() == 13 || isbnField.getText().equals(Consts.Misc.ISBN_PLACEHOLDER));
        publishedDateField.setRestriction(publishedDate -> publishedDate.matches("\\d*") && publishedDateField.getLength() < 4);
        publishedDateField.setSubmitRestriction(aVoid -> publishedDateField.getText().trim().length() == 4);

        titleField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        authorField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publisherField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publishedDateField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());

        enableButton.run();

        titleField.requestFocus();

        if (dialog.showAndWait().get() == ButtonType.OK)
        {
            return new Book(titleField.getText(), authorField.getText(), isbnField.getText(), publisherField.getText(), publishedDateField.getText(), additionalInfoField.getText());
        }

        return null;
    }

    public static String showScanIsbnDialog()
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Messages.ADD_BOOK_TEXT, Consts.Images.FETCH_HEADER_IMAGE, Consts.Resources.SCAN_ISBN_DIALOG_PATH);

        Node button = dialog.getDialogPane().lookupButton(ButtonType.OK);

        RestrictedTextField isbnField = (RestrictedTextField)dialog.getDialogPane().lookup(Consts.FxIds.ISBN_FIELD_ID);

        button.setDisable(true);

        isbnField.setRestriction(text -> text.matches("\\d*") && isbnField.getLength() < 13);
        isbnField.setSubmitRestriction(text -> text.length() == 10 || text.length() == 13);
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(!isbnField.isSubmitRestrictionMet()));
        isbnField.requestFocus();

        if (dialog.showAndWait().orElse(null) == ButtonType.OK)
        {
            return isbnField.getText();
        }

        return null;
    }

    public static Book showBookIsbnDialog(String isbn)
    {
        Dialog<ButtonType> dialog = DialogHelper.createStandardDialog(Consts.Messages.ADD_BOOK_TEXT, Consts.Images.FETCH_HEADER_IMAGE, Consts.Resources.BOOK_ISBN_DIALOG_PATH);

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

        publishedDateField.setRestriction(text -> text.matches("\\d*") && publishedDateField.getLength() < 4);
        publishedDateField.setSubmitRestriction(text -> text.trim().length() == 4);

        authorField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publisherField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());
        publishedDateField.textProperty().addListener((observable, oldValue, newValue) -> enableButton.run());

        ProgressHelper.showDialog(Consts.Messages.PROGRESS_FETCH_BOOK_INFO_TITLE);

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
        {
            return new Book(titleField.getText(), authorField.getText(), isbnField.getText(), publisherField.getText(), publishedDateField.getText(), additionalInfoField.getText());
        }

        return null;
    }
}
