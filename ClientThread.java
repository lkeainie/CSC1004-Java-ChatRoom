import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.net.Socket;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.Base64;
import java.util.Scanner;

public class ClientThread extends Thread{
    /*
     * 客户端的接收消息线程，接收来自服务器的消息并将其打印到频幕上
     */
    private Socket socket = null;
    private ClientFrame clientFrame = null;
    private String username = null;
    //构造方法
    public ClientThread(Socket socket, ClientFrame clientFrame, String username) {
        this.socket = socket;
        this.clientFrame = clientFrame;
        this.username = username;
    }

    @Override
    public void run() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
            //持续接收服务器消息
            while(scanner.hasNextLine()) {
                String type = scanner.nextLine();
                //根据type选择方式
                //文本消息
                if(type.equals("MESSAGE")) {
                    String username_ = scanner.nextLine();
                    String dateTime = scanner.nextLine();
                    String message = scanner.nextLine();
                    JLabel senderLabel = new JLabel(" " + username_ + "   \t\t" + dateTime);
                    JLabel messageLabel = new JLabel("   " + message + "   ");
                    senderLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
                    messageLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
                    messageLabel.setBorder(BorderFactory.createLineBorder(new Color(127, 126, 126), 1));
                    messageLabel.setOpaque(true);
                    //自己的消息显示绿色，否则白色
                    messageLabel.setBackground(username.equals(username_) ? new Color(93, 215, 60) : new Color(255, 255, 255));
                    this.clientFrame.messageJP.add(senderLabel);//Noto Color Emoji
                    this.clientFrame.messageJP.add(messageLabel);
                }
                //图片
                if(type.equals("IMAGE")) {
                    String username_ = scanner.nextLine();
                    String dateTime = scanner.nextLine();
                    String message = scanner.nextLine();
                    //Base64解码
                    byte[] bytes = Base64.getMimeDecoder().decode(message);
                    for (int i = 0; i < bytes.length; ++i) {
                        if (bytes[i] < 0) bytes[i] += 256;
                    }
                    ImageIcon imageIcon = new ImageIcon(bytes);
                    //长宽修改适配屏幕
                    int width = imageIcon.getIconWidth();
                    int height = imageIcon.getIconHeight();
                    int [] res = changeSize(300, 250, width, height);
                    int reWidth = res[0], reHeight = res[1];
                    ImageIcon newImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(reWidth, reHeight, Image.SCALE_DEFAULT));

                    JLabel messageLabel = new JLabel("  ", newImageIcon, JLabel.LEFT);
                    messageLabel.setHorizontalTextPosition(JLabel.LEFT);
                    JLabel senderLabel = new JLabel(" " + username_ + "   \t\t" + dateTime);
                    senderLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));

                    this.clientFrame.messageJP.add(senderLabel);
                    this.clientFrame.messageJP.add(messageLabel);
                    //添加监听，鼠标点击放大，还要修改一下
                    messageLabel.addMouseListener(new imageClickListener(imageIcon));
                }
                //音频
                if(type.equals("VIDEO")) {
                    String username_ = scanner.nextLine();
                    String dateTime = scanner.nextLine();
                    String message = scanner.nextLine();
                    //Base64解码
                    byte[] bytes = Base64.getDecoder().decode(message);

                    JButton video = new JButton("        play        ");
                    video.setFont(new Font("微软雅黑", Font.BOLD, 16));
                    video.setContentAreaFilled(false);
                    video.setOpaque(true);
                    video.setBackground(username.equals(username_) ? new Color(93, 215, 60) : new Color(255, 255, 255));
                    video.setFocusPainted(false);
                    video.addActionListener(new videoPlayListener(bytes));

                    JLabel senderLabel = new JLabel(" " + username_ + "   \t\t" + dateTime);
                    senderLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));

                    this.clientFrame.messageJP.add(senderLabel);
                    this.clientFrame.messageJP.add(video);
                }
                //有用户登出
                if(type.equals("USERLOGIN")) {
                    String addUser = scanner.nextLine();
                    JLabel label = new JLabel("                                    " + addUser + " join the chat room");
                    label.setFont(new Font("微软雅黑", Font.PLAIN, 13));
                    this.clientFrame.messageJP.add(label);
                    clientFrame.defaultListModel.addElement(addUser);
                }
                //有用户登录
                if(type.equals("USERLOGOUT")) {
                    String removeUser = scanner.nextLine();
                    JLabel label = new JLabel("                                    " + removeUser + " leave the chat room");
                    label.setFont(new Font("微软雅黑", Font.PLAIN, 13));
                    this.clientFrame.messageJP.add(label);
                    clientFrame.defaultListModel.removeElement(removeUser);
                }
                //间隔
                this.clientFrame.messageJP.add(Box.createVerticalStrut(10));
                this.clientFrame.messageJP.revalidate();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {            
            try {
                if(scanner != null)
                scanner.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class videoPlayListener implements ActionListener {
        /*
        音频播放
         */
        private byte [] bytes;
        Boolean isPlaying = null;
        Clip clip = null;
        public videoPlayListener(byte [] bytes) { this.bytes = bytes; }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //根据是否正在播放，按下按钮会有不同反应
                isPlaying = isPlaying == null ? false : clip.isActive();
                if(!isPlaying) {
                    isPlaying = true;
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bais);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    (new Thread(() -> {
                        clip.start();
                    })).start();
                }else {
                    clip.stop();
                    isPlaying = false;
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }
    public int [] changeSize(int labelWidth, int labelHeight, int width, int height) {
        //图片压缩算法，好像有问题
        int [] res = new int[2];
        int reWidth, reHeight;
        if(width/height >= labelWidth/labelHeight){
            if(width > labelWidth){
                reWidth = labelWidth;
                reHeight = height * reWidth / width;
            }else{
                reWidth = width;
                reHeight = height;
            }
        }else{
            if(width > labelWidth){
                reHeight = labelHeight;
                reWidth = width * reHeight / height;
            }else{
                reWidth = width;
                reHeight = height;
            }
        }
        res[0] = reWidth;
        res[1] = reHeight;
        return res;
    }
    //图像点击查看
    public class imageClickListener implements MouseListener {
        ImageIcon imageIcon = null;
        public imageClickListener(ImageIcon imageIcon) {
            this.imageIcon = imageIcon;
        }
        public void mouseClicked(MouseEvent e) {
            JFrame jFrame = new JFrame();
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();
            int [] res = changeSize(1400, 1100, width, height);
            int reWidth = res[0], reHeight = res[1];
            jFrame.setSize(reWidth, reHeight);
            JLabel jLabel = new JLabel(new ImageIcon(imageIcon.getImage().getScaledInstance(reWidth, reHeight, Image.SCALE_DEFAULT)));
            jFrame.add(jLabel);
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
    }
}