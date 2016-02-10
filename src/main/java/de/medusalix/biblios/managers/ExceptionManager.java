package de.medusalix.biblios.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

public class ExceptionManager
{
    private static Logger logger = LogManager.getLogger(ExceptionManager.class);

    public static void log(Exception exception)
    {
        String message;

        if (exception instanceof DBIException)
            message = "Database error";

        else
            message = "Error";

        logger.error(message, exception);
    }
}
