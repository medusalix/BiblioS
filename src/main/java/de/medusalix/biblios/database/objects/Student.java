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

public class Student implements Searchable
{
    private long id;

    private String name, grade;
    
    private boolean hasBorrowedBooks;

    public Student() {}

    public Student(String name, String grade)
    {
        this.name = name;
        this.grade = grade;
    }
    
    @Override
    public boolean contains(String text)
    {
        boolean nameContains = getName().toLowerCase().contains(text);
        boolean gradeContains = getGrade() != null && getGrade().toLowerCase().contains(text);
        
        return nameContains || gradeContains;
    }
    
    @Override
    public String toString()
    {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", hasBorrowedBooks=" + hasBorrowedBooks +
                '}';
    }
    
    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getGrade()
    {
        return grade;
    }
    
    public boolean hasBorrowedBooks()
    {
        return hasBorrowedBooks;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }
    
    public void setHasBorrowedBooks(boolean hasBorrowedBooks)
    {
        this.hasBorrowedBooks = hasBorrowedBooks;
    }
}
