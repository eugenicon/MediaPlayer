package com.jplayer.view.controller.settings;


import com.jplayer.media.file.MediaReader;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.kairos.core.Fragment;

import java.io.File;

@SceneContent("generalSettings")
public class GeneralSettings extends Fragment {

    @FXML
    TextField pathToLibraryField;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);
    }

    @FXML
    public void selectLibraryRoot(){
        DirectoryChooser chooser = new DirectoryChooser();
        File root = chooser.showDialog(this.getScene().getWindow());
        if (root != null) {
            pathToLibraryField.setText(root.getPath());
        }
    }

    @FXML
    public void rescanLibrary(){
        Thread thread = new Thread(() -> {
            new MediaReader(getApp().getMediaFiles()).readMedia(pathToLibraryField.getText());
            //File currentDir = new File(".");
            //getApp().getMediaLibrary().saveSettings(currentDir.getAbsolutePath() + "/lib.jml");
        });
        thread.start();
    }

    private AppActivity getApp() {
        return (AppActivity) getActivity();
    }

}

