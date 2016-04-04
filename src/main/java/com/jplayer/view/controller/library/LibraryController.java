package com.jplayer.view.controller.library;

import com.jplayer.media.MediaFile;
import com.jplayer.media.MediaReader;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.tmp.MediaViewHelper;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.kairos.core.Fragment;

import java.util.List;

@SceneContent("library")
public class LibraryController extends Fragment {

    @FXML
    Label noContentLabel;
    @FXML
    private FlowPane authorsPane;
    private ScanService scanService;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);
        if (getState() <= 1){
            scanService = new ScanService(((AppActivity) getActivity()).getMediaLibrary());
            scanService.start();
        }
    }


    class ScanService extends Service<Boolean> {

        private List<MediaFile> mediaFiles;

        public ScanService(List<MediaFile> mediaFiles) {
            super();
            this.mediaFiles = mediaFiles;

            this.setOnSucceeded((WorkerStateEvent event) -> {

                    }
            );
        }

        protected Task<Boolean> createTask() {

            return new Task<Boolean>() {
                protected Boolean call() {

                    new MediaReader(mediaFiles).readMedia("/media/data/Music");

                    MediaViewHelper.setup(mediaFiles, authorsPane);

                    return true;
                }
            };
        }
    }

}

