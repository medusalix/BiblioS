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

public class Book implements Searchable
{
    private long id;

    private String title, author;
    private long isbn;
    
    private String publisher;
    private short publishedDate;
    
    private String additionalInfo;
    
    private long borrowedBy;

    public Book() {}

    public Book(String title, String author, long isbn, String publisher, short publishedDate, String additionalInfo)
    {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        
        this.additionalInfo = additionalInfo;
    }
    
    @Override
    public boolean contains(String text)
    {
        boolean titleContains = getTitle().toLowerCase().contains(text);
        boolean authorContains = getAuthor() != null && getAuthor().toLowerCase().contains(text);
        boolean isbnContains = String.valueOf(getIsbn()).toLowerCase().contains(text);
        boolean publisherContains = getPublisher() != null && getPublisher().toLowerCase().contains(text);
        boolean publishedDateContains = String.valueOf(getPublishedDate()).toLowerCase().contains(text);
        boolean additionalInfoContains = getAdditionalInfo() != null && getAdditionalInfo().toLowerCase().contains(text);
        
        return titleContains || authorContains || isbnContains || publisherContains || publishedDateContains || additionalInfoContains;
    }
    
    @Override
    public String toString()
    {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn=" + isbn +
                ", publisher='" + publisher + '\'' +
                ", publishedDate=" + publishedDate +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", borrowedBy=" + borrowedBy +
                '}';
    }
    
    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public long getIsbn()
    {
        return isbn;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public short getPublishedDate()
    {
        return publishedDate;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
    }

    public long getBorrowedBy()
    {
        return borrowedBy;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setIsbn(long isbn)
    {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public void setPublishedDate(short publishedDate)
    {
        this.publishedDate = publishedDate;
    }

    public void setAdditionalInfo(String additionalInfo)
    {
        this.additionalInfo = additionalInfo;
    }
    
    public void setBorrowedBy(long borrowedBy)
    {
        this.borrowedBy = borrowedBy;
    }
}
