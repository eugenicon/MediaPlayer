package com.jplayer.view.controller.library;

import com.jplayer.media.LastFMScrobbler;
import com.jplayer.media.file.MediaFile;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.controller.playlist.PlayListController;
import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.kairos.core.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SceneContent("library")
public class LibraryController extends Fragment {

    @FXML
    private Node noContentNode;

    @FXML
    private FlowPane authorsPane;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);

        if (authorsPane.getChildren().isEmpty()) {
            Map<String, String> authorImages = prepareAuthorImages(activity().getMediaFiles());
            showAuthors(authorImages);
            noContentNode.setVisible(authorImages.isEmpty());
        }
    }

    private Map<String, String> prepareAuthorImages(List<MediaFile> mediaFiles) {
        return mediaFiles.stream()
                .map(MediaFile::getArtist)
                .distinct().sorted()
                .collect(Collectors.toMap(author -> author, LastFMScrobbler::getImage,
                        (k, v) -> {
                            throw new RuntimeException(String.format("Duplicate key %s", k));
                        }
                        , TreeMap::new));
    }

    private void showAuthors(Map<String, String> authors) {

        authorsPane.getChildren().clear();

        authors.entrySet().forEach(entry -> {
            String url = entry.getValue().isEmpty() ? "com/jplayer/view/UnknownArtist.jpg" : entry.getValue();
            Label label = new Label(entry.getKey());
            ImageView pane = new ImageView(url);
            pane.setFitWidth(128);
            pane.setPreserveRatio(true);
            VBox vBox = new VBox();
            vBox.getChildren().addAll(pane, label);
            vBox.setCursor(Cursor.HAND);
            vBox.setOnMouseClicked(e -> onAuthorSelected(entry));
            Platform.runLater(() -> authorsPane.getChildren().add(vBox));
        });
    }

    private void onAuthorSelected(Map.Entry<String, String> entry) {
        FilteredList<MediaFile> filteredData =
                new FilteredList<>(activity().getMediaFiles(),
                        file -> file.getArtist().equals(entry.getKey()));

        HashMap<String, List<MediaFile>> arguments = new HashMap<>();
        arguments.put("filteredData", filteredData);

        activity().getPager().setCurrentItem(PlayListController.class, arguments);
    }

    @FXML
    public void onSpecifyPathToLibrary() {
        activity().getPager().setCurrentItem(SettingsController.class);
    }

    private AppActivity activity() {
        return (AppActivity) getActivity();
    }
}