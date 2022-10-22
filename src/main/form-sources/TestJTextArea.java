import javax.swing.*;
import java.awt.*;

public class TestJTextArea {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        var textArea = new JTextArea();
        var scrollPane = new JScrollPane();
        scrollPane.getViewport().add(textArea);
        frame.add(BorderLayout.CENTER, scrollPane);

        frame.setVisible(true);
    }
}
