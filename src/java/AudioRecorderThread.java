

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorderThread extends Thread {

    private static TargetDataLine mic;
    private String audioName;
    public AudioRecorderThread(String audioName) {
        this.audioName = audioName;
    }

    @Override
    public void run() {
        initRecording();
        statRecording();
    }
    private void initRecording() {
        try {
            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            mic = (TargetDataLine) AudioSystem.getLine(info);
            mic.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void statRecording() {
        try {
            mic.start();
            AudioInputStream audioInputStream = new AudioInputStream(mic);
            File f = new File(audioName);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stopRecording() {
        mic.stop();
        mic.close();
    }
}