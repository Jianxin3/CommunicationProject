package pigeon.gui;

import javax.swing.*;
import java.awt.*;

public interface UIProvider {
    JComponent newJComponent(String location);

    Window newWindow(String location);
}
