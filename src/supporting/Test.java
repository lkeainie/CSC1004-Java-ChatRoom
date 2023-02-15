
public class Test {
    public static void main(String[] args) throws InterruptedException {
        //新建录音线程，并存入test.wav文件里
        asdasd audioRecorder = new asdasd("test.wav");
        audioRecorder.start();
        //Thread.sleep(5000);//这里是设置录音的时长
        //audioRecorder.stopRecording();
        //audioRecorder.play("test.wav");//根据文件路径播放音频
    }
}
