package com.jplayer.media.file;

import java.nio.file.Path;
import java.time.Duration;

public class MediaFile {

    private Path path;
    private String artist;
    private String album;
    private String title;
    private int year;
    private int trackNumber;
    private Duration duration;

    public MediaFile(Path path) {
        this.path = path;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getFulName() {
        return path.toString();
    }

    public String getURI() {
        return path.toUri().toString();
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