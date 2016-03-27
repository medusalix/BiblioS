package de.medusalix.biblios.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medusalix.biblios.pojos.GoogleBook;
import de.medusalix.biblios.pojos.GoogleBookQuery;
import de.medusalix.biblios.utils.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GoogleBooks
{
    private static Logger logger = LogManager.getLogger(GoogleBooks.class);

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

        try (InputStream queryInputStream = new URL(String.format(Consts.GoogleBooks.GET_VOLUMES_BY_ISBN_URL, isbn, apiKey)).openStream())
        {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            List<GoogleBook> items = mapper.readValue(queryInputStream, GoogleBookQuery.class).getItems();

            if (items != null)
            {
                try (InputStream volumeInputStream = new URL(String.format(Consts.GoogleBooks.VOLUME_INFO_FIELDS_URL, items.get(0).getSelfLink(), apiKey)).openStream())
                {
                    logger.info("Google Books information fetched");

                    return mapper.readValue(volumeInputStream, GoogleBook.class).getVolumeInfo();
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