package com.jplayer.view.util.widget;

import com.jplayer.view.util.fxml.FxmlUtils;
import javafx.fxml.FXMLLoader;
import org.kairos.core.Activity;
import org.kairos.core.Fragment;

public abstract class ActivityFragment<T extends Activity> extends Fragment {
    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        if (getState() <= 1) {
            FxmlUtils.setupScene(fxmlLoader);
            init();
        }
        onShow();
    }

    protected void onShow(){}

    protected void init(){}

    public T activity() {
        return (T) getActivity();
    }
}