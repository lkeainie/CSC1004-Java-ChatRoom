import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerFrame {
    public ServerFrame() {}
    public void createFrame() {
        /*
         * 创建一个服务器窗口，允许输入ip和端口(还没写^_^，目前固定)，开启和关闭服务器
        */
        // 创建窗体 Ichat
        JFrame jFrame = new JFrame("Ichat");
        jFrame.setSize(500, 300);
        jFrame.setResizable(false);  //不可伸缩
        jFrame.setLocationRelativeTo(null);  //居中
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("icon.jpg"));
        jFrame.setIconImage(imageIcon.getImage());
        // 创建面板
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);

        // 创建ip标签
        JLabel userLabel = new JLabel("    IP : ");
        userLabel.setBounds(70, 30, 150, 40);
        userLabel.setFont(new Font("微软雅黑", 0, 20));

        // 创建端口标签
        JLabel passwordLabel = new JLabel("Port : ");
        passwordLabel.setBounds(70, 100, 150, 40);
        passwordLabel.setFont(new Font("微软雅黑", 0, 20));
        jPanel.add(passwordLabel);

        // 创建ip文本域
        JTextField ipTextField = new JTextField("127.0.0.1",30);
        ipTextField.setBounds(140, 35, 300, 35);
        ipTextField.setFont(new Font("微软雅黑", 0, 19));
        jPanel.add(ipTextField);

        // 创建端口文本域
        JTextField portTextField = new JTextField("2000", 30);
        portTextField.setBounds(140, 105, 300, 35);
        portTextField.setFont(new Font("微软雅黑", 0, 19));

        // 开关服务器按钮
        JButton serverButton = new JButton("Start");
        serverButton.setBounds(175, 185, 150, 50);
        serverButton.setFont(new Font("微软雅黑", 1, 19));
        serverButton.addActionListener(new StartButtonListener(serverButton));
        serverButton.setFocusPainted(false);
        serverButton.setContentAreaFilled(false);
        serverButton.setForeground(new Color(52, 169, 18, 216));

        //现已加入豪华实例套餐
        jPanel.add(userLabel);
        jFrame.add(jPanel);
        jPanel.add(serverButton);
        jPanel.add(portTextField);

        //最神圣的一步！
        jFrame.setVisible(true);
    }

    public class StartButtonListener implements ActionListener {
        /*
         *开关服务器按钮按下的监听
         */
        private JButton jButton = null;
        ServerSystem server = null;
        Thread thread = null;
        public StartButtonListener(JButton jButton) {
            this.jButton = jButton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /*
            当按下按钮时，根据开或关执行不同命令
            开：创建一个新的线程(创建ServerSystem对象，开启服务器)
            关: 关闭进程，停止ServerSystem对象
             */
            if(jButton.getText().equals("Start")) {  //检测按钮上的文本
                jButton.setText("Stop");
                (thread = new Thread(() -> {
                    try {
                        server = new ServerSystem();
                        server.start();  //开启服务器
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                })).start();
            }else {
                jButton.setText("Start");
                server.stop();   //关闭服务器
                thread.interrupt();
            }

        }
    }
}

