package com.jplayer.view.controller.settings;

import com.jplayer.media.LastFMScrobbler;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.Key;
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
        if (arguments.containsKey(Key.Param.CONNECTED)){
            connectionButton.setDisable(getArgument(Key.Param.CONNECTED));
        }
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