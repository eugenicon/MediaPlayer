package com.jplayer.view.controller.playlist;

import com.jplayer.model.MediaPlayer;
import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.fxml.FXMLLoader;
import org.kairos.core.Fragment;

@SceneContent("playlist")
public class PlayListController extends Fragment {

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);

    }

    private MediaPlayer getPlayer() {
        return ((AppActivity)getActivity()).getPlayer();
    }

}

