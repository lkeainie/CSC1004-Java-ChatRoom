import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFrame {
    JButton sendB = null;
    JTextField inputTF = null;
    JPanel messageJP = null;
    JButton imageB = null;
    JButton voiceB = null;
    JButton logoutB = null;
    JScrollPane messageJSP = null;
    DefaultListModel defaultListModel = null;
    JFrame jFrame = null;

    JButton videoB = null;

    JButton emojiB = null;
    public ClientFrame(String username){
    //} public static void main(String[] args) {
        //创建背景Color
        final Color toolButtonColor = new Color(64, 65, 64, 216);
        final Color greenButtonColor = new Color(52, 169, 18, 216);
        final Color JPBackgroundColor = new Color(244, 244, 244);
        final Color BorderColor = new Color(206, 205, 205);
        //创建窗体，标题，大小，位置，不可缩放，关闭方式
        JFrame jFrame = new JFrame("Ichat");
        jFrame.setLayout(null);
        jFrame.setSize(705, 625);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("icon.jpg"));
        jFrame.setIconImage(imageIcon.getImage());

        //创建好友滚动面板内嵌面板

        DefaultListModel defaultListModel = new DefaultListModel();
        JList friendJL = new JList(defaultListModel);
        friendJL.setBackground(JPBackgroundColor);
        friendJL.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                super.setSelectionInterval(-1, -1);
            }
        });
        friendJL.setFont(new Font("Dialog", 0, 17));
        JScrollPane friendJSP = new JScrollPane(friendJL,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        friendJSP.setBorder(BorderFactory.createLineBorder(BorderColor, 1));
        friendJSP.setBounds(0, 160, 180, 455);
        //
        JLabel onlineList = new JLabel("Online User:");
        onlineList.setBounds(0, 130, 180, 30);
        onlineList.setFont(new Font("Dialog", 0, 19));

        //
        JLabel welcomeLabel = new JLabel("Welcome to Ichat!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", 1, 17));
        welcomeLabel.setBounds(0, 0, 180, 40);
        JLabel userLabel = new JLabel(username, SwingConstants.CENTER);
        userLabel.setFont(new Font("微软雅黑", 1, 20));
        userLabel.setBounds(0, 40, 180, 40);

        //创建输入文本域
        JTextField inputTF = new JTextField();
        inputTF.setBounds(180, 525, 430, 60);
        inputTF.setFont(new Font("Dialog", Font.PLAIN, 26));
        inputTF.setBorder(BorderFactory.createLineBorder(BorderColor, 1));
        inputTF.setBackground(JPBackgroundColor);
        //450 180
        //创建右下角发送按钮
        JButton sendB = new JButton("Send");
        sendB.setFont(new Font("微软雅黑", 0, 17));
        sendB.setBounds(610, 495, 80, 90);
        sendB.setContentAreaFilled(false);
        sendB.setForeground(greenButtonColor);
        sendB.setFocusPainted(false);

        //Logout
        JButton logoutB = new JButton("Logout");
        logoutB.setFont(new Font("微软雅黑", 0, 17));
        logoutB.setBounds(0, 80, 180, 50);
        logoutB.setContentAreaFilled(false);
        logoutB.setForeground(greenButtonColor);
        logoutB.setFocusPainted(false);

        //photo
        JButton imageB = new JButton("image");
        imageB.setFont(new Font("微软雅黑", 0, 17));
        imageB.setBounds(180, 495, 88, 30);
        imageB.setContentAreaFilled(false);
        imageB.setForeground(toolButtonColor);
        imageB.setFocusPainted(false);
        //emoji
        JButton emojiB = new JButton("emoji");
        emojiB.setFont(new Font("微软雅黑", 0, 17));
        emojiB.setBounds(268, 495, 88, 30);
        emojiB.setContentAreaFilled(false);
        emojiB.setForeground(toolButtonColor);
        emojiB.setFocusPainted(false);
        //voice
        JButton videoB = new JButton("video");
        videoB.setFont(new Font("微软雅黑", 0, 17));
        videoB.setBounds(356, 495, 88, 30);
        videoB.setContentAreaFilled(false);
        videoB.setForeground(toolButtonColor);
        videoB.setFocusPainted(false);

        //x
        JButton voiceB = new JButton("voice start");
        voiceB.setFont(new Font("微软雅黑", 0, 17));
        voiceB.setBounds(444, 495, 121, 30);
        voiceB.setContentAreaFilled(false);
        voiceB.setForeground(toolButtonColor);
        voiceB.setFocusPainted(false);

        //down
        JButton downB = new JButton("↓");
        downB.setFont(new Font("Dialog", 1, 20));
        downB.setBounds(565, 495, 45, 30);
        downB.setContentAreaFilled(false);
        downB.setForeground(toolButtonColor);
        downB.setFocusPainted(false);

        JPanel glue = new JPanel();
        glue.setBounds(180, 5, 10, 490);
        glue.setBackground(JPBackgroundColor);
        //创建右上方消息栏垂直Box面板
        JPanel messageJP = new JPanel();
        messageJP.setLayout(new BoxLayout(messageJP, BoxLayout.Y_AXIS));
        //创建消息栏滚动面板
        JScrollPane messageJSP = new JScrollPane(messageJP,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageJSP.setBounds(190, 5, 500, 490);
        messageJSP.setBorder(null);
        messageJP.setBackground(JPBackgroundColor);

        //全部纳入jFrame
        jFrame.add(onlineList);
        jFrame.add(glue);
        jFrame.add(friendJSP);
        jFrame.add(inputTF);
        jFrame.add(sendB);
        jFrame.add(messageJSP);
        jFrame.add(imageB);
        jFrame.add(logoutB);
        jFrame.add(emojiB);
        jFrame.add(videoB);
        jFrame.add(voiceB);
        jFrame.add(downB);
        jFrame.add(userLabel);
        jFrame.add(welcomeLabel);
        //现已加入豪华实例套餐

        JScrollBar jScrollBar = messageJSP.getVerticalScrollBar();
        downB.addActionListener(new downButtonListener(jScrollBar));

        //jFrame设为可视
        this.voiceB = voiceB;
        this.jFrame = jFrame;
        this.sendB = sendB;
        this.messageJSP = messageJSP;
        this.imageB = imageB;
        this.logoutB = logoutB;
        this.inputTF = inputTF;
        this.defaultListModel = defaultListModel;
        this.messageJP = messageJP;
        this.videoB = videoB;
        this.emojiB = emojiB;
        jFrame.setVisible(true);
    }
    //一键移动到最下的按钮
    public class downButtonListener implements ActionListener {
        private JScrollBar jScrollBar = null;

        public downButtonListener(JScrollBar jScrollBar) {
            this.jScrollBar = jScrollBar;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (jScrollBar != null) {
                jScrollBar.setValue(jScrollBar.getMaximum());
            }
        }
    }
}
