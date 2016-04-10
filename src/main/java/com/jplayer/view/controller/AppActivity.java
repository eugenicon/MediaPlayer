package com.jplayer.view.controller;

import com.jplayer.media.file.MediaFile;
import com.jplayer.media.library.MediaLibrary;
import com.jplayer.media.player.MediaPlayer;
import com.jplayer.view.controller.library.LibraryController;
import com.jplayer.view.controller.playlist.PlayListController;
import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.FragmentPager;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import org.kairos.Toolbar;
import org.kairos.core.Activity;
import org.kairos.layouts.SlidingTabLayout;

import java.time.Duration;

import static com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

@SceneContent("app_activity")
public class AppActivity extends Activity {

    @FXML
    private Toolbar toolbar;

    @FXML
    private SlidingTabLayout tabLayout;

    @FXML
    private org.kairos.layouts.ViewPager viewPager;

    @FXML
    private ProgressBar currentPosition;

    @FXML
    private Label labelCurrentPosition;

    @FXML
    private ProgressBar soundBar;

    private MediaPlayer player;

    private MediaLibrary mediaLibrary;

    @Override
    public void onCreate() {
        super.onCreate();
        FxmlUtils.setupScene(this);

        setupPlayer();
        setupLibrary();

        setupToolbar();
        setupPager();
    }

    private void setupLibrary() {
        mediaLibrary = new MediaLibrary(FXCollections.observableArrayList());
    }

    private void setupPlayer() {
        player = new MediaPlayer();
        player.setVolume(soundBar.getProgress());
        player.addTimeListener(this::onTimePlayedChanged);
        player.addStateListener(this::onPlayedTrackChanged);
    }

    private void setupToolbar() {
        toolbar.setTitle("jPlayer");
        toolbar.setDisplayHomeAsUpEnabled(true);
        setActionBar(toolbar);
    }

    private void onPlayedTrackChanged(PlayerStateEvent event) {
        setWindowTitle(player.getNowPlayed().toString());
    }

    private void onTimePlayedChanged(double duration) {
        currentPosition.setProgress(duration / player.getTotalDuration());
        Duration duration1 = Duration.ofMillis((long) duration);
        Platform.runLater(() -> labelCurrentPosition.setText(String.format("%02d:%02d", duration1.toMinutes(),
                duration1.minusMinutes(duration1.toMinutes()).getSeconds())));
    }

    private void setWindowTitle(String title) {
        Stage window = (Stage) toolbar.getScene().getWindow();
        window.setTitle(title);
    }

    private void setupPager() {
        FragmentPager pagerAdapter = new FragmentPager(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        pagerAdapter.addTab("Library", LibraryController.class);
        pagerAdapter.addTab("Playlist", PlayListController.class);
        pagerAdapter.addTab("Settings", SettingsController.class);

        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(0);

    }

    @FXML
    public void onSoundScroll(ScrollEvent event) {
        if (event.getDeltaY() < 0) {
            seekVolume(-0.05, soundBar);
        } else {
            seekVolume(0.05, soundBar);
        }
    }

    @FXML
    public void onSoundClicked(MouseEvent event) {
        player.setVolume(event.getX() / soundBar.getWidth());
        soundBar.setProgress(player.getVolume());
    }

    @FXML
    public void onPlayButtonClicked(ActionEvent event) {
        if (player.getState().equals(PlayerState.PAUSED)) {
            player.play();
        } else {
            player.pause();
        }
    }

    private void seekVolume(double value, ProgressBar soundBar) {
        player.seekVolume(value);
        soundBar.setProgress(player.getVolume());
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public MediaLibrary getMediaLibrary() {
        return mediaLibrary;
    }

    public ObservableList<MediaFile> getMediaFiles() {
        return (ObservableList<MediaFile>) mediaLibrary.getContent();
    }

}
