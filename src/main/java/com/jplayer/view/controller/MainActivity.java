package com.jplayer.view.controller;

import com.jplayer.view.MediaViewHelper;
import com.jplayer.view.util.FxmlUtils;
import com.jplayer.view.util.SceneContent;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
            ScanService scanService = new ScanService();
            scanService.start();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    class ScanService extends Service<Boolean> {

        ScanService() {
            super();
            this.setOnSucceeded((WorkerStateEvent event) -> {


                    }
            );
        }

        protected Task<Boolean> createTask() {

            return new Task<Boolean>() {
                protected Boolean call() {

                    try {
                        Platform.runLater(()->MediaViewHelper.setup(authorsPane));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
            };
        }
    }

}

