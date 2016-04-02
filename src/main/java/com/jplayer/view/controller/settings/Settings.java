package com.jplayer.view.controller.settings;


import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.FragmentPager;
import com.jplayer.view.util.widget.VerticalSlidingTabLayout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.kairos.core.Fragment;
import org.kairos.layouts.ViewPager;

import java.util.HashMap;

@SceneContent("settings")
public class Settings extends Fragment {

    @FXML
    private VerticalSlidingTabLayout tabLayout;
    @FXML
    private ViewPager viewPager;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {

        FxmlUtils.setupScene(fxmlLoader);

        if (getState() <= 1) {

            FragmentPager pagerAdapter = new FragmentPager(getFragmentManager());
            pagerAdapter.setContainer(viewPager);
            viewPager.setAdapter(pagerAdapter);

            HashMap args = new HashMap();
            args.put("lastFmConnected", false);

            pagerAdapter.addTab("LastFM",  LastFMSettings.class, args);
            tabLayout.setViewPager(viewPager);

            viewPager.setCurrentItem(0);
        }

    }

}

