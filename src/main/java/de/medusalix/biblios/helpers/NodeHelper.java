package de.medusalix.biblios.helpers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class NodeHelper
{
    public static void blinkGreen(Node node, Node focusNode)
    {
        blink(node, "lime", focusNode);
    }

    public static void blinkGreen(Node node)
    {
        blinkGreen(node, null);
    }

    public static void blinkRed(Node node, Node focusNode)
    {
        blink(node, "red", focusNode);

        if (focusNode != null)
        {
            if (focusNode instanceof TextField)
                ((TextField)focusNode).selectAll();

            else if (focusNode instanceof ComboBox<?>)
                ((ComboBox<?>)focusNode).getEditor().selectAll();
        }
    }

    private static void blink(Node node, String color, Node focusNode)
    {
        node.setStyle(String.format("-fx-base: %s;", color));

        Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), event -> node.setStyle(null)));
        timeline.play();

        if (focusNode != null)
            focusNode.requestFocus();
    }
}
