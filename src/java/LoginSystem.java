import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Font;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;


public class LoginSystem {

    public LoginSystem(){}
    public void start() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.createFrame();
        loginFrame.loginButton.addActionListener(new LoginListener(loginFrame));
        loginFrame.registerButton.addActionListener(new RegisterListener(loginFrame));
    }
    //密码用md5加密后再传输
    public String encryption(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = password.getBytes();
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            return new String(resultByteArray);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public class LoginListener implements ActionListener {
        //登录按钮的监听
        private LoginFrame loginFrame = null;
        LoginListener(LoginFrame loginFrame) {
            this.loginFrame = loginFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PrintWriter printWriter = null;
            Scanner scanner = null;
            Socket socket = null;
            try {
                socket = new Socket("127.0.0.1", 8000);
                printWriter = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                String username = loginFrame.userTextField.getText();
                String password = new String(loginFrame.passwordTextField.getPassword());
                //
                password = encryption(password);
                printWriter.println("LOGIN");
                printWriter.println(username);
                printWriter.println(password);
                printWriter.flush();
                String receive = scanner.nextLine();
                //错误消息，弹窗
                if(!receive.equals("welcome")) {
                    JOptionPane.showMessageDialog(null, receive, "error", 1);
                }else {
                    //接收在线用户列表
                    String userlists = scanner.nextLine();
                    //开启聊天界面
                    ClientSystem clientSystem = new ClientSystem(socket);
                    clientSystem.startClientSystem(socket, username, loginFrame.jFrame, userlists);
                    loginFrame.jFrame.setVisible(false);
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    public class RegisterListener implements ActionListener {
        private LoginFrame loginFrame = null;
        RegisterListener(LoginFrame loginFrame) {
            this.loginFrame = loginFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loginFrame.jFrame.setVisible(false);
            RegisterFrame registerFrame = new RegisterFrame();
            registerFrame.start();
            registerFrame.jButton.addActionListener(new RegisterNewListener(registerFrame, loginFrame.jFrame));
            registerFrame.returnButton.addActionListener(new returnListener(registerFrame, loginFrame.jFrame));
        }

        public class RegisterNewListener implements ActionListener {
            RegisterFrame registerFrame = null;
            JFrame jFrame = null;
            public RegisterNewListener(RegisterFrame registerFrame, JFrame jFrame) {
                this.registerFrame = registerFrame;
                this.jFrame = jFrame;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                PrintWriter printWriter = null;
                Scanner scanner = null;
                try {
                    String password = new String(registerFrame.passwordTextField.getPassword());
                    if(!isPasswordComplex(password)) {
                        JOptionPane.showMessageDialog(null, "<html><p>password: length 6-18<br>Contains special characters, uppercase and lowercase letters and numbers</p></html>", "erro", 1);
                        return;
                    }
                    socket = new Socket("127.0.0.1", 8000);
                    printWriter = new PrintWriter(socket.getOutputStream());
                    scanner = new Scanner(socket.getInputStream());
                    String username = registerFrame.usernameTextField.getText();
                    String passwordConfirm = new String(registerFrame.passwordConfirmTextField.getPassword());
                    String gender = registerFrame.genderTextField.getText();
                    String address = registerFrame.addressTextField.getText();
                    String age = registerFrame.ageTextField.getText();
                    password = encryption(password);
                    passwordConfirm = encryption(passwordConfirm);
                    printWriter.println("REGISTER");
                    printWriter.println(username);
                    printWriter.println(password);
                    printWriter.println(passwordConfirm);
                    printWriter.println(gender);
                    printWriter.println(address);
                    printWriter.println(age);
                    printWriter.flush();
                    String receive = scanner.nextLine();
                    if(!receive.equals("welcome")) {
                        JOptionPane.showMessageDialog(null, receive, "erro", 1);
                        socket.close();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Register successfully", "success", 1);
                        registerFrame.jFrame.setVisible(false);
                        this.jFrame.setVisible(true);
                        socket.close();
                    }
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        public class returnListener implements ActionListener {
            RegisterFrame registerFrame = null;
            JFrame jFrame = null;
            public returnListener(RegisterFrame registerFrame, JFrame jFrame) {
                this.registerFrame = registerFrame;
                this.jFrame = jFrame;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                registerFrame.jFrame.setVisible(false);
                this.jFrame.setVisible(true);
            }

        }
        //密码复杂度检测
        public boolean isPasswordComplex(String age) {
            String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@%^*?~]).{6,18}$";
            boolean isMatch = Pattern.matches(pattern, age);
            return isMatch;
        }
    }
}
