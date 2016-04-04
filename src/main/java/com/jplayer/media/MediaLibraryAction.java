package com.jplayer.media;

import java.util.Arrays;
import java.util.Collection;

public class MediaLibraryAction {
    private Action action;
    private Collection<? extends MediaFile> mediaFiles;

    public MediaLibraryAction(Action action, MediaFile... mediaFiles) {
        this.mediaFiles = Arrays.asList(mediaFiles);
        this.action = action;
    }

    public MediaLibraryAction(Action action, Collection<? extends MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public Collection<? extends MediaFile> getMediaFiles() {
        return mediaFiles;
    }

    @Override
    public String toString() {
        return String.format("MediaLibraryAction{action=%s, mediaFiles=%s}", action, mediaFiles);
    }
}
