package com.jplayer.view.controller;

import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.controller.library.LibraryController;
import com.jplayer.view.util.widget.FragmentPager;
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
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        System.out.println("onStop");
        super.onStop();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FxmlUtils.setupScene(this);

        toolbar.setTitle("jPlayer");
        toolbar.setDisplayHomeAsUpEnabled(true);

        setActionBar(toolbar);

        FragmentPager pagerAdapter = new FragmentPager(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        pagerAdapter.addTab(new FragmentPager.Tab("Library",   LibraryController.class));
        pagerAdapter.addTab(new FragmentPager.Tab("Settings",  SettingsController.class));
        tabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(0);


    }

    public void setTitle(String title){
        toolbar.setTitle(title);
    }


}
