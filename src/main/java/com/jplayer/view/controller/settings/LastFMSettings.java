package com.jplayer.view.controller.settings;


import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.kairos.core.Fragment;

import java.util.HashMap;

@SceneContent("lastFmSettings")
public class LastFMSettings extends Fragment {

    @FXML
    private TextField userField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button connectionButton;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);
        HashMap arguments = getArguments();
        if (arguments.containsKey("lastFmConnected")){
            boolean lastFmConnected = (boolean) arguments.get("lastFmConnected");
            connectionButton.setDisable(lastFmConnected);
        }

        arguments.forEach((k, v) -> System.out.printf("%s : %s%n", k, v));
    }

    public void connect(){
        System.out.println(userField.getText());
    }

    public void userCredinialsChanged(){
        System.out.println(userField.getText());
        System.out.println(passwordField.getText());
    }
}

