import pigeon.gui.PlusIcon;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TestSerialize {
    public static void main(String[] args) throws IOException {
        JFrame connectDialog = new JFrame();
        connectDialog.setResizable(false);
        connectDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        connectDialog.setTitle("连接或创建服务器...");
        connectDialog.setSize(800, 300);

        JPanel connectPanel = new JPanel();
        connectPanel.setSize(connectPanel.getSize());
        connectDialog.add(connectPanel, 0); //0
        connectPanel.setLayout(null);
        JButton avatar = new JButton(new PlusIcon(50, 4, 13, 2, Color.GRAY, true));
        avatar.setFocusPainted(false);
        avatar.setBorderPainted(false);
        avatar.setContentAreaFilled(false);
        avatar.setSize(50, 50);
        connectPanel.add(avatar); //0

        JTextField username = new JTextField();
        username.setSize(300, 30);
        JLabel label = new JLabel("昵称: ");
        label.setFont(new Font("雅黑", Font.BOLD, 25));
        username.setFont(new Font("雅黑", Font.PLAIN, 20));
        label.setSize(label.getFontMetrics(label.getFont()).stringWidth(label.getText()) + 10, 30);
        connectPanel.add(label); //1
        connectPanel.add(username); //2
        label.setLocation(10, 10);
        username.setLocation(label.getWidth() + 10, 10);
        avatar.setLocation(username.getX() + username.getWidth() + 10, 10);

        JTextField address = new JTextField();
        address.setSize(username.getSize());
        JLabel ip = new JLabel("地址: ");
        ip.setFont(label.getFont());
        address.setFont(username.getFont());
        ip.setSize(label.getSize());
        connectPanel.add(ip);//3
        connectPanel.add(address); //4
        ip.setLocation(10, label.getY() + label.getHeight() + 5);
        address.setLocation(ip.getWidth() + 10, ip.getY());

        JTextField port = new JTextField();
        port.setSize(username.getSize());
        JLabel pt = new JLabel("端口: ");
        pt.setFont(label.getFont());
        port.setFont(username.getFont());
        pt.setSize(label.getSize());
        connectPanel.add(pt);//5
        connectPanel.add(port);//6
        pt.setLocation(10, ip.getY() + ip.getHeight() + 5);
        port.setLocation(pt.getWidth() + 10, pt.getY());

        connectPanel.setSize(10 + label.getWidth() + username.getWidth() + 10 + avatar.getWidth() + 30,
                10 + ((username.getHeight() + 5) * 5) + 5);
        connectDialog.setSize(connectPanel.getSize());

        JButton createServer = new JButton("创建服务器");
        JButton connectServer = new JButton("加入服务器");
        createServer.setFont(username.getFont());
        connectServer.setFont(createServer.getFont());
        createServer.setSize((connectPanel.getWidth() - 40) / 2, username.getHeight());
        connectServer.setSize(createServer.getSize());
        createServer.setLocation(10, pt.getY() + pt.getHeight() + 5);
        connectServer.setLocation(15 + createServer.getWidth(), createServer.getY());
        connectPanel.add(createServer);//7
        connectPanel.add(connectServer);//8

        try (FileOutputStream fileOutputStream = new FileOutputStream("ConnectDialog.ui");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(connectDialog);
            objectOutputStream.flush();
        }
    }
}
