package com.jplayer.media.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.Duration;

@Getter
@Setter
@EqualsAndHashCode(of = "path")
public class MediaFile implements Serializable {

    private String path;
    private String artist;
    private String album;
    private String title;
    private int year;
    private int trackNumber;
    private Duration duration;

    public MediaFile(Path path) {
        this.path = path.toString();
    }

    public String getArtist() {
        return artist == null ? "" : artist;
    }

    public String getPrettyDuration() {
        if (duration == null) {
            return "0:00";
        }
        return String.format("%02d:%02d", duration.toMinutes(),
                duration.minusMinutes(duration.toMinutes()).getSeconds());
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", artist, title, getPrettyDuration());
    }
}