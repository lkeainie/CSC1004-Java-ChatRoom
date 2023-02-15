import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientSystem {
    private Socket socket = null;
    //构造方法
    public ClientSystem(Socket socket) {
        this.socket = socket;
    }

    public void startClientSystem(Socket socket, String username, JFrame jFrame, String userlists) {
        ClientFrame clientFrame = new ClientFrame(username);
        String [] userlist = userlists.split(" ");
        for(int i = 0; i < userlist.length; ++i) {
            clientFrame.defaultListModel.addElement(userlist[i]);
        }
        clientFrame.emojiB.addActionListener(new emojiButtonListener(clientFrame.inputTF));
        clientFrame.sendB.addActionListener(new SendButtonListener(clientFrame.inputTF, socket));
        clientFrame.imageB.addActionListener(new imageButtonListener(socket));
        clientFrame.videoB.addActionListener(new videoButtonListener(socket));
        clientFrame.voiceB.addActionListener(new voiceButtonListener(clientFrame.voiceB));
        ClientThread clientThread = new ClientThread(socket, clientFrame, username);
        clientThread.start();
        clientFrame.logoutB.addActionListener(new LogoutListener(socket, jFrame, clientFrame.jFrame));

        clientFrame.inputTF.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    clientFrame.sendB.doClick();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });
    }
    //emoji
    public class emojiButtonListener implements ActionListener {
        private JTextField inputTF = null;
        public emojiButtonListener(JTextField inputTF) {
            this.inputTF = inputTF;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            EmojiFrame emojiFrame = new EmojiFrame();
            emojiFrame.selectEmoji();
            emojiFrame.jButton.addActionListener(new selectButtonListener(emojiFrame, inputTF));
        }

        public class selectButtonListener implements ActionListener {
            private EmojiFrame emojiFrame = null;
            private JTextField jTextField = null;
            public selectButtonListener(EmojiFrame emojiFrame, JTextField jTextField) {
                this.emojiFrame = emojiFrame;
                this.jTextField = jTextField;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = emojiFrame.emojiList.getSelectedIndex();
                if(index != -1) {
                    String emojiStr = (String)emojiFrame.defaultListModel.get(index);
                    emojiStr = emojiStr.substring(10);
                    jTextField.setText(jTextField.getText() + emojiStr);
                    emojiFrame.jFrame.setVisible(false);
                }
            }
        }
    }
    //发送按钮
    public class SendButtonListener implements ActionListener {
        
        private JTextField inputTF = null;
        Socket socket = null;

        public SendButtonListener(JTextField inputTF, Socket socket) {
            this.inputTF = inputTF;
            this.socket = socket;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String message = inputTF.getText();
            PrintWriter printWriter = null;
            if(!message.equals("")) {
                try {
                    printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.println("MESSAGE");
                    printWriter.println(message);
                    printWriter.flush();
                    inputTF.setText("");
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    //图片发送
    public class imageButtonListener implements ActionListener {
        private Socket socket = null;
        public imageButtonListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter filter =new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "gif", "png");
            jFileChooser.setFileFilter(filter);
            jFileChooser.setCurrentDirectory(null);
            int choose_status = jFileChooser.showOpenDialog(null);
            if(choose_status == JFileChooser.APPROVE_OPTION) {
                String path = jFileChooser.getSelectedFile().getPath();
                FileInputStream fis = null;
                byte[] bytes = new byte[1024];
                int len = 0;
                PrintWriter printWriter = null;
                ByteArrayOutputStream baos = null;
                try {
                    fis = new FileInputStream(path);
                    printWriter = new PrintWriter(socket.getOutputStream());
                    baos = new ByteArrayOutputStream();
                    while((len = fis.read(bytes)) != -1) {
                        baos.write(bytes, 0, len);
                    }
                    byte[] by = baos.toByteArray();
                    String base64 = Base64.getEncoder().encodeToString(by);
                    printWriter.println("IMAGE");
                    printWriter.println(base64);
                    printWriter.flush();
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public class videoButtonListener implements ActionListener {
        private Socket socket = null;
        public videoButtonListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter filter =new FileNameExtensionFilter("Video Files",  "wav", "wma");
            jFileChooser.setFileFilter(filter);
            jFileChooser.setCurrentDirectory(null);
            int choose_status = jFileChooser.showOpenDialog(null);
            if(choose_status == JFileChooser.APPROVE_OPTION) {
                String path = jFileChooser.getSelectedFile().getPath();
                FileInputStream fis = null;
                byte[] bytes = new byte[1024];
                int len = 0;
                PrintWriter printWriter = null;
                ByteArrayOutputStream baos = null;
                try {
                    fis = new FileInputStream(path);
                    printWriter = new PrintWriter(socket.getOutputStream());
                    baos = new ByteArrayOutputStream();
                    while((len = fis.read(bytes)) != -1) {
                        baos.write(bytes, 0, len);
                    }
                    byte[] by = baos.toByteArray();
                    String base64 = Base64.getEncoder().encodeToString(by);
                    printWriter.println("VIDEO");
                    printWriter.println(base64);
                    printWriter.flush();
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public class LogoutListener implements ActionListener {
        private Socket socket = null;
        private JFrame jFrameLR = null;
        private JFrame jFrameU = null;
        public LogoutListener(Socket socket, JFrame jFrameLR, JFrame jFrameU) {
            this.socket = socket;
            this.jFrameLR = jFrameLR;
            this.jFrameU = jFrameU;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println("LOGOUT");
                printWriter.flush();
                if(!socket.isClosed()) socket.close();
                jFrameLR.setVisible(true);
                jFrameU.setVisible(false);
            }catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    public class voiceButtonListener implements ActionListener {
        /*
         *开关服务器按钮按下的监听
         */
        private JButton jButton = null;
        Thread thread = null;
        public voiceButtonListener(JButton jButton) {
            this.jButton = jButton;
        }

        AudioRecorderThread audioRecorderThread = null;


        @Override
        public void actionPerformed(ActionEvent e) {
            if(jButton.getText().equals("voice start")) {  //检测按钮上的文本
                jButton.setText("voice stop");
                (thread = new Thread(() -> {
                    try {
                        audioRecorderThread = new AudioRecorderThread("voice.wav");
                        audioRecorderThread.start();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                })).start();
            }else {
                jButton.setText("voice start");
                audioRecorderThread.stopRecording();

                FileInputStream fis = null;
                byte[] bytes = new byte[1024];
                int len = 0;
                PrintWriter printWriter = null;
                ByteArrayOutputStream baos = null;
                try {
                    fis = new FileInputStream("./voice.wav");

                    printWriter = new PrintWriter(socket.getOutputStream());
                    baos = new ByteArrayOutputStream();
                    while((len = fis.read(bytes)) != -1) {
                        baos.write(bytes, 0, len);
                    }
                    byte[] by = baos.toByteArray();
                    String base64 = Base64.getEncoder().encodeToString(by);
                    printWriter.println("VIDEO");
                    printWriter.println(base64);
                    printWriter.flush();
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
                thread.interrupt();
            }
        }
    }
}