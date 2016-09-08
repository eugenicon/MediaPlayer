package com.jplayer.media.library;

import com.jplayer.media.file.MediaFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collection;

@Getter
@ToString
@AllArgsConstructor
public class MediaLibraryAction {
    private Action action;
    private Collection<? extends MediaFile> mediaFiles;

    public MediaLibraryAction(Action action, MediaFile... mediaFiles) {
        this.mediaFiles = Arrays.asList(mediaFiles);
        this.action = action;
    }
}