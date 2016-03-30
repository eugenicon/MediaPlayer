package com.jplayer.media;

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

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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