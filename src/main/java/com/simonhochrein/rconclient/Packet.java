package com.simonhochrein.rconclient;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Packet {
    public int requestId;
    public int type;
    public String payload;

    public Packet(int requestId, int type, String payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }

    public ByteBuffer encode() {
        ByteBuffer buffer = ByteBuffer.allocate(1460).order(ByteOrder.LITTLE_ENDIAN);
        buffer.clear();

        buffer.position(Integer.BYTES);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(StandardCharsets.US_ASCII.encode(payload));
        buffer.put((byte)0);
        buffer.put((byte)0);
        buffer.putInt(0, buffer.position() - Integer.BYTES);
        buffer.flip();
        return buffer;
    }
}
