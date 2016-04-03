package com.jplayer.view.controller.library;

import com.jplayer.media.MediaFile;
import com.jplayer.media.MediaReader;
import com.jplayer.view.tmp.MediaViewHelper;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import org.kairos.core.Fragment;

import java.util.List;

@SceneContent("library")
public class LibraryController extends Fragment {

    @FXML
    private FlowPane authorsPane;

    private ScanService scanService;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (scanService == null){
            scanService = new ScanService();
            //scanService.start();
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

                    List<MediaFile> mediaFiles = new MediaReader().readMedia("/media/data/Music");

                    MediaViewHelper.setup(mediaFiles, authorsPane);

                    return true;
                }
            };
        }
    }

}

