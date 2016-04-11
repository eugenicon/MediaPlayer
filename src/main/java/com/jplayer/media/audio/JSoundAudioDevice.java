/*
 * 11/26/04		Buffer size modified to support JRE 1.5 optimizations.
 *              (CPU usage < 1% under P4/2Ghz, RAM < 12MB).
 *              jlayer@javazoom.net
 * 11/19/04		1.0 moved to LGPL.
 * 06/04/01		Too fast playback fixed. mdm@techie.com
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

import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDeviceBase;

import javax.sound.sampled.*;

public class JSoundAudioDevice extends AudioDeviceBase {

    private SourceDataLine source = null;

    private AudioFormat fmt = null;

    private byte[] byteBuf = new byte[4096];

    private float volume;

    private FloatControl volControl;

    protected AudioFormat getAudioFormat() {
        if (fmt == null) {
            Decoder decoder = getDecoder();
            fmt = new AudioFormat(decoder.getOutputFrequency(),
                    16,
                    decoder.getOutputChannels(),
                    true,
                    false);
        }
        return fmt;
    }

    protected DataLine.Info getSourceLineInfo() {
        AudioFormat fmt = getAudioFormat();
        return new DataLine.Info(SourceDataLine.class, fmt);
    }

    protected void createSource() throws JavaLayerException {
        Throwable t = null;
        try {
            Line line = AudioSystem.getLine(getSourceLineInfo());
            if (line instanceof SourceDataLine) {
                source = (SourceDataLine) line;
                //source.open(fmt, millisecondsToBytes(fmt, 2000));
                source.open(fmt);

                if (source.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    volControl = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
                    setLineGain(volume);
                }
                source.start();

            }
        } catch (RuntimeException | LinkageError | LineUnavailableException ex) {
            t = ex;
        }
        if (source == null) throw new JavaLayerException("cannot obtain source audio line", t);
    }

    protected void closeImpl() {
        if (source != null) {
            source.close();
        }
    }

    protected void writeImpl(short[] samples, int offs, int len) throws JavaLayerException {
        if (source == null)
            createSource();

        byte[] b = toByteArray(samples, offs, len);
        source.write(b, 0, len * 2);
    }

    protected byte[] getByteArray(int length) {
        if (byteBuf.length < length) {
            byteBuf = new byte[length + 1024];
        }
        return byteBuf;
    }

    protected byte[] toByteArray(short[] samples, int offs, int len) {
        byte[] b = getByteArray(len * 2);
        int idx = 0;
        short s;
        while (len-- > 0) {
            s = samples[offs++];
            b[idx++] = (byte) s;
            b[idx++] = (byte) (s >>> 8);
        }
        return b;
    }

    protected void flushImpl() {
        if (source != null) {
            source.drain();
        }
    }

    public int getPosition() {
        int pos = 0;
        if (source != null) {
            pos = (int) (source.getMicrosecondPosition() / 1000);
        }
        return pos;
    }

    private void setSourceVolume(float volume) {
        this.volume = volume;
        if (source != null) {
            FloatControl control = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(volume);
        }
    }

    private void setLineGain(float newValueInPercents) {
        float newGain = newValueInPercents;
        if (source != null) {
            float length = volControl.getMaximum() - getMinimum();
            float newValue = length / 100 * newValueInPercents + getMinimum();
            newGain = Math.min(Math.max(newValue, getMinimum()), volControl.getMaximum());
        }
        setSourceVolume(newGain);
    }

    private float getMinimum() {
        return (float) (volControl.getMinimum() / 1.4);
    }

    public void seekVolume(float gain) {
        setLineGain(getCurrentVolume() + gain * 100);
    }

    public void setVolume(float gain) {
        setLineGain(gain * 100);
    }

    public float getCurrentVolume() {
        float length = volControl.getMaximum() - getMinimum();
        return 100 / length * (volControl.getValue() - getMinimum());
    }

}
