package de.medusalix.biblios.managers;

import de.medusalix.biblios.helpers.RuntimeHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class ReportManager
{
    private static void report(NetworkManager.NetworkCode networkCode, String text)
    {
        try (Socket socket = NetworkManager.openSocket())
        {
            try (DataOutputStream outputStream = NetworkManager.prepareMessage(socket, networkCode))
            {
                outputStream.writeUTF(text);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void report(NetworkManager.NetworkCode networkCode, String text, byte[] image)
    {
        try (Socket socket = NetworkManager.openSocket())
        {
            try (DataOutputStream outputStream = NetworkManager.prepareMessage(socket, networkCode))
            {
                outputStream.writeUTF(text);
                outputStream.writeInt(image.length);
                outputStream.write(image);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void reportException(Exception exception)
    {
        if (RuntimeHelper.isRelease())
        {
            try (StringWriter stringWriter = new StringWriter(); PrintWriter printWriter = new PrintWriter(stringWriter))
            {
                exception.printStackTrace(printWriter);

                Robot robot = new Robot();
                BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                ImageIO.write(image, "jpg", outputStream);

                report(NetworkManager.NetworkCode.REPORT_EXCEPTION, stringWriter.toString(), outputStream.toByteArray());
            }

            catch (IOException | AWTException e)
            {
                e.printStackTrace();
            }
        }

        else
        {
            exception.printStackTrace();
        }
    }

    public static void reportProblem(String text)
    {
        report(NetworkManager.NetworkCode.REPORT_PROBLEM, text);
    }

    public static void suggestFeature(String text)
    {
        report(NetworkManager.NetworkCode.SUGGEST_FEATURE, text);
    }
}
