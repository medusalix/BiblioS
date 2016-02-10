package de.medusalix.biblios.controllers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.ExceptionManager;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController
{
    @FXML
    private ImageView faviconView;

    @FXML
    private Label titleLabel, mainLabel;

    @FXML
    private TextFlow iconPackText;

    @FXML
    private void initialize()
    {
        FadeTransition faviconFadeTransition = new FadeTransition(Duration.seconds(1), faviconView);
        ScaleTransition faviconScaleTransition = new ScaleTransition(Duration.seconds(1), faviconView);

        faviconFadeTransition.setFromValue(0);
        faviconFadeTransition.setToValue(1);

        faviconScaleTransition.setFromX(0);
        faviconScaleTransition.setFromY(0);
        faviconScaleTransition.setToX(1);
        faviconScaleTransition.setToY(1);

        new ParallelTransition(faviconFadeTransition, faviconScaleTransition).play();

        FadeTransition titleFadeTransition = new FadeTransition(Duration.seconds(1), titleLabel);

        titleFadeTransition.setFromValue(0);
        titleFadeTransition.setToValue(1);
        titleFadeTransition.play();

        FadeTransition mainFadeTransition = new FadeTransition(Duration.seconds(1), mainLabel);

        mainFadeTransition.setFromValue(0);
        mainFadeTransition.setToValue(1);
        mainFadeTransition.play();

        FadeTransition iconPackTransition = new FadeTransition(Duration.seconds(1), iconPackText);

        iconPackTransition.setFromValue(0);
        iconPackTransition.setToValue(1);
        iconPackTransition.play();
    }

    @FXML
    private void onIconPackClick()
    {
        try
        {
            Desktop.getDesktop().browse(new URI(Consts.Paths.ICON_PACKAGE_URL));
        }

        catch (URISyntaxException | IOException e)
        {
            ExceptionManager.log(e);
        }
    }
}
