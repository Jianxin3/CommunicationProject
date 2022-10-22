package pigeon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class ChatHandle implements Runnable, ActionListener {
    protected SocketChannel channel;
    protected final JFrame discussForm;
    protected final JFrame connectDialog;
    protected final Logger LOG = Logger.getLogger("ChatHandle");

    public ChatHandle(JFrame discussForm, JFrame connectDialog) {
        this.discussForm = discussForm;
        this.connectDialog = connectDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SendMsg")) {
            var scrollPane = (JScrollPane) discussForm.getComponent(1);
            var textPane = (JTextArea) scrollPane.getViewport().getComponent(0);
            try {
                if (channel != null && !textPane.getText().isEmpty()) {
                    var buffer = PacketUtils.encodeStringPacket(textPane.getText());
                    channel.write(ByteBuffer.allocate(1).put(Byte.parseByte("0x0")).flip());
                    channel.write(buffer);
                }
            } catch (IOException ex) {
                LOG.info(ex.toString());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        JPanel connectPane = (JPanel) connectDialog.getComponent(0);
        JTextField username = (JTextField) connectPane.getComponent(2);
        JTextField address = (JTextField) connectPane.getComponent(4);
        JTextField port = (JTextField) connectPane.getComponent(6);
        JButton avatar = (JButton) connectPane.getComponent(0);

        try {
            connectPane.getRootPane().getParent().setVisible(false);
            InetSocketAddress socketAddress = new InetSocketAddress(address.getText(), Integer.parseInt(port.getText()));
            channel = SocketChannel.open(socketAddress);
            channel.socket().setKeepAlive(true);
            channel.write(PacketUtils.encodeStringPacket(username.getText()));
            if (avatar.getIcon() instanceof ImageIcon image) {
                channel.write(PacketUtils.encodeStringPacket("avatar"));
                channel.write(PacketUtils.encodeObjectPacket(image));
            }
            channel.write(PacketUtils.encodeStringPacket("END"));
        } catch (IOException e) {
            connectPane.getRootPane().getParent().setVisible(true);
            LOG.info(e.toString());
            e.printStackTrace();
        }
    }
}
