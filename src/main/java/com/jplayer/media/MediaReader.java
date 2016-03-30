package com.jplayer.media;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MediaReader {

    static {
        Logger loggerTagger = Logger.getLogger("org.jaudiotagger");
        loggerTagger.setLevel(Level.OFF);
    }

    public static final String DEFAULT_MEDIA_EXTENSION = "wav|mp3|mp4";

    public List<MediaFile> readMedia(Path root){
        return readMedia(root, DEFAULT_MEDIA_EXTENSION);
    }

    public List<MediaFile> readMedia(String root){
        return readMedia(Paths.get(root), DEFAULT_MEDIA_EXTENSION);
    }

    public List<MediaFile> readMedia(Path root, String extensions){

        List<MediaFile> mediaFiles;
        try {

            mediaFiles = Files.walk(root)
                    .filter(path -> path.toString().matches(".*\\.(" + extensions + ")"))
                    .map(this::readMediaFile)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            mediaFiles = Collections.emptyList();
        }

        return mediaFiles;
    }

    public MediaFile readMediaFile(Path path) {
        MediaFile mediaFile = new MediaFile(path);

        try {
            AudioFile audioFile = AudioFileIO.read(path.toFile());
            Tag tag = audioFile.getTag();
            AudioHeader audioHeader = audioFile.getAudioHeader();

            mediaFile.setDuration(Duration.ofSeconds(audioHeader.getTrackLength()));
            mediaFile.setTitle(tag.getFirst(FieldKey.TITLE));
            mediaFile.setArtist(tag.getFirst(FieldKey.ARTIST));
            mediaFile.setAlbum(tag.getFirst(FieldKey.ALBUM));
            mediaFile.setYear(Integer.parseInt(tag.getFirst(FieldKey.YEAR)));
            mediaFile.setTrackNumber(Integer.parseInt(tag.getFirst(FieldKey.TRACK)));

        } catch (Exception ignored) {

        }

        return mediaFile;
    }
}
