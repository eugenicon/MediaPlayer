package com.jplayer.media.player;

import com.jplayer.media.audio.Player;
import com.jplayer.media.file.MediaFile;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import javazoom.jl.decoder.JavaLayerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

public class MediaPlayer extends Observable {

    private MediaFile nowPlayed;
    private Player player;
    private Thread playerThread;
    private Thread statusThread;
    private double currentPosition;
    private double stoppedPosition;

    private List<PlayerStateListener> stateListeners;
    private List<PlayerTimeListener> timeListeners;
    private double totalDuration;
    private double volume;

    private PlayerState playerState;
    private int streamTotalLength;
    private int streamAvailableLeft;
    private InputStream stream;

    @SuppressWarnings({"InfiniteLoopStatement", "AccessStaticViaInstance"})
    public MediaPlayer() {
        playerState = PlayerState.STOPPED;
        statusThread = new Thread(() -> {
            while (true) {
                updateCurrentPosition();
                try {
                    statusThread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        statusThread.start();
    }

    public void addStateListener(PlayerStateObserver stateListener) {
        if (stateListeners == null) {
            stateListeners = new ArrayList<>();
        }
        stateListeners.add(stateListener);
    }

    public void addTimeListener(PlayerTimeListener timeListener) {
        if (timeListeners == null) {
            timeListeners = new ArrayList<>();
        }
        timeListeners.add(timeListener);
    }

    public void play(MediaFile mediaFile) {
        releaseResources();

        if (playerState.equals(PlayerState.PAUSED) && mediaFile.equals(nowPlayed)) {
            startPlaying(mediaFile, streamTotalLength - streamAvailableLeft);
        } else {
            startPlaying(mediaFile);
        }
    }

    private void startPlaying(MediaFile mediaFile) {
        stoppedPosition = 0;
        startPlaying(mediaFile, 0);
    }

    private void startPlaying(MediaFile mediaFile, int position) {
        totalDuration = mediaFile.getDuration().toMillis();
        try {
            playSource(mediaFile.getFulName(), position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        nowPlayed = mediaFile;
        setPlayerState(PlayerState.PLAYING);
    }

    private void playSource(String uri, int position) throws IOException, JavaLayerException {
        stream = new FileInputStream(uri);
        streamTotalLength = stream.available();
        if (position > 0) {
            stream.skip(position);
        }
        player = new Player(stream);
        currentPosition = player.getPosition();
        playerThread = new Thread(() -> {
            try {
                player.play((float) volume);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playerThread.start();
    }

    public double getCurrentPosition() {
        return currentPosition + stoppedPosition;
    }

    private void updateCurrentPosition() {
        if (playerState.equals(PlayerState.STOPPED)) {
            return;
        }
        double positionBeforeUpdate = getCurrentPosition();
        if (player != null) {
            currentPosition = player.getPosition();
        } else if (!playerState.equals(PlayerState.PAUSED)) {
            currentPosition = 0;
        }
        if (positionBeforeUpdate != getCurrentPosition()) {
            notifyTimeListeners(getCurrentPosition());
        }
    }

    private void notifyTimeListeners(double currentPosition) {
        if (timeListeners != null) {
            timeListeners.forEach(timeListener -> timeListener.onDurationChanged(currentPosition));
        }
    }

    private void notifyStateListeners(PlayerState playerState) {
        if (stateListeners != null) {
            stateListeners.forEach(stateListener -> stateListener.onPlaying(
                    new PlayerStateEvent(playerState, 0)));
        }
    }

    private void releaseResources() {
        if (player != null) {
            try {
                if (player.getPosition() == 0) {
                    streamAvailableLeft = 0;
                    stoppedPosition = 0;
                } else {
                    streamAvailableLeft = stream.available() + 14800;
                    stoppedPosition += player.getPosition();
                }
                player.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player = null;
        }
    }

    public MediaFile getNowPlayed() {
        return nowPlayed;
    }

    public double getTotalDuration() {
        return totalDuration;
    }

    public void seekVolume(double value) {
        if (player != null) {
            player.seekVolume((float) value);
            volume = player.getCurrentVolume();
        } else {
            volume = Math.min(Math.max(volume + value, 0), 1);
        }
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double value) {
        if (player != null) {
            player.setVolume((float) value);
            volume = player.getCurrentVolume();
        } else {
            volume = value;
        }
    }

    public void stop() {
        releaseResources();
        streamAvailableLeft = 0;
        setPlayerState(PlayerState.STOPPED);
    }

    public void pause() {
        if (player != null) {
            releaseResources();
            setPlayerState(PlayerState.PAUSED);
        }
    }

    private void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
        notifyStateListeners(this.playerState);
    }

    public PlayerState getState() {
        return playerState;
    }

    public void play() {
        if (nowPlayed != null && (playerState.equals(PlayerState.PAUSED) ||
                playerState.equals(PlayerState.STOPPED))) {
            play(nowPlayed);
        }
    }
}
