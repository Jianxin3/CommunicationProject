package pigeon.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class StaticActionCommands implements ActionListener {
    Logger LOG = Logger.getLogger("ActionCommandListener");

    @Override
    public void actionPerformed(ActionEvent e) {
        var commands = StaticCommands.class;
        try {
            var method = commands.getMethod(e.getActionCommand(), e.getClass());
            method.invoke(null, e);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            LOG.info(ex.toString());
        }
    }

    private static class StaticCommands {
        private StaticCommands(){}

        public static void chooseAvatar(ActionEvent e) {
            if (e.getSource() instanceof JButton avatarButton) {
                if (avatarButton.getRootPane().getParent() instanceof Window window) {
                    FileDialog fileDialog;
                    if (window instanceof Frame frame)
                        fileDialog = new FileDialog(frame);
                    else
                        fileDialog = new FileDialog((Dialog) window);
                    fileDialog.setTitle("选择一个图片作为头像");
                    fileDialog.setMode(FileDialog.LOAD);
                    fileDialog.setVisible(true);
                    if (fileDialog.getFile() != null) {
                        File file = new File(fileDialog.getDirectory(), fileDialog.getFile());
                        try {
                            var image = ImageIO.read(file);
                            if (image != null)
                                avatarButton.setIcon(new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_FAST)));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
    }
}
