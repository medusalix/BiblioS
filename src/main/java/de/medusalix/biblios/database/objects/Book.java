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

package de.medusalix.biblios.database.objects;

import de.medusalix.biblios.database.SearchHelper;
import javafx.beans.property.*;

public class Book implements SearchHelper.Searchable
{
    private long id;

    private StringProperty titleProperty = new SimpleStringProperty();
    private StringProperty authorProperty = new SimpleStringProperty();

    private LongProperty isbnProperty = new SimpleLongProperty();

    private StringProperty publisherProperty = new SimpleStringProperty();
    private IntegerProperty publishedDateProperty = new SimpleIntegerProperty();

    private StringProperty additionalInfoProperty = new SimpleStringProperty();
    
    private LongProperty borrowedByProperty = new SimpleLongProperty();

    public Book() {}

    public Book(String title, String author, long isbn, String publisher, int publishedDate, String additionalInfo)
    {
        titleProperty.set(title);
        authorProperty.set(author);

        isbnProperty.set(isbn);
        
        publisherProperty.set(publisher);
        publishedDateProperty.set(publishedDate);
        
        additionalInfoProperty.set(additionalInfo);
    }

    @Override
    public String[] getAttributes()
    {
        return new String[] {
            titleProperty.get(),
            authorProperty.get(),
            String.valueOf(isbnProperty.get()),
            publisherProperty.get(),
            String.valueOf(publishedDateProperty.get()),
            additionalInfoProperty.get()
        };
    }

    @Override
    public String toString()
    {
        return "Book{" +
            "id=" + id +
            ", title='" + titleProperty.get() + '\'' +
            ", author='" + authorProperty.get() + '\'' +
            ", isbn=" + isbnProperty.get() +
            ", publisher='" + publisherProperty.get() + '\'' +
            ", publishedDate=" + publishedDateProperty.get() +
            ", additionalInfo='" + additionalInfoProperty.get() + '\'' +
            ", borrowedBy=" + borrowedByProperty.get() +
            '}';
    }

    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return titleProperty.get();
    }

    public String getAuthor()
    {
        return authorProperty.get();
    }

    public long getIsbn()
    {
        return isbnProperty.get();
    }

    public String getPublisher()
    {
        return publisherProperty.get();
    }

    public int getPublishedDate()
    {
        return publishedDateProperty.get();
    }

    public String getAdditionalInfo()
    {
        return additionalInfoProperty.get();
    }

    public long getBorrowedBy()
    {
        return borrowedByProperty.get();
    }
    
    public void setId(long id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        titleProperty.set(title);
    }

    public void setAuthor(String author)
    {
        authorProperty.set(author);
    }

    public void setIsbn(long isbn)
    {
        isbnProperty.set(isbn);
    }

    public void setPublisher(String publisher)
    {
        publisherProperty.set(publisher);
    }

    public void setPublishedDate(int publishedDate)
    {
        publishedDateProperty.set(publishedDate);
    }

    public void setAdditionalInfo(String additionalInfo)
    {
        additionalInfoProperty.set(additionalInfo);
    }

    public void setBorrowedBy(long borrowedBy)
    {
        borrowedByProperty.set(borrowedBy);
    }

    public StringProperty titleProperty()
    {
        return titleProperty;
    }

    public StringProperty authorProperty()
    {
        return authorProperty;
    }

    public LongProperty isbnProperty()
    {
        return isbnProperty;
    }

    public StringProperty publisherProperty()
    {
        return publisherProperty;
    }

    public IntegerProperty publishedDateProperty()
    {
        return publishedDateProperty;
    }

    public StringProperty additionalInfoProperty()
    {
        return additionalInfoProperty;
    }

    public LongProperty borrowedByProperty()
    {
        return borrowedByProperty;
    }
}
