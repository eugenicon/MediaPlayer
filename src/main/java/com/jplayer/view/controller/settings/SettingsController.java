package com.jplayer.view.controller.settings;

import com.jplayer.view.controller.AppActivity;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.ActivityFragment;
import com.jplayer.view.util.widget.FragmentPagerAdapter;
import com.jplayer.view.util.widget.VerticalSlidingTabLayout;
import javafx.fxml.FXML;
import org.kairos.layouts.ViewPager;

import java.util.HashMap;

@SceneContent("settings")
public class SettingsController extends ActivityFragment<AppActivity> {

    @FXML
    private VerticalSlidingTabLayout tabLayout;
    @FXML
    private ViewPager viewPager;

    @Override
    protected void init() {
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getFragmentManager());
        pagerAdapter.setContainer(viewPager);

        HashMap<String, Boolean> args = new HashMap<>();
        args.put("lastFmConnected", false);

        pagerAdapter.addTab("General", GeneralSettings.class, args);
        pagerAdapter.addTab("LastFM",  LastFMSettings.class, args);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setViewPager(viewPager);
    }
}