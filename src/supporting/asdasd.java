

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class asdasd extends Thread {

    private static TargetDataLine mic;
    private String audioName;

    public asdasd(String audioName) {
        this.audioName = audioName;
    }

    @Override
    public  void run() {
        initRecording();
        statRecording();
    }

    private void initRecording() {

        System.out.println("开始录音.....");

        try {
            //define audio format
            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

            mic = (TargetDataLine) AudioSystem.getLine(info);
            System.out.println("尼玛");
            mic.open();
            System.out.println("录音中......");
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }

    }

    private void statRecording() {
        try {
            mic.start();
            AudioInputStream audioInputStream = new AudioInputStream(mic);
            File f = new File(audioName);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, f);
            System.out.println("录音文件存储.....");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopRecording() {
        mic.stop();
        mic.close();
        System.out.println("录音结束....");
    }

    /**
     * pcm音频播放
     * @param file 文件路径
     */
    public void play(String file){
        try {
            System.out.println("开始播放.....");
            System.out.println(file);
            FileInputStream fis = new FileInputStream(file);
            AudioFormat.Encoding encoding =  new AudioFormat.Encoding("PCM_SIGNED");
            //编码格式，采样率，每个样本的位数，声道，帧长（字节），帧数，是否按big-endian字节顺序存储
            AudioFormat format = new AudioFormat(encoding,44100, 16, 2, 4, 44100 ,false);
            SourceDataLine auline = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            try {
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            auline.start();
            byte[] b = new byte[256];
            try {
                while(fis.read(b)>0) {
                    auline.write(b, 0, b.length);
                }
                auline.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
