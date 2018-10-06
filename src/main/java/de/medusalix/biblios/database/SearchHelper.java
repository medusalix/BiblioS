/*
 * Copyright (C) 2018 Medusalix
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

package de.medusalix.biblios.database;

import java.util.*;

public class SearchHelper<T extends SearchHelper.Searchable>
{
    private Map<T, List<String>> items = new HashMap<>();

    public void setObjects(Collection<T> objects)
    {
        items.clear();

        for (T object : objects)
        {
            List<String> words = new ArrayList<>();
            String[] attributes = object.getAttributes();

            for (String attribute : attributes)
            {
                if (attribute == null)
                {
                    continue;
                }

                // Remove special characters
                attribute = attribute.toLowerCase();
                attribute = attribute.replaceAll("[^a-z0-9äöüß\\s]", "");

                // Split all attributes into separate words
                words.addAll(Arrays.asList(attribute.split(" ")));
            }

            items.put(object, words);
        }
    }

    public boolean matches(T object, String input)
    {
        String[] inputWords = input.toLowerCase().split(" ");
        List<String> words = items.get(object);

        // If not in list
        if (words == null)
        {
            return false;
        }

        boolean matches = false;

        for (String inputWord : inputWords)
        {
            // Reset matching status
            matches = false;

            for (String word : words)
            {
                // Check if any word contains input
                if (word.contains(inputWord))
                {
                    matches = true;

                    break;
                }
            }

            // Instantly exit if input wasn't found
            if (!matches)
            {
                return false;
            }
        }

        return matches;
    }

    public interface Searchable
    {
        String[] getAttributes();
    }
}
