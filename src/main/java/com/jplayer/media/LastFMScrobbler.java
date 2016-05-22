package com.jplayer.media;

import com.jplayer.media.file.MediaFile;
import de.umass.lastfm.*;

public class LastFMScrobbler {

    private static final String KEY = "1408f118d90bc596c411fa06022ed5ac";
    private static final String SECRET = "57122036ada9b418c1cc400296e300df";
    private static Session session;

    public static String getImage(String author) {
        if (author.isEmpty()) {
            return "";
        }
        Artist artistInfo = Artist.getInfo(author, KEY);
        return getImage(artistInfo, ImageSize.LARGE);
    }


    public static String getImage(String author, String album) {
        if (author.isEmpty() || album.isEmpty()) {
            return "";
        }
        Album albumInfo = Album.getInfo(author, album, KEY);
        return getImage(albumInfo, ImageSize.LARGE);
    }

    private static String getImage(MusicEntry entry, ImageSize size) {
        try {
            return entry.getImageURL(size);
        } catch (Exception e) {
            return "";
        }
    }

    public static Session initSession(String userName, String password) {
        session = Authenticator.getMobileSession(userName, password, KEY, SECRET);
        return session;
    }

    public static void scrobble(MediaFile mediafile) {
        if (session != null) {
            int now = (int) (System.currentTimeMillis() / 1000);
            Track.scrobble(mediafile.getArtist(), mediafile.getTitle(), now, session);
        }
    }

    public static void updatePlaying(MediaFile mediafile) {
        if (session != null) {
            Track.updateNowPlaying(mediafile.getArtist(), mediafile.getTitle(), session);
        }
    }
}