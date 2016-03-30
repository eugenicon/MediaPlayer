package com.jplayer.view.controller.fragments;

import com.jplayer.view.util.FxmlUtils;
import com.jplayer.view.util.SceneContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import org.kairos.core.Fragment;

import java.util.HashMap;

@SceneContent("app_fragment")
public class AppFragment extends Fragment {
    @FXML
    private Label label;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);

        HashMap arguments=getArguments();

        if(arguments!=null){
            if(arguments.containsKey("textLabel")){
                label.setText(arguments.get("textLabel").toString());
            }
        }
    }
}
