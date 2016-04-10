package com.jplayer.media.audio;

import com.jplayer.view.util.ReflectionUtils;
import javazoom.jl.player.JavaSoundAudioDevice;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class JSoundAudioDevice extends JavaSoundAudioDevice {
    private FloatControl volControl;

    private FloatControl getVolControl() {
        if (volControl == null) {
            SourceDataLine source = ReflectionUtils.getObjectField(this, "source");
            assert source != null;
            volControl = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
        }
        return volControl;
    }

    private void setLineGain(float newValueInPercents) {
        float length = getVolControl().getMaximum() - getMinimum();
        float newValue = length / 100 * newValueInPercents + getMinimum();
        float newGain = Math.min(Math.max(newValue, getMinimum()), volControl.getMaximum());
        getVolControl().setValue(newGain);
    }

    private float getMinimum() {
        return (float) (getVolControl().getMinimum() / 1.4);
    }

    public void seekVolume(float gain) {
        setLineGain(getCurrentVolume() + gain * 100);
    }

    public void setVolume(float gain) {
        setLineGain(gain * 100);
    }

    public float getCurrentVolume() {
        float length = getVolControl().getMaximum() - getMinimum();
        return 100 / length * (getVolControl().getValue() - getMinimum());
    }

}
