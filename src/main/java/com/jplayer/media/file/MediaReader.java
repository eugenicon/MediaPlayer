package com.jplayer.media.file;

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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MediaReader {

    public static final String DEFAULT_MEDIA_EXTENSION = "wav|mp3|mp4";

    static {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
    }

    public static List<MediaFile> readMedia(String root) {
        return readMedia(Paths.get(root), DEFAULT_MEDIA_EXTENSION);
    }

    public static List<MediaFile> readMedia(Path root, String extensions) {
        List<MediaFile> mediaFiles = new ArrayList<>();
        try {
            Files.walk(root)
                    .filter(path -> path.toString().matches(".*\\.(" + extensions + ")"))
                    .map(MediaReader::readMediaFile)
                    .collect(Collectors.toCollection(() -> mediaFiles));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mediaFiles;
    }

    private static MediaFile readMediaFile(Path path) {
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
            if (mediaFile.getTitle() == null) {
                mediaFile.setTitle(path.getFileName().toString());
            }
            if (mediaFile.getArtist() == null) {
                mediaFile.setArtist("");
            }
            if (mediaFile.getAlbum() == null) {
                mediaFile.setAlbum("");
            }
        }

        return mediaFile;
    }
}