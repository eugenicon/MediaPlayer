package com.jplayer.view.controller;

import com.jplayer.view.controller.fragments.Settings;
import com.jplayer.view.util.fxml.FragmentPager;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import javafx.fxml.FXML;
import org.kairos.Toolbar;
import org.kairos.core.Activity;
import org.kairos.layouts.SlidingTabLayout;

@SceneContent("app_activity")
public class AppActivity extends Activity{
    @FXML
    Toolbar toolbar;
    @FXML
    private SlidingTabLayout tabLayout;
    @FXML
    private org.kairos.layouts.ViewPager viewPager;

    @Override
    public void onCreate() {
        super.onCreate();
        FxmlUtils.setupScene(this);

        toolbar.setTitle("jPlayer");
        toolbar.setDisplayHomeAsUpEnabled(true);

        setActionBar(toolbar);

        FragmentPager pagerAdapter = new FragmentPager(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        pagerAdapter.addTab(new FragmentPager.Tab("Library",   MainActivity.class));
        pagerAdapter.addTab(new FragmentPager.Tab("Settings",  Settings.class));
        tabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(0);


    }

    public void setTitle(String title){
        toolbar.setTitle(title);
    }


}
