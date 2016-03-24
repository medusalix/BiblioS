package de.medusalix.biblios.helpers;

public class Threads
{
    public static void runThreadAsDaemon(Runnable runnable)
    {
        Thread thread = new Thread(runnable);

        thread.setDaemon(true);
        thread.start();
    }
}
