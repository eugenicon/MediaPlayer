package com.jplayer.view.controller;

import com.jplayer.media.LastFMScrobbler;
import com.jplayer.media.file.MediaFile;
import com.jplayer.media.library.MediaLibrary;
import com.jplayer.media.player.MediaPlayer;
import com.jplayer.view.controller.library.LibraryController;
import com.jplayer.view.controller.playlist.PlayListController;
import com.jplayer.view.controller.settings.SettingsController;
import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.widget.FragmentPagerAdapter;
import com.jplayer.view.util.widget.ViewPager;
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
import java.util.Iterator;

import static com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

@SceneContent("app_activity")
public class AppActivity extends Activity {

    @FXML
    private Toolbar toolbar;

    @FXML
    private SlidingTabLayout tabLayout;

    @FXML
    private ViewPager viewPager;

    @FXML
    private ProgressBar currentPosition;

    @FXML
    private Label labelCurrentPosition;

    @FXML
    private ProgressBar soundBar;

    private MediaPlayer player;

    private MediaLibrary mediaLibrary;
    private Iterator<MediaFile> libIterator;

    @Override
    public void onCreate() {
        super.onCreate();
        FxmlUtils.setupScene(this);

        setupPlayer();
        setupLibrary();

        setupToolbar();
        setupPager();
    }

    public void setLibIterator(Iterator<MediaFile> libIterator) {
        this.libIterator = libIterator;
    }

    private void setupLibrary() {
        mediaLibrary = MediaLibrary.loadSettings("./lib.jml", FXCollections.observableArrayList());
        libIterator = mediaLibrary.iterator();
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
        if (event.getState().equals(PlayerState.PLAYING)) {
            LastFMScrobbler.scrobble(player.getNowPlayed());
            Platform.runLater(() -> Popup.show(player.getNowPlayed()));
        } else if (event.getState().equals(PlayerState.FINISHED)) {
            playNext();
        }
    }

    private void onTimePlayedChanged(double durationInMills) {
        currentPosition.setProgress(durationInMills / player.getTotalDuration());
        Duration duration = Duration.ofMillis((long) durationInMills);
        Platform.runLater(() -> labelCurrentPosition.setText(String.format("%02d:%02d", duration.toMinutes(),
                duration.minusMinutes(duration.toMinutes()).getSeconds())));
    }

    private void setWindowTitle(String title) {
        Stage window = (Stage) toolbar.getScene().getWindow();
        Platform.runLater(() -> window.setTitle(title));
    }

    private void setupPager() {
        FragmentPagerAdapter pager = new FragmentPagerAdapter(getFragmentManager());

        pager.addTab("Library", LibraryController.class);
        pager.addTab("Playlist", PlayListController.class);
        pager.addTab("Settings", SettingsController.class);

        viewPager.setAdapter(pager);
        viewPager.setCurrentItem(0);
        tabLayout.setViewPager(viewPager);
    }

    @FXML
    public void onSoundScroll(ScrollEvent event) {
        player.seekVolume(event.getDeltaY() < 0 ? -0.05 : 0.05);
        soundBar.setProgress(player.getVolume());
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

    @FXML
    public void onNextButtonClicked(ActionEvent event) {
        playNext();
    }

    private void playNext() {
        if (libIterator.hasNext()) {
            player.play(libIterator.next());
        }
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

    public ViewPager getPager() {
        return viewPager;
    }
}