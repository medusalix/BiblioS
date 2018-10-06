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

package de.medusalix.biblios.googlebooks;

import java.util.List;

public class VolumeInfo
{
    private String title;
    
    private List<String> authors;
    
    private String publisher, publishedDate;

    @Override
    public String toString()
    {
        return "VolumeInfo{" +
            "title='" + title + '\'' +
            ", authors=" + authors +
            ", publisher='" + publisher + '\'' +
            ", publishedDate='" + publishedDate + '\'' +
            '}';
    }

    public String getTitle()
    {
        return title;
    }
    
    public String getAuthors()
    {
        return authors != null ? String.join(", ", authors) : null;
    }
    
    public String getPublisher()
    {
        return publisher;
    }
    
    public String getPublishedDate()
    {
        return publishedDate != null ? publishedDate.substring(0, 4) : null;
    }
}
