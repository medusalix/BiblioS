package de.medusalix.biblios.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

public class Exceptions
{
    private static Logger logger = LogManager.getLogger(Exceptions.class);

    public static void log(Exception exception)
    {
        String message;

        if (exception instanceof DBIException)
        {
            message = "Database error";
        }

        else
        {
            message = "Error";
        }

        logger.error(message, exception);
    }
}