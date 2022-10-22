package pigeon;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TestSocket {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket();
        InetSocketAddress socketAddress = new InetSocketAddress("27.199.83.140", 50010);
        while (!socket.isConnected()) {
            try {
                System.out.println("Trying connecting " + socketAddress.getHostString() + " ...");
                socket.connect(socketAddress);
                if (!socket.isConnected()) {
                    System.out.println("Failed to connect, retrying...");
                    socket.close();
                    socket = new Socket();
                }
            } catch (IOException e) {
                System.out.println("Failed to connect, retrying...");
                e.printStackTrace();
                socket.close();
                socket = new Socket();
            }
            Thread.sleep(2000);
        }
        Toolkit.getDefaultToolkit().beep();
        System.out.println("Connected!");
        new Thread(new MessageReceiveThread(socket)).start();
        try (OutputStream socketOutput = socket.getOutputStream();
             PrintStream printStream = new PrintStream(socketOutput, true, StandardCharsets.US_ASCII)) {
            for (int i = 0; i < 20; i++) {
                printStream.println(i + "Hello World!");
            }
            printStream.println("Test Ended.");
        }
        socket.close();
    }

    private record MessageReceiveThread(Socket socket) implements Runnable {
        @Override
        public void run() {
            if (!socket.isClosed() && socket.isConnected()) {
                try (BufferedInputStream socketStream = new BufferedInputStream(socket.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socketStream))) {
                    String line;
                    while ((line = reader.readLine()) != null && !socket.isClosed())
                        System.out.println("Denchin: " + line);
                } catch (IOException e) {
                    System.out.println("Error occurred");
                    e.printStackTrace();
                }
            }
        }
    }
}
