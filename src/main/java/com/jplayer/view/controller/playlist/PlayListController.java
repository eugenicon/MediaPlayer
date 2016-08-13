package com.jplayer.view.controller.playlist;

import com.jplayer.media.file.MediaFile;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.ActivityFragment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.util.ListIterator;

@SceneContent("playlist")
public class PlayListController extends ActivityFragment<AppActivity> {

    @FXML
    private TableView<MediaFile> playList;
    private ObservableList<MediaFile> mediaFiles;

    @SuppressWarnings("unchecked")
    @Override
    protected void onShow() {
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

    @FXML
    public void onTrackClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1) {
            ListIterator<MediaFile> libIterator = mediaFiles.listIterator(playList.getSelectionModel().getFocusedIndex());
            activity().setLibIterator(libIterator);
            activity().getPlayer().play(libIterator.next());
        }
    }

    @FXML
    public void showInFolder(ActionEvent event) throws Exception {
        MediaFile mediaFile = playList.getSelectionModel().getSelectedItem();
        if (mediaFile != null) {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("explorer.exe /select," + mediaFile.getPath());
            } else {
                Desktop.getDesktop().open(new File(mediaFile.getPath()).getParentFile());
            }
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