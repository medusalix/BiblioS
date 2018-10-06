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
import de.medusalix.biblios.controls.RestrictedTextField;
import de.medusalix.biblios.database.objects.Book;
import de.medusalix.biblios.googlebooks.VolumeInfo;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookDialog extends FormDialog<Book>
{
    private Book book;

    private TextField titleField = (TextField)lookupNode(Consts.FxIds.TITLE_FIELD);
    private TextField authorField = (TextField)lookupNode(Consts.FxIds.AUTHOR_FIELD);
    private RestrictedTextField isbnField = (RestrictedTextField)lookupNode(Consts.FxIds.ISBN_FIELD);
    private TextField publisherField = (TextField)lookupNode(Consts.FxIds.PUBLISHER_FIELD);
    private RestrictedTextField publishedDateField = (RestrictedTextField)lookupNode(Consts.FxIds.PUBLISHED_DATE_FIELD);
    private TextField additionalInfoField = (TextField)lookupNode(Consts.FxIds.ADDITIONAL_INFO_FIELD);
    
    private Matcher isbnMatcher = Pattern.compile("[0-9]{0,13}").matcher("");
    private Matcher publishedDateMatcher = Pattern.compile("[0-9]{0,4}").matcher("");
    
    private BookDialog(Image image, String text)
    {
        super(Consts.Paths.BOOK_DIALOG, image, text);

        isbnField.setInputRestriction(isbn -> isbnMatcher.reset(isbn).matches());
        isbnField.setSubmitRestriction(isbn -> isbn.length() == 10 || isbn.length() == 13 || isbn.equals(Consts.Misc.ISBN_PLACEHOLDER));
        publishedDateField.setInputRestriction(publishedDate -> publishedDateMatcher.reset(publishedDate).matches());
        publishedDateField.setSubmitRestriction(publishedDate -> publishedDate.length() == 4);
        
        addTextFields(
            titleField,
            authorField,
            isbnField,
            publisherField,
            publishedDateField,
            additionalInfoField
        );
    }
    
    public BookDialog()
    {
        this(Consts.Images.ADD_DIALOG_HEADER, Consts.Dialogs.ADD_BOOK_TEXT);
    }
    
    public BookDialog(Book book)
    {
        this(Consts.Images.CHANGE_DIALOG_HEADER, Consts.Dialogs.CHANGE_BOOK_TEXT);

        this.book = book;

        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(String.valueOf(book.getIsbn()));
        publisherField.setText(book.getPublisher());
        publishedDateField.setText(String.valueOf(book.getPublishedDate()));
        additionalInfoField.setText(book.getAdditionalInfo());
    }
    
    public BookDialog(String isbn, VolumeInfo volumeInfo)
    {
        this(Consts.Images.ADD_DIALOG_HEADER, Consts.Dialogs.ADD_BOOK_TEXT);
    
        isbnField.setText(isbn);
        
        // If a book was found online
        if (volumeInfo != null)
        {
            titleField.setText(volumeInfo.getTitle());
            authorField.setText(volumeInfo.getAuthors());
            publisherField.setText(volumeInfo.getPublisher());
            publishedDateField.setText(volumeInfo.getPublishedDate());
        }
    }
    
    @Override
    protected boolean isFilled()
    {
        return !titleField.getText().trim().isEmpty()
            && !authorField.getText().trim().isEmpty()
            && isbnField.isSubmitRestrictionMet()
            && !publisherField.getText().trim().isEmpty()
            && publishedDateField.isSubmitRestrictionMet();
    }
    
    @Override
    protected Book getFormResult()
    {
        if (book == null)
        {
            book = new Book();
        }

        book.setTitle(titleField.getText());
        book.setAuthor(authorField.getText());
        book.setIsbn(Long.parseLong(isbnField.getText()));
        book.setPublisher(publisherField.getText());
        book.setPublishedDate(Short.parseShort(publishedDateField.getText()));
        book.setAdditionalInfo(additionalInfoField.getText());

        return book;
    }
}
