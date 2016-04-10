package com.jplayer.media.player;

import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;

public interface PlayerStateObserver extends PlayerStateListener {
    @Override
    default void onReady(PlayerStateEvent evt) {
    }

    @Override
    default void onStop(PlayerStateEvent evt) {
    }

    @Override
    default void onStall(PlayerStateEvent evt) {
    }

    @Override
    default void onHalt(PlayerStateEvent evt) {
    }

    @Override
    default void onPause(PlayerStateEvent evt) {
    }

    @Override
    default void onFinish(PlayerStateEvent evt) {
    }
}
