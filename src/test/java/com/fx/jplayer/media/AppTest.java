package com.fx.jplayer.media;

import com.jplayer.media.file.MediaFile;
import de.umass.lastfm.*;
import de.umass.lastfm.scrobble.ScrobbleResult;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
@RunWith(Parameterized.class)
public class AppTest
        extends TestCase {

    static {
        Logger loggerTagger = Logger.getLogger("org.jaudiotagger");
        loggerTagger.setLevel(Level.OFF);
        Logger loggerScrobbler = Logger.getLogger("de.umass.lastfm");
        loggerScrobbler.setLevel(Level.OFF);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testScrobbler() {

       /* Application name	jPlayer
        API key	a60e68714a3cdf0ae6a558ae64346e6e
        Shared secret	e76e4f99c6db7e5caee733157a873021
        Registered to	jespter*/

        String user = "jespter";
        String password = "s28e04a87";
        String key = "a60e68714a3cdf0ae6a558ae64346e6e";
        String secret = "e76e4f99c6db7e5caee733157a873021";
        Session session = Authenticator.getMobileSession(user, password, key, secret);
        //Update "now playing" status:
        ScrobbleResult result = Track.updateNowPlaying("Powerwolf", "We drink your blood", session);
        System.out.println("ok: " + (result.isSuccessful() && !result.isIgnored()));
        int now = (int) (System.currentTimeMillis() / 1000);
        //Scrobble track:
        result = Track.scrobble("Powerwolf", "We drink your blood", now, session);

        Artist artist = Artist.getInfo("Powerwolf", key);
        String imageURL = artist.getImageURL(ImageSize.SMALL);

        System.out.println("ok: " + (result.isSuccessful() && !result.isIgnored()));
        assertTrue(true);
    }

    public void testTagger() throws Exception {

        File testFile = new File("/com/fx/jplayer/media/data/Music/Powerwolf/2015 - Blessed and Possessed/CD 1/11 Let There Be Night.mp3");
        readProps(testFile);

    }

    private void readProps(File testFile) throws Exception {
        readMediaFile(testFile.toPath());
    }

    private Path readProps(Path testFile) {

        try {
            readProps(testFile.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testFile;
    }

    public void testSearch() throws Exception {
        Path start = Paths.get("/com/fx/jplayer/media/data/Music");
        String extensions = "wav|mp3|mp4";

        List<MediaFile> joined = Files.walk(start)
                .filter(path -> path.toString().matches(".*\\.(" + extensions + ")"))
                .map(this::readMediaFile)
                .collect(Collectors.toList());

        System.out.println("walk(): " + joined);

    }

    public MediaFile readMediaFile(Path path) {
        MediaFile mediaFile = null;

        try {
            AudioFile audioFile = AudioFileIO.read(path.toFile());
            Tag tag = audioFile.getTag();
            AudioHeader audioHeader = audioFile.getAudioHeader();

            mediaFile = new MediaFile(path);
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
