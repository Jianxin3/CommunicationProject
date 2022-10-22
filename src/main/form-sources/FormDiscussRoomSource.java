import pigeon.gui.DiscussRoomLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FormDiscussRoomSource {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new DiscussRoomLayout());
        frame.setMinimumSize(new Dimension(640, 480));

        var messagePane = new JScrollPane();
        var textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        messagePane.getViewport().add(textArea);
        messagePane.setBorder(new LineBorder(new Color(33, 82, 209), 2, true));
        frame.add("MessagePane", messagePane); //0

        var scrollPane = new JScrollPane();
        var sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        sendArea.setWrapStyleWord(true);
        scrollPane.getViewport().add(sendArea);
        frame.add("SendPane", scrollPane); //1
        frame.add("SendButton", new JButton("发送")); //2

        scrollPane.setOpaque(false);

        try (FileOutputStream fileOutputStream = new FileOutputStream("DiscussRoom.ui");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(frame);
            objectOutputStream.flush();
        }
    }
}
