package com.jplayer.view.controller;

import com.jplayer.view.MediaViewHelper;
import com.jplayer.view.util.FxmlUtils;
import com.jplayer.view.util.SceneContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import org.kairos.core.Fragment;

@SceneContent("main_activity")
public class MainActivity extends Fragment {

    @FXML
    private FlowPane authorsPane;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);
        if (authorsPane.getChildren().isEmpty()){
            MediaViewHelper.setup(authorsPane);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

}