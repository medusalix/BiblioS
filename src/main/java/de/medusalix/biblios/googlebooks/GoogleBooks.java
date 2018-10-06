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
import de.medusalix.biblios.Consts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GoogleBooks
{
    private static final Logger logger = LogManager.getLogger(GoogleBooks.class);
    
    private static Gson gson = new GsonBuilder().create();

    public static void query(String isbn, Consumer<VolumeInfo> callback)
    {
        CompletableFuture.supplyAsync(new QueryTask(isbn))
            .thenAccept(callback);
    }

    private static List<GoogleBook> findBooksForIsbn(String apiKey, String isbn) throws IOException
    {
        URL url = new URL(String.format(Consts.GoogleBooks.VOLUMES_BY_ISBN_URL, isbn, apiKey));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())))
        {
            return gson.fromJson(reader, Query.class).getItems();
        }
    }

    private static VolumeInfo findVolumeInfoForBook(String apiKey, GoogleBook book) throws IOException
    {
        URL url = new URL(String.format(Consts.GoogleBooks.VOLUME_INFO_FIELDS_URL, book.getSelfLink(), apiKey));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())))
        {
            return gson.fromJson(reader, GoogleBook.class).getVolumeInfo();
        }
    }

    public static class QueryTask implements Supplier<VolumeInfo>
    {
        private String isbn;

        public QueryTask(String isbn)
        {
            this.isbn = isbn;
        }

        @Override
        public VolumeInfo get()
        {
            try
            {
                List<String> lines = Files.readAllLines(Consts.Paths.API_KEY);
                String apiKey = lines.get(0);

                List<GoogleBook> books = findBooksForIsbn(apiKey, isbn);

                if (books == null || books.size() == 0)
                {
                    logger.info("No books found for ISBN " + isbn);

                    return null;
                }

                logger.info("Found " + books.size() + " books for ISBN " + isbn);

                // Use the first result and fetch the volume info
                VolumeInfo volumeInfo = findVolumeInfoForBook(apiKey, books.get(0));

                logger.info("Using first result: " + volumeInfo);

                return volumeInfo;
            }

            catch (IOException e)
            {
                logger.error("", e);
            }

            return null;
        }
    }
}
