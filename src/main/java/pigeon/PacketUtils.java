package pigeon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PacketUtils {
    private PacketUtils(){}

    public static ByteBuffer encodeStringPacket(String str) {
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        int strLength = strBytes.length;
        return ByteBuffer.allocate(strLength + Integer.BYTES)
                .putInt(strLength)
                .put(strBytes)
                .flip();
    }

    public static String readStringPacket(ReadableByteChannel channel) throws IOException {
        var buffer = ByteBuffer.allocate(Integer.BYTES);
        channel.read(buffer);
        buffer.flip();
        int strLength = buffer.getInt();

        var strBuffer = ByteBuffer.allocate(strLength);
        var charBuffer = StandardCharsets.UTF_8.decode(strBuffer);
        return charBuffer.toString();
    }

    public static ByteBuffer encodeObjectPacket(Serializable object) {
        try {
            var byteArrayOutputStream = new ByteArrayOutputStream();
            var objOutput = new ObjectOutputStream(byteArrayOutputStream);
            objOutput.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return ByteBuffer.allocate(Integer.BYTES + bytes.length)
                    .putInt(bytes.length)
                    .put(bytes)
                    .flip();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
