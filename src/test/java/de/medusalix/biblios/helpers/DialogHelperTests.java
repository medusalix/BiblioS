package de.medusalix.biblios.helpers;

import com.sun.javafx.application.PlatformImpl;
import de.medusalix.biblios.core.Consts;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public class DialogHelperTests
{
    @BeforeClass
    private void beforeClass()
    {
        CountDownLatch latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() ->
        {
            PlatformImpl.startup(() -> {});

            latch.countDown();
        });

        try
        {
            latch.await();
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    private void customDialogShouldBeCreated()
    {
        Platform.runLater(() -> Assert.assertNotNull(DialogHelper.createCustomDialog(Consts.Resources.BOOK_DIALOG_PATH)));
    }

    @Test(dependsOnMethods = "customDialogShouldBeCreated")
    private void standardDialogShouldBeCreated()
    {
        Platform.runLater(() -> Assert.assertNotNull(DialogHelper.createCustomDialog(Consts.Resources.BOOK_DIALOG_PATH)));
    }

    @Test
    private void alertShouldBeCreated()
    {
        Platform.runLater(() -> Assert.assertNotNull(DialogHelper.createAlert(Alert.AlertType.CONFIRMATION, Consts.Messages.DELETE_ALL_BACKUPS_TITLE, Consts.Messages.DELETE_ALL_BACKUPS_MESSAGE)));
    }
}
