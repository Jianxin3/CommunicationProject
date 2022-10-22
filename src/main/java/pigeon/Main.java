package pigeon;

import pigeon.gui.FileUIProvider;
import pigeon.gui.StaticActionCommands;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) {
        JFrame connectDialog;
        JPanel connectPanel;
        JButton avatar;
        JButton connectServer;
        JTextField address;
        JTextField port;

        var uiProvider = new FileUIProvider();
        connectDialog = (JFrame) uiProvider.newWindow("/ConnectDialog.ui");

        synchronized (connectDialog.getTreeLock()) {
            connectPanel = (JPanel) connectDialog.getContentPane().getComponent(0);
        }

        synchronized (connectPanel.getTreeLock()) {
            avatar = (JButton) connectPanel.getComponent(0);
            connectServer = (JButton) connectPanel.getComponent(8);
            address = (JTextField) connectPanel.getComponent(4);
            port = (JTextField) connectPanel.getComponent(6);
        }

        var staticCommands = new StaticActionCommands();

        avatar.setActionCommand("chooseAvatar");
        avatar.addActionListener(staticCommands);

        JProgressBar progressBar = new JProgressBar(SwingConstants.VERTICAL, 0, 100);
        progressBar.setSize(50, 50);
        progressBar.setStringPainted(true);
        connectPanel.add(progressBar);
        progressBar.setLocation(avatar.getX(), avatar.getY() + avatar.getHeight() + 1);

        connectDialog.setVisible(true);

        connectServer.addActionListener(action -> {
            Thread ui = new Thread() {
                private boolean isClosed;

                @Override
                public void run() {
                    String ori = connectServer.getLabel();
                    for (int i = 0; !isClosed; i++) {
                        String s = ori + " ";
                        switch (i) {
                            case 0 -> s += '-';
                            case 1 -> s += "\\";
                            case 2 -> s += "|";
                            case 3 -> s += "/";
                            default -> {
                                i = 0;
                                s += '-';
                            }
                        }
                        connectServer.setLabel(s);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                    connectServer.setLabel(ori);
                }

                @Override
                public void interrupt() {
                    super.interrupt();
                    isClosed = true;
                }
            };
            ui.start();

            try (SocketChannel socketChannel = SocketChannel.open()) {
                socketChannel.connect(new InetSocketAddress(address.getText(), Integer.parseInt(port.getText())));

                FileDialog fileDialog = new FileDialog(connectDialog);
                fileDialog.setTitle("选择要发送的视频");
                while(fileDialog.getFile() == null) {
                    fileDialog.setVisible(true);
                }
                Path file = Path.of(fileDialog.getDirectory(), fileDialog.getFile());
                try (var channel = FileChannel.open(file, StandardOpenOption.READ)) {

                    long length = file.toFile().length();
                    socketChannel.write(ByteBuffer.allocate(Long.BYTES).putLong(length).flip());

                    String fileName = fileDialog.getFile();
                    byte[] nameBytes = fileName.getBytes(StandardCharsets.UTF_8);
                    socketChannel.write(ByteBuffer.allocate(Integer.BYTES + nameBytes.length)
                            .putInt(nameBytes.length).put(nameBytes).flip());

                    new Thread(() -> {
                        try {
                            System.out.println(length);
                            long transformed = 0;
                            while (length > transformed && connectDialog.isVisible()) {
                                long accepted = channel.transferTo(transformed, Math.min(length - transformed, 8192), socketChannel);
                                transformed += accepted;
                                long finalTransformed = transformed;
                                progressBar.setValue((int) ((finalTransformed * 1.0 / length) * 100));
                            }

                            System.out.println("Finished" + transformed);
                            ui.interrupt();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (Exception e) {
                ui.interrupt();
                connectServer.setLabel("Connect server");
                e.printStackTrace();
            }
        });
    }
}
