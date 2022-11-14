package com.simonhochrein.rconclient;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

public class PromptController {
    @FXML
    TextArea console;

    @FXML
    TextField input;

    private ByteChannel socket;
    private PacketReader reader;

    @FXML
    public void initialize() {
        this.console.textProperty().addListener($ -> console.setScrollTop(Double.MAX_VALUE));
        this.console.setFocusTraversable(false);
    }

    public void connect(String host, int port, String password) throws IOException {
        this.socket = SocketChannel.open(new InetSocketAddress(host, port));
        this.reader = new PacketReader(this.socket::read);

        var response = sendPacket(3, password);
        if (response.requestId == -1) {
            throw new IOException("Authentication Failed");
        }
    }

    volatile int requestId;

    @FXML
    public void send() throws IOException {
        var command = input.getText();
        input.setText(""); // clear input field to make it behave like a prompt

        var response = sendPacket(2, command);
        log(response.payload);
    }

    private synchronized Packet sendPacket(int packetType, String payload) throws IOException {
        var packet = new Packet(requestId++, packetType, payload);
        socket.write(packet.encode());
        var response = reader.read();
        assert response.requestId == packet.requestId;

        return response;
    }

    private void log(String text) {
        console.appendText(cleanText(text) + "\n");
    }

    private String cleanText(String text) {
        return text.replaceAll("\u00A7.", ""); // replace §(whatever) with "" to produce clean output in the log
    }
}
