package de.medusalix.biblios.managers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.helpers.RuntimeHelper;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.helpers.RuntimeHelper;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class UpdateManager
{
    private static int fetchVersion() throws IOException
    {
        try (Socket socket = NetworkManager.openSocket())
        {
            try (DataOutputStream outputStream = NetworkManager.prepareMessage(socket, NetworkManager.NetworkCode.FETCH_VERSION))
            {
                try (DataInputStream inputStream = NetworkManager.openInputStream(socket))
                {
                    return inputStream.readInt();
                }
            }
        }
    }

    private static void fetchUpdate() throws IOException, URISyntaxException
    {
        try (Socket socket = NetworkManager.openSocket())
        {
            try (DataOutputStream outputStream = NetworkManager.prepareMessage(socket, NetworkManager.NetworkCode.FETCH_UPDATE))
            {
                try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream()))
                {
                    try (BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(RuntimeHelper.getExecutableName())))
                    {
                        byte[] buffer = new byte[1024];

                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1)
                        {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        }
    }

    private static void restart() throws URISyntaxException, IOException
    {
        Runtime.getRuntime().exec("java -jar " + RuntimeHelper.getExecutableName());

        // System.exit is used for faster closing than Platform.exit
        System.exit(0);
    }

    public static void update()
    {
        // TODO change to client-only version
//        if (RuntimeHelper.isRelease())
//        {
//            try
//            {
//                int latestVersion = fetchVersion();
//                int currentVersion = Integer.parseInt(Consts.CURRENT_VERSION.replace(".", ""));
//
//                if (latestVersion > currentVersion)
//                {
//                    fetchUpdate();
//                    restart();
//                }
//            }
//
//            catch (IOException | URISyntaxException e)
//            {
//                ReportManager.reportException(e);
//            }
//        }
    }
}
