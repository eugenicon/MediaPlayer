package com.jplayer.view;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaViewHelper {

    public static void setup(FlowPane authorsPane) {
        List<String> authors = Arrays.asList("Powerwolf","We butter the bread with butter",
                "Inflames","Amorphis","Metallica","nazareth","system of a down",
                "Deftones","Doors","Diablo","Udo","Rage");
        String key = "a60e68714a3cdf0ae6a558ae64346e6e";

        authorsPane.getChildren().clear();
        authors.stream().limit(3).forEach(author -> {

            Label label = new Label(author);
            ImageView pane = new ImageView();

            try {
                AdvancedArtistInfo artist = new AdvancedArtistInfo(Artist.getInfo(author, key));
                label.setText(artist.getName());
                List<String> imageURLs = artist.getImageURLs();
                String imageURL = imageURLs.get(imageURLs.size()-3);
                System.out.println(imageURL);

                pane.setImage(new Image(imageURL));

            } catch (Exception e){
                e.printStackTrace();
            }

            VBox vBox = new VBox();
            vBox.getChildren().addAll(pane, label);
            vBox.setMaxWidth(128);

            authorsPane.getChildren().add(vBox);

            vBox.setCursor(Cursor.OPEN_HAND);
            vBox.setOnMouseClicked(e -> {
                System.out.println(author);
            });
        });

        System.out.println(authors);
    }

    static class AdvancedArtistInfo extends Artist{
        Artist artist;
        protected AdvancedArtistInfo(Artist artist) {
            super(artist.getName(), artist.getUrl());
            this.artist = artist;
        }

        public List<String> getImageURLs() {
            ArrayList<String> objects = new ArrayList<>();
            for (ImageSize size : ImageSize.values()) {
                String imageURL = artist.getImageURL(size);
                if (imageURL!=null){
                    objects.add(imageURL);
                }
            }
            return objects;
        }
    }

}
