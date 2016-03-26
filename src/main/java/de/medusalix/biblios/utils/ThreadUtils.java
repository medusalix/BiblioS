package de.medusalix.biblios.utils;

public class ThreadUtils
{
    public static void runThreadAsDaemon(Runnable runnable)
    {
        Thread thread = new Thread(runnable);

        thread.setDaemon(true);
        thread.start();
    }
}
