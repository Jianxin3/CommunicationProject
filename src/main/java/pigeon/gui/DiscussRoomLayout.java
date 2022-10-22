package pigeon.gui;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DiscussRoomLayout implements LayoutManager, Serializable {
    private transient Map<String, Component> layoutTable = new HashMap<>();

    @Override
    public void addLayoutComponent(String name, Component comp) {
        layoutTable.put(name, comp);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        layoutTable.forEach((name, component) -> {
            if (comp == component)
                layoutTable.remove(name);
        });
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(800, 600);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(800, 600);
    }

    @Override
    public void layoutContainer(Container parent) {
        JScrollPane messagePane = (JScrollPane) layoutTable.get("MessagePane");
        JScrollPane scrollPane = (JScrollPane) layoutTable.get("SendPane");
        JButton sendButton = (JButton) layoutTable.get("SendButton");

        messagePane.setSize(parent.getWidth(), (int) (parent.getHeight() * 0.618));
        scrollPane.setSize(parent.getWidth(), parent.getHeight() - messagePane.getHeight() - 20);
        messagePane.setLocation(0, 0);
        scrollPane.setLocation(0, messagePane.getHeight());

        sendButton.setSize(100, 20);
        sendButton.setLocation(parent.getWidth() - sendButton.getWidth(), scrollPane.getHeight() + scrollPane.getY());
    }
}
