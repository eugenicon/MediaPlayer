package com.jplayer.view.controller.settings;

import com.jplayer.media.file.MediaReader;
import com.jplayer.media.library.MediaLibrary;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.ActivityFragment;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;

@SceneContent("generalSettings")
public class GeneralSettings extends ActivityFragment<AppActivity> {

    @FXML
    TextField pathToLibraryField;

    @FXML
    public void selectLibraryRoot(){
        DirectoryChooser chooser = new DirectoryChooser();
        File root = chooser.showDialog(getScene().getWindow());
        if (root != null) {
            pathToLibraryField.setText(root.getPath());
        }
    }

    @FXML
    public void rescanLibrary(){
        new Thread(() -> {
            MediaLibrary mediaLibrary = activity().getMediaLibrary();
            mediaLibrary.clear();
            mediaLibrary.addAll(MediaReader.readMedia(pathToLibraryField.getText()));
            mediaLibrary.saveSettings("./lib.jml");
        }).start();
    }
}