import javax.swing.*;
import java.awt.*;

public class EmojiFrame {
    JButton jButton = null;
    JList emojiList = null;
    JFrame jFrame = null;
    DefaultListModel defaultListModel = null;
    public void selectEmoji(){
        final Color JPBackgroundColor = new Color(244, 244, 244);
        final Color greenButtonColor = new Color(52, 169, 18, 216);
        JFrame jFrame = new JFrame("Emoji");
        jFrame.setAlwaysOnTop(true);
        jFrame.setLayout(null);
        jFrame.setSize(200, 400);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("icon.jpg"));
        jFrame.setIconImage(imageIcon.getImage());
        DefaultListModel defaultListModel = new DefaultListModel();
        JList emojiList = new JList(defaultListModel);
        for(char c = '\uDE00'; c <= '\uDE4F'; ++c) {
            String emojic = "          \uD83D" + c; //10 space
            defaultListModel.addElement(emojic);
        }
        for(char c = '\udc00'; c <= '\udc50'; ++c) {
            String emojic = "          \ud83d" + c; //10 space
            defaultListModel.addElement(emojic);
        }
        emojiList.setFont(new Font("Dialog", 0, 25));
        JScrollPane emojiJSP = new JScrollPane(emojiList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        emojiJSP.setBackground(JPBackgroundColor);
        emojiJSP.setBounds(0, 0, 185, 300);

        JButton jButton = new JButton("Select");
        jButton.setContentAreaFilled(false);
        jButton.setForeground(greenButtonColor);
        jButton.setFocusPainted(false);
        jButton.setFont(new Font("微软雅黑", 0, 17));
        jButton.setBounds(30, 310, 120, 40);

        jFrame.add(emojiJSP);
        jFrame.add(jButton);
        jFrame.setVisible(true);

        this.emojiList = emojiList;
        this.defaultListModel = defaultListModel;
        this.jFrame = jFrame;
        this.jButton = jButton;
    }
}
