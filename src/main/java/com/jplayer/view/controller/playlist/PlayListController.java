package com.jplayer.view.controller.playlist;

import com.jplayer.media.file.MediaFile;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.kairos.core.Fragment;

@SceneContent("playlist")
public class PlayListController extends Fragment {

    @FXML
    private TableView<MediaFile> playList;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);

        ObservableList<MediaFile> mediaFiles;
        if (getArguments().containsKey("filteredData")) {
            mediaFiles = (ObservableList<MediaFile>) getArguments().get("filteredData");
        } else {
            mediaFiles = activity().getMediaFiles();
        }
        playList.setItems(mediaFiles);
        playList.getColumns().forEach(
                c -> c.setCellValueFactory(new PropertyValueFactory<>(c.getId())));

    }

    private AppActivity activity() {
        return (AppActivity) getActivity();
    }

    @FXML
    public void onTrackClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1) {
            activity().getPlayer().play(playList.getSelectionModel().getSelectedItem());
        }
    }

}

