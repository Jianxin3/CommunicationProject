package pigeon.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class FileUIProvider implements UIProvider {
    Logger LOG = Logger.getLogger("FileUIProvider");

    @Override
    public JComponent newJComponent(String location) {
        try (var objectInputStream = new ObjectInputStream(getClass().getResourceAsStream(location))) {
            JComponent jComponent = (JComponent) objectInputStream.readObject();
            return jComponent;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Window newWindow(String location) {
        try (var objectInputStream = new ObjectInputStream(getClass().getResourceAsStream(location))) {
            Window Window = (Window) objectInputStream.readObject();
            return Window;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
