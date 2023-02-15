import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOError;
import java.net.URL;

public class MainFrame {
    public void start(){
        JFrame jFrame = new JFrame("Ichat");
        jFrame.setSize(500, 300);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("icon.jpg"));
        jFrame.setIconImage(imageIcon.getImage());

        Color buttonColor =new Color(52, 169, 18, 216);
        // 创建面板
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jFrame.add(jPanel);

        JLabel jLabel = new JLabel(" _Ichat_ ", SwingConstants.CENTER);
        jLabel.setFont(new Font("Times New Roman", 1, 90));
        jLabel.setBounds(0, 20, 500, 100);
        jPanel.add(jLabel);

        JButton serverButton = new JButton("Server");
        serverButton.setFont(new Font("微软雅黑", Font.BOLD, 30));
        serverButton.setBounds(70, 150, 150, 60);
        serverButton.setContentAreaFilled(false);
        serverButton.setForeground(buttonColor);
        serverButton.setFocusPainted(false);
        jPanel.add(serverButton);

        JButton clientButton = new JButton("Client");
        clientButton.setFont(new Font("微软雅黑", Font.BOLD, 30));
        clientButton.setBounds(280, 150, 150, 60);
        clientButton.setContentAreaFilled(false);
        clientButton.setForeground(buttonColor);
        clientButton.setFocusPainted(false);
        jPanel.add(clientButton);

        jFrame.setVisible(true);

        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerFrame serverFrame = new ServerFrame();
                serverFrame.createFrame();
                jFrame.setVisible(false);
            }
        });

        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginSystem loginSystem = new LoginSystem();
                loginSystem.start();
                jFrame.setVisible(false);
            }
        });


    }

}
