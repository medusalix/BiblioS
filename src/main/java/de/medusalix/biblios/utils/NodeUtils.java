/*
 * Copyright (C) 2016 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.medusalix.biblios.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class NodeUtils
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

        if (focusNode == null)
        {
            return;
        }
        
        if (focusNode instanceof TextField)
        {
            ((TextField)focusNode).selectAll();
        }

        else if (focusNode instanceof ComboBox<?>)
        {
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
        {
            focusNode.requestFocus();
        }
    }
}
