package de.medusalix.biblios.managers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.core.Consts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkManager
{
    public enum NetworkCode
    {
        FETCH_VERSION(1),
        FETCH_UPDATE(2),
        REPORT_EXCEPTION(3),
        REPORT_PROBLEM(4),
        SUGGEST_FEATURE(5);

        private int code;

        NetworkCode(int code)
        {
            this.code = code;
        }

        public int getCode()
        {
            return code;
        }
    }

    public static Socket openSocket() throws IOException
    {
        return new Socket(Consts.Network.SERVER_HOSTNAME, Consts.Network.SERVER_PORT);
    }

    public static DataOutputStream prepareMessage(Socket socket, NetworkCode networkCode) throws IOException
    {
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        outputStream.writeInt(networkCode.getCode());
        outputStream.writeUTF(Consts.PROGRAM_NAME);

        return outputStream;
    }

    public static DataInputStream openInputStream(Socket socket) throws IOException
    {
        return new DataInputStream(socket.getInputStream());
    }
}
