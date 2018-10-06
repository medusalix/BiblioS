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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student implements SearchHelper.Searchable
{
    private long id;

    private StringProperty nameProperty = new SimpleStringProperty();
    private StringProperty gradeProperty = new SimpleStringProperty();

    private BooleanProperty hasBorrowsProperty = new SimpleBooleanProperty();

    public Student() {}

    public Student(String name, String grade)
    {
        nameProperty.set(name);
        gradeProperty.set(grade);
    }

    @Override
    public String[] getAttributes()
    {
        return new String[] {
            nameProperty.get(),
            gradeProperty.get()
        };
    }

    @Override
    public String toString()
    {
        return "Student{" +
            "id=" + id +
            ", name='" + nameProperty.get() + '\'' +
            ", grade='" + gradeProperty.get() + '\'' +
            ", hasBorrows=" + hasBorrowsProperty.get() +
            '}';
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return nameProperty.get();
    }

    public String getGrade()
    {
        return gradeProperty.get();
    }

    public boolean getHasBorrows()
    {
        return hasBorrowsProperty.get();
    }
    
    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        nameProperty.set(name);
    }

    public void setGrade(String grade)
    {
        gradeProperty.set(grade);
    }

    public void setHasBorrows(boolean hasBorrows)
    {
        hasBorrowsProperty.set(hasBorrows);
    }

    public StringProperty nameProperty()
    {
        return nameProperty;
    }

    public StringProperty gradeProperty()
    {
        return gradeProperty;
    }

    public BooleanProperty hasBorrowsProperty()
    {
        return hasBorrowsProperty;
    }
}
