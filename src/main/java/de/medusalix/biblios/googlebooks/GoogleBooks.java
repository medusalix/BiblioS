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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public class GoogleBooks
{
    private static final Logger logger = LogManager.getLogger(GoogleBooks.class);
    
    private static Gson gson = new GsonBuilder().create();
    
    public static void findVolumeInfoForIsbn(String isbn, Consumer<VolumeInfo> callback)
    {
        new Thread(() ->
        {
            try
            {
                String apiKey = FileUtils.readLine(Consts.Paths.API_KEY);
                
                List<Query.GoogleBook> books = findBooksForIsbn(apiKey, isbn);
                
                if (books == null || books.size() == 0)
                {
                    logger.info("No books found for ISBN " + isbn);
    
                    callback.accept(null);
    
                    return;
                }
                
                logger.info("Found " + books.size() + " books for ISBN " + isbn);
                
                // Use the first result and fetch the volume info
                VolumeInfo volumeInfo = findVolumeInfoForBook(apiKey, books.get(0));
                
                logger.info("Using first result: " + volumeInfo);
                        
                callback.accept(volumeInfo);
            }

            catch (IOException e)
            {
                logger.error("", e);
            }
        }).start();
    }
    
    private static List<Query.GoogleBook> findBooksForIsbn(String apiKey, String isbn) throws IOException
    {
        URL url = new URL(String.format(Consts.GoogleBooks.VOLUMES_BY_ISBN_URL, isbn, apiKey));
    
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())))
        {
            return gson.fromJson(reader, Query.class).getItems();
        }
    }
    
    private static VolumeInfo findVolumeInfoForBook(String apiKey, Query.GoogleBook book) throws IOException
    {
        URL url = new URL(String.format(Consts.GoogleBooks.VOLUME_INFO_FIELDS_URL, book.getSelfLink(), apiKey));
    
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())))
        {
            return gson.fromJson(reader, Query.GoogleBook.class).getVolumeInfo();
        }
    }
    
    private static class Query
    {
        private List<GoogleBook> items;
        
        public List<GoogleBook> getItems()
        {
            return items;
        }
        
        private static class GoogleBook
        {
            private String selfLink;
    
            private VolumeInfo volumeInfo;
            
            public String getSelfLink()
            {
                return selfLink;
            }
            
            public VolumeInfo getVolumeInfo()
            {
                return volumeInfo;
            }
        }
    }
}