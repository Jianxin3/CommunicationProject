package pigeon.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.Serializable;

public record PlusIcon(int width, int plusThickness, int alignment, int borderThickness, Color color, boolean roundedCorners)
        implements Icon, Serializable {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics graphics = g.create(x, y, width, width);
        var borderObj = new LineBorder(color, borderThickness, roundedCorners);
        borderObj.paintBorder(c, graphics, 0, 0, width, width);
        graphics.setColor(color);
        int plusWidth = width - ((borderThickness + alignment) * 2);
        graphics.fillRect(borderThickness + alignment, (width - plusThickness) / 2, plusWidth, plusThickness);
        graphics.fillRect((width - plusThickness) / 2, borderThickness + alignment, plusThickness, plusWidth);
        graphics.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return width;
    }
}
