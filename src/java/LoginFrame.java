import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginFrame {
    JButton loginButton = null;
    JButton registerButton = null;
    JTextField userTextField  = null;
    JPasswordField passwordTextField = null;
    JFrame jFrame = null;
    public LoginFrame() {}
    public void createFrame() {
        //创建窗口
        JFrame jFrame = new JFrame("Ichat");
        jFrame.setSize(500, 300);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("icon.jpg"));
        jFrame.setIconImage(imageIcon.getImage());
        //
        final Color buttonColor = new Color(52, 169, 18, 216);
        // 创建面板
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jFrame.add(jPanel);

        // 创建用户名标签
        JLabel userLabel = new JLabel("        User : ");
        userLabel.setBounds(20, 30, 150, 40);
        userLabel.setFont(new Font("微软雅黑", 0, 20));
        jPanel.add(userLabel);

        // 创建密码标签
        JLabel passwordLabel = new JLabel("Password : ");
        passwordLabel.setBounds(20, 100, 150, 40);
        passwordLabel.setFont(new Font("微软雅黑", 0, 20));
        jPanel.add(passwordLabel);

        // 创建用户名文本域
        JTextField userTextField = new JTextField();
        userTextField.setBounds(140, 35, 300, 35);
        userTextField.setFont(new Font("微软雅黑", 0, 19));
        jPanel.add(userTextField);

        // 创建密码文本域
        JPasswordField passwordTextField = new JPasswordField();
        passwordTextField.setBounds(140, 105, 300, 35);
        passwordTextField.setFont(new Font("微软雅黑", 0, 19));
        jPanel.add(passwordTextField);

        // 登录按钮
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(140, 185, 130, 50);
        loginButton.setFont(new Font("微软雅黑", 1, 19));
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(buttonColor);
        jPanel.add(loginButton);
        loginButton.setFocusPainted(false);

        //注册按钮
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(300, 185, 130, 50);
        registerButton.setFont(new Font("微软雅黑", 1, 19));
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(buttonColor);
        jPanel.add(registerButton);
        registerButton.setFocusPainted(false);

        jFrame.setVisible(true);
        //回车将进入下一个文本框
        userTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    passwordTextField.requestFocus();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });
        //回车启动登录按钮
        passwordTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    loginButton.doClick();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });

        //现已加入豪华实例套餐
        this.loginButton = loginButton;
        this.registerButton = registerButton;
        this.jFrame = jFrame;
        this.passwordTextField = passwordTextField;
        this.userTextField = userTextField;

    }
}
