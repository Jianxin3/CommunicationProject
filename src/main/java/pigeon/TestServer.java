package pigeon;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TestServer {
    public static void main(String[] args) {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.socket().bind(new InetSocketAddress(25565));
            while(true) {
                System.out.println("Start listening on port " + server.socket().getLocalPort() + " ...");
                SocketChannel socket = server.accept();
                socket.socket().setSoTimeout(2000);
                try {
                    System.out.println("Client from " + socket.socket().getInetAddress().toString() + " connected.");
                    Toolkit.getDefaultToolkit().beep();
                    System.out.println("Connected!");
                    socket.socket().setKeepAlive(true);

                    //Reading file length
                    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                    socket.read(buffer);
                    buffer.flip();//need flip

                    //Reading file name
                    ByteBuffer nameLength = ByteBuffer.allocate(Integer.BYTES);
                    socket.read(nameLength);
                    nameLength.flip();
                    int nlength = nameLength.getInt();
                    ByteBuffer name = ByteBuffer.allocate(nlength);
                    socket.read(name);
                    byte[] nameBytes = new byte[nlength];
                    name.flip().get(nameBytes);
                    String fileName = new String(nameBytes, StandardCharsets.UTF_8);

                    var file = FileChannel.open(Path.of(fileName), StandardOpenOption.CREATE,
                            StandardOpenOption.WRITE);
                    long length = buffer.getLong();
                    System.out.println(length);
                    long transformed = 0;

                    while (length > transformed) {
                        long accepted = file.transferFrom(socket, transformed, Math.min(length - transformed, 8192));
                        transformed += accepted;
                        System.out.println("Accepted: " + transformed + "/" + length);
                    }
                    file.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
