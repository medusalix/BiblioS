package de.medusalix.biblios.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.medusalix.biblios.pojos.GoogleBook;
import de.medusalix.biblios.pojos.GoogleBookQuery;
import de.medusalix.biblios.utils.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GoogleBooks
{
    private static Logger logger = LogManager.getLogger(GoogleBooks.class);

    private static Gson gson = new GsonBuilder().create();

    public static String readApiKey()
    {
        Path apiKeyPath = java.nio.file.Paths.get(Consts.Paths.API_KEY);

        if (Files.exists(apiKeyPath))
        {
            try
            {
                return Files.readAllLines(apiKeyPath).get(0);
            }

            catch (IOException e)
            {
                ExceptionUtils.log(e);
            }
        }

        return null;
    }

    public static void saveApiKey(String apiKey)
    {
        try
        {
            Files.write(java.nio.file.Paths.get(Consts.Paths.API_KEY), apiKey.getBytes());
        }

        catch (IOException e)
        {
            ExceptionUtils.log(e);
        }
    }

    public static GoogleBook.VolumeInfo getVolumeInfoFromIsbn(String isbn)
    {
        String apiKey = readApiKey();

        try
        {
            URL volumeListUrl = new URL(String.format(Consts.GoogleBooks.GET_VOLUMES_BY_ISBN_URL, isbn, apiKey));

            try (BufferedReader volumeListReader = new BufferedReader(new InputStreamReader(volumeListUrl.openStream())))
            {
                List<GoogleBook> items = gson.fromJson(volumeListReader, GoogleBookQuery.class).getItems();

                if (items != null)
                {
                    URL volumeUrl = new URL(String.format(Consts.GoogleBooks.VOLUME_INFO_FIELDS_URL, items.get(0).getSelfLink(), apiKey));

                    try (BufferedReader volumeReader = new BufferedReader(new InputStreamReader(volumeUrl.openStream())))
                    {
                        logger.info("Google Books information fetched");

                        return gson.fromJson(volumeReader, GoogleBook.class).getVolumeInfo();
                    }
                }
            }
        }

        catch (IOException e)
        {
            ExceptionUtils.log(e);
        }

        return null;
    }
}