package com.jplayer.view.controller.library;

import com.jplayer.media.LastFMScrobbler;
import com.jplayer.media.file.MediaFile;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.controller.playlist.PlayListController;
import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.util.Key;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.ActivityFragment;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SceneContent("library")
public class LibraryController extends ActivityFragment<AppActivity> {

    @FXML
    private Node noContentNode;

    @FXML
    private FlowPane authorsPane;

    @Override
    protected void init() {
        activity().getMediaLibrary().addObserver((o, a) -> Platform.runLater(() -> {
            authorsPane.getChildren().clear();
            update();
        }));
        update();
    }

    private void update() {
        if (authorsPane.getChildren().isEmpty()) {
            Map<String, ImageView> authorImages = showAuthorNodes(getAuthors(activity().getMediaFiles()));
            noContentNode.setVisible(authorsPane.getChildren().isEmpty());
            new Thread(() -> {
                for (Map.Entry<String, ImageView> entry : authorImages.entrySet()) {
                    String url = LastFMScrobbler.getImage(entry.getKey());
                    if (!url.isEmpty()) {
                        entry.getValue().setImage(new Image(url));
                    }
                }
            }).start();
        }
    }

    private List<String> getAuthors(List<MediaFile> mediaFiles) {
        return mediaFiles.stream().map(MediaFile::getArtist)
                .distinct().sorted()
                .collect(Collectors.toList());
    }

    private Map<String, ImageView> showAuthorNodes(List<String> authors) {
        Map<String, ImageView> authorImages = new HashMap<>();
        authorsPane.getChildren().clear();

        String url = "com/jplayer/view/UnknownArtist.jpg";
        authors.forEach(author -> {
            ImageView pane = new ImageView(url);
            pane.setFitWidth(128);
            pane.setPreserveRatio(true);
            VBox vBox = new VBox(pane, new Label(author));
            vBox.setCursor(Cursor.HAND);
            vBox.setOnMouseClicked(e -> onAuthorSelected(author));
            authorImages.put(author, pane);
            authorsPane.getChildren().add(vBox);
        });
        return authorImages;
    }

    private void onAuthorSelected(String author) {
        FilteredList<MediaFile> filteredData = new FilteredList<>(activity().getMediaFiles(),
                file -> file.getArtist().equals(author));

        HashMap<String, List<MediaFile>> arguments = new HashMap<>();
        arguments.put(Key.Param.FILTERED_DATA, filteredData);

        activity().showPage(PlayListController.class, arguments);
    }

    @FXML
    public void onSpecifyPathToLibrary() {
        activity().showPage(SettingsController.class);
    }
}