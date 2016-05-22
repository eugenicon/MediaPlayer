package com.jplayer.view.controller.playlist;

import com.jplayer.media.file.MediaFile;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.kairos.core.Fragment;

import java.util.ListIterator;

@SceneContent("playlist")
public class PlayListController extends Fragment {

    @FXML
    private TableView<MediaFile> playList;
    private ObservableList<MediaFile> mediaFiles;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);

        ObservableList<MediaFile> filteredData;
        if (getArguments().containsKey("filteredData")) {
            filteredData = (ObservableList<MediaFile>) getArguments().get("filteredData");
        } else {
            filteredData = activity().getMediaFiles();
        }

        if (filteredData != mediaFiles) {
            setupPlaylist(filteredData);
        }
    }

    private void setupPlaylist(ObservableList<MediaFile> filteredData) {
        mediaFiles = filteredData;
        activity().setLibIterator(mediaFiles.iterator());

        playList.setItems(mediaFiles);
        playList.getColumns().forEach(column -> {
            column.setCellFactory(new FormattedTableCellFactory());
            column.setCellValueFactory(new PropertyValueFactory<>(column.getId()));
        });
    }

    private AppActivity activity() {
        return (AppActivity) getActivity();
    }

    @FXML
    public void onTrackClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1) {
            ListIterator<MediaFile> libIterator = mediaFiles.listIterator(playList.getSelectionModel().getFocusedIndex());
            activity().setLibIterator(libIterator);
            activity().getPlayer().play(libIterator.next());
        }
    }

    private class FormattedTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

        @Override
        public TableCell<S, T> call(TableColumn<S, T> p) {

            return new TableCell<S, T>() {

                private String nowPlayedStyle = "nowPlayed";

                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);

                    if (getTableRow() != null && !empty) {
                        getStyleClass().remove(nowPlayedStyle);

                        if (getTableRow().getItem() == activity().getPlayer().getNowPlayed()) {
                            getStyleClass().add(nowPlayedStyle);
                        }

                        setText(String.valueOf(item));
                    }
                }
            };
        }
    }
}

