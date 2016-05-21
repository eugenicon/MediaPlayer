package com.jplayer.view.controller;

import com.jplayer.Bootstrap;
import com.jplayer.media.file.MediaFile;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class Popup {
    public static void show(MediaFile mediaFile) {
        Node node = null;
        try {
            node = FXMLLoader.load(Bootstrap.class.getResource("view/popup.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Notifications notification = Notifications.create()
                //.title("Title Text")
                //.text("text")
                .graphic(node)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(event -> System.out.println("Notification clicked on!"));
        notification.show();

        Label label = (Label) node.lookup("#label");
        label.setText(mediaFile.toString());

        ImageView imageView = (ImageView) node.getScene().lookup("#image");

    }

}
