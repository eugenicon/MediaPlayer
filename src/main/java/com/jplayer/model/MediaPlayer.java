package com.jplayer.model;

import com.jplayer.media.MediaFile;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer.Status;

public class MediaPlayer {

    private javafx.scene.media.MediaPlayer player;
    private MediaFile nowPlayed;

    public void play(MediaFile mediaFile){
        releaseResources();
        startPlaying(mediaFile);
    }

    private void startPlaying(MediaFile mediaFile) {
        player = new javafx.scene.media.MediaPlayer(new Media(mediaFile.getURI()));
        player.play();
        nowPlayed = mediaFile;
    }

    private void releaseResources() {
        if (player != null) {
            if (player.getStatus() == Status.PLAYING) {
                player.stop();
            }
            player.dispose();
            player = null;
        }
    }

    public MediaFile getNowPlayed() {
        return nowPlayed;
    }
}
