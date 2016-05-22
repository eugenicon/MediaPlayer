package com.jplayer;

import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.FxmlUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class Bootstrap extends Application {

    public static void main( String[] args ){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FxmlUtils.addIcon(stage, "ico/main16.png", "ico/main32.png", "ico/main48.png");
        FxmlUtils.startActivity(stage, AppActivity.class);
        FxmlUtils.addAppToTray(stage);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }
}