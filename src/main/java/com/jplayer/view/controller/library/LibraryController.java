package com.jplayer.view.controller.library;

import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import org.kairos.core.Fragment;

@SceneContent("library")
public class LibraryController extends Fragment {

    @FXML
    private Node noContentNode;

    @FXML
    private FlowPane authorsPane;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        FxmlUtils.setupScene(fxmlLoader);

        if (authorsPane.getChildren().isEmpty()) {
            noContentNode.setVisible(true);
        }

    }

    @FXML
    public void onSpecifyPathToLibrary() {
        activity().getPager().setCurrentItem(SettingsController.class);
    }

    private AppActivity activity() {
        return (AppActivity) getActivity();
    }

}

