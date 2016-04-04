package com.jplayer.view.controller;

import com.jplayer.media.MediaFile;
import com.jplayer.media.MediaLibrary;
import com.jplayer.model.MediaPlayer;
import com.jplayer.view.controller.library.LibraryController;
import com.jplayer.view.controller.playlist.PlayListController;
import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.FragmentPager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private MediaPlayer player;

    private MediaLibrary mediaLibrary;

    public MediaPlayer getPlayer() {
        return player;
    }

    public MediaLibrary getMediaLibrary() {
        return mediaLibrary;
    }

    public ObservableList<MediaFile> getMediaFiles() {
        return (ObservableList<MediaFile>) mediaLibrary.getContent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FxmlUtils.setupScene(this);

        player = new MediaPlayer();
        mediaLibrary = new MediaLibrary(FXCollections.observableArrayList());
        mediaLibrary.addObserver((o, arg) -> System.out.println(arg));

        setupToolbar();
        setupPager();

    }

    private void setupToolbar() {
        toolbar.setTitle("jPlayer");
        toolbar.setDisplayHomeAsUpEnabled(true);

        setActionBar(toolbar);
    }

    private void setupPager() {
        FragmentPager pagerAdapter = new FragmentPager(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        pagerAdapter.addTab(new FragmentPager.Tab("Library",   LibraryController.class));
        pagerAdapter.addTab(new FragmentPager.Tab("Playlist",  PlayListController.class));
        pagerAdapter.addTab(new FragmentPager.Tab("Settings",  SettingsController.class));
        tabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(0);
    }

}
