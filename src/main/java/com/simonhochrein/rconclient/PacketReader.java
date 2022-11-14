package com.simonhochrein.rconclient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketReader {

    private final ReadFunction readFunction;
//    private final ByteBuffer buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);

    public PacketReader(ReadFunction readFunction) {
        this.readFunction = readFunction;
    }

    public Packet read() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);

        while(buffer.position() < 4) {
            if(readFunction.read(buffer) == -1) {
                throw new IOException("Connection closed");
            }
        }
        buffer.flip();
        final int length = buffer.getInt();
        buffer.compact();

        if(buffer.position() < length) {
            if(readFunction.read(buffer) == -1) {
                throw new IOException("Connection closed");
            }
        }
        buffer.flip();
        int requestId = buffer.getInt();
        int type = buffer.getInt();
        byte[] payload = new byte[length - 10];
        buffer.get(payload);
        String result = new String(payload);
        buffer.compact();

        return new Packet(requestId, type, result);
    }

    @FunctionalInterface
    public interface ReadFunction {
        int read(ByteBuffer buffer) throws IOException;
    }
}
