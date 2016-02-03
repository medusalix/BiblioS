package de.medusalix.biblios.helpers;

import de.medusalix.biblios.core.BiblioS;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class RuntimeHelper
{
    public static boolean isRelease()
    {
        return System.getenv("debug") == null;
    }

    public static String getExecutableName() throws URISyntaxException
    {
        return Paths.get(BiblioS.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getFileName().toString();
    }
}
