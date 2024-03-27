package oprpp2.hw01.client;

import oprpp2.hw01.message.*;
import oprpp2.hw01.util.MessageUtil;
import oprpp2.hw01.util.NetworkUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class ClientWorker {

    private final InetAddress ip;
    private final int port;
    private final DatagramSocket socket;

    private ClientEnvironment environment;
    private Long uid = null;
    private Message pendingMessage = null;

    private ClientFrame frame = null;

    public ClientWorker(InetAddress ip, int port, DatagramSocket socket) {
        this.ip = ip;
        this.port = port;
        this.socket = socket;
    }

    public void setFrame(ClientFrame frame) {
        this.frame = frame;
    }

    public void setEnvironment(ClientEnvironment environment) {
        this.environment = environment;
    }

    public void setPendingMessage(Message pendingMessage) {
        this.pendingMessage = pendingMessage;
    }

    public void listen() {
        while (true) {
            if (pendingMessage != null) {
                System.out.println("Input disabling");

                if (pendingMessage instanceof HelloMessage) {
                    AckMessage response = NetworkUtil.getResponseBySendMessage(this.ip,
                            this.port, this.socket, pendingMessage);

                    if (response == null) {
                        throw new ClientException("Could not establish connection with a server.");
                    }

                    pendingMessage = null;
                    this.uid = response.getKey();
                    this.environment.setUid(this.uid);
                    this.pendingMessage = null;

                    System.out.println("Enabling ");
                    if (this.frame != null) this.frame.enableInput();
                    continue;
                } else if (pendingMessage instanceof ByeMessage) {
                    AckMessage response = NetworkUtil.getResponseBySendMessage(this.ip,
                            this.port, this.socket, pendingMessage);

                    if (response == null) {
                        throw new ClientException("Could not establish connection with a server.");
                    }

                    this.frame.dispose();
                    return;
                } else {
                    AckMessage response = NetworkUtil.getResponseBySendMessage(this.ip,
                            this.port, this.socket, pendingMessage);

                    if (response == null) {
                        throw new ClientException("Could not establish connection with a server.");
                    }

                    this.pendingMessage = null;

                    if (this.frame != null) this.frame.enableInput();
                    continue;
                }
            }

            byte[] receiveBuffer = new byte[NetworkUtil.MAX_MESSAGE_LENGTH];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            try {
                this.socket.receive(receivePacket);
            } catch (IOException e) {
                continue;
            }

            try {
                Message message = MessageUtil.getMessageFromByte(receivePacket.getData());

                if (!(message instanceof InMessage)) continue;

                InMessage inMessage = (InMessage) message;

                if (this.uid != null) {
                    NetworkUtil.sendMessage(this.ip, this.port, this.socket,
                            new AckMessage(message.getNumber(), uid));
                }

                if (this.frame != null) {
                    this.frame.handleReceiveMessage(receivePacket.getSocketAddress().toString(),
                            inMessage.getFullName(), inMessage.getMessage());
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

}
