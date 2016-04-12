package com.jplayer.view.controller.library;

import com.jplayer.media.file.MediaFile;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.controller.playlist.PlayListController;
import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                .collect(Collectors.toMap(
                        author -> author,
                        this::getImage));
    }

    private void showAuthors(Map<String, String> authors) {

        authorsPane.getChildren().clear();

        for (Map.Entry<String, String> entry : authors.entrySet()) {
            Label label = new Label(entry.getKey());
            ImageView pane = new ImageView(entry.getValue());
            VBox vBox = new VBox();
            vBox.getChildren().addAll(pane, label);
            vBox.setMaxWidth(128);
            vBox.setCursor(Cursor.OPEN_HAND);
            vBox.setOnMouseClicked(e -> onAuthorSelected(entry));
            Platform.runLater(() -> authorsPane.getChildren().add(vBox));
        }
    }

    private void onAuthorSelected(Map.Entry<String, String> entry) {
        FilteredList<MediaFile> filteredData =
                new FilteredList<>(activity().getMediaFiles(),
                        file -> file.getArtist().equals(entry.getKey()));

        HashMap<String, List<MediaFile>> arguments = new HashMap<>();
        arguments.put("filteredData", filteredData);

        activity().getPager().setCurrentItem(PlayListController.class, arguments);
    }

    private String getImage(String author) {
        String key = "a60e68714a3cdf0ae6a558ae64346e6e";
        AdvancedArtistInfo artist = new AdvancedArtistInfo(Artist.getInfo(author, key));
        List<String> imageURLs = artist.getImageURLs();
        return imageURLs.get(imageURLs.size() - 3);
    }

    @FXML
    public void onSpecifyPathToLibrary() {
        activity().getPager().setCurrentItem(SettingsController.class);
    }

    private AppActivity activity() {
        return (AppActivity) getActivity();
    }

}

class AdvancedArtistInfo extends Artist {
    Artist artist;

    protected AdvancedArtistInfo(Artist artist) {
        super(artist.getName(), artist.getUrl());
        this.artist = artist;
    }

    public List<String> getImageURLs() {
        ArrayList<String> objects = new ArrayList<>();
        for (ImageSize size : ImageSize.values()) {
            String imageURL = artist.getImageURL(size);
            if (imageURL != null) {
                objects.add(imageURL);
            }
        }
        return objects;
    }
}

