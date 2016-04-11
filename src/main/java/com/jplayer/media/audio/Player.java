/*
 * 11/19/04		1.0 moved to LGPL.
 * 29/01/00		Initial version. mdm@techie.com
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

package com.jplayer.media.audio;

import javazoom.jl.decoder.JavaLayerException;

import java.io.InputStream;

public class Player extends javazoom.jl.player.Player {

    private JSoundAudioDevice audio;

    public Player(InputStream stream) throws JavaLayerException {
        this(stream, new JSoundAudioDevice());
    }

    public Player(InputStream stream, JSoundAudioDevice audio) throws JavaLayerException {
        super(stream, audio);
        this.audio = audio;
    }

    public void seekVolume(float volume) {
        audio.seekVolume(volume);
    }

    public float getCurrentVolume() {
        return audio.getCurrentVolume() / 100;
    }

    public void setVolume(float volume) {
        audio.setVolume(volume);
    }

    public void play(float volume) throws JavaLayerException {
        audio.setVolume(volume);
        super.play();
    }

}
