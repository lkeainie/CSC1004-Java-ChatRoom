import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class RegisterFrame {
        JTextField usernameTextField = null;
        JTextField addressTextField = null;
        JTextField ageTextField = null;
        JTextField genderTextField = null;
        JPasswordField passwordTextField = null;
        JPasswordField passwordConfirmTextField = null;
        JButton jButton = null;
        JFrame jFrame = null;
        JButton returnButton = null;
    public void start() {
        JFrame jFrame = new JFrame("Icaht");
        jFrame.setSize(500, 580);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("icon.jpg"));
        jFrame.setIconImage(imageIcon.getImage());
        //常量
        final Color buttonColor = new Color(52, 169, 18, 216);;

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

        JLabel passwordConfirmLabel = new JLabel("  Confirm : ");
        passwordConfirmLabel.setBounds(20, 170, 150, 40);
        passwordConfirmLabel.setFont(new Font("微软雅黑", 0, 20));
        jPanel.add(passwordConfirmLabel);

        // 创建性别标签
        JLabel genderLabel = new JLabel("   Gender : ");
        genderLabel.setBounds(20, 240, 150, 40);
        genderLabel.setFont(new Font("微软雅黑", 0, 20));
        jPanel.add(genderLabel);

        // 创建地址标签
        JLabel addressLabel = new JLabel("  Address : ");
        addressLabel.setBounds(20, 310, 150, 40);
        addressLabel.setFont(new Font("微软雅黑", 0, 20));
        jPanel.add(addressLabel);

        // 创建年龄标签
        JLabel ageLabel = new JLabel("        Age : ");
        ageLabel.setBounds(20, 380, 150, 40);
        ageLabel.setFont(new Font("微软雅黑", 0, 20));
        jPanel.add(ageLabel);

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

        // 确认密码文本域
        JPasswordField passwordConfirmTextField = new JPasswordField();
        passwordConfirmTextField.setBounds(140, 175, 300, 35);
        passwordConfirmTextField.setFont(new Font("微软雅黑", 0, 19));
        jPanel.add(passwordConfirmTextField);

        // 创建性别文本域 JComBox?
        JTextField genderTextField = new JTextField();
        genderTextField.setBounds(140, 245, 300, 35);
        genderTextField.setFont(new Font("微软雅黑", 0, 19));
        jPanel.add(genderTextField);

        // 创建地址文本域
        JTextField addressTextField = new JTextField();
        addressTextField.setBounds(140, 315, 300, 35);
        addressTextField.setFont(new Font("微软雅黑", 0, 19));
        jPanel.add(addressTextField);

        // 创建年龄文本域
        JTextField ageTextField = new JTextField();
        ageTextField.setBounds(140, 385, 300, 35);
        ageTextField.setFont(new Font("微软雅黑", 0, 19));
        jPanel.add(ageTextField);

        //注册按钮
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(185, 465, 130, 50);
        registerButton.setFont(new Font("微软雅黑", 1, 19));
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(buttonColor);
        jPanel.add(registerButton);

        //返回按钮
        JButton returnButton = new JButton("Return");
        returnButton.setBounds(340, 485, 100, 30);
        returnButton.setFont(new Font("微软雅黑", 1, 16));
        returnButton.setFocusPainted(false);
        returnButton.setContentAreaFilled(false);
        returnButton.setForeground(buttonColor);
        jPanel.add(returnButton);

        //现已加入豪华实例套餐
        this.genderTextField = genderTextField;
        this.addressTextField = addressTextField;
        this.ageTextField = ageTextField;
        this.passwordTextField = passwordTextField;
        this.usernameTextField = userTextField;
        this.passwordConfirmTextField = passwordConfirmTextField;
        this.jButton = registerButton;
        this.jFrame = jFrame;
        this.returnButton = returnButton;

        //一堆回车设置
        usernameTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    passwordTextField.requestFocus();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });

        passwordTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    passwordConfirmTextField.requestFocus();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });
        passwordConfirmTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    genderTextField.requestFocus();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });

        genderTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    addressTextField.requestFocus();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });
        addressTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    ageTextField.requestFocus();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });
        ageTextField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    registerButton.doClick();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });

        jFrame.setVisible(true);
    }

}
