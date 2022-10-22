import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TestDeserialize {
    public static void main(String[] args) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream("TestWindow.ui");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            if (objectInputStream.readObject() instanceof JFrame frame) {
                frame.setVisible(true);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
