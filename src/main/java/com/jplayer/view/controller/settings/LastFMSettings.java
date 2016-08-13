package com.jplayer.view.controller.settings;

import com.jplayer.media.LastFMScrobbler;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.ActivityFragment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.HashMap;

@SceneContent("lastFmSettings")
public class LastFMSettings extends ActivityFragment<AppActivity> {

    @FXML
    private TextField userField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button connectionButton;

    @Override
    protected void onShow() {
        HashMap arguments = getArguments();
        if (arguments.containsKey("lastFmConnected")){
            boolean lastFmConnected = (boolean) arguments.get("lastFmConnected");
            connectionButton.setDisable(lastFmConnected);
        }

        arguments.forEach((k, v) -> System.out.printf("%s : %s%n", k, v));
    }

    @FXML
    public void connect(){
        LastFMScrobbler.initSession(userField.getText(), passwordField.getText());
    }

    @FXML
    public void userCredentialsChanged(){
        System.out.println(userField.getText());
        System.out.println(passwordField.getText());
    }
}