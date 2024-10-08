package oprpp2.hw01.client;

import oprpp2.hw01.message.*;
import oprpp2.hw01.util.MessageUtil;
import oprpp2.hw01.util.NetworkUtil;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

/**
 * Class representing a client background job for receiving messages from server.
 */
public class ClientWorker extends SwingWorker<Void, Void> {

    /**
     * Server ip
     */
    private final InetAddress ip;

    /**
     * Server port
     */
    private final int port;

    /**
     * Server socket
     */
    private final DatagramSocket socket;

    /**
     * User ID
     */
    private final long uid;

    /**
     * Received acknowledgment messages
     */
    private final BlockingQueue<AckMessage> ackMessages;

    /**
     * Client environment
     */
    private final ClientEnvironment environment;

    /**
     * Worker message count
     */
    private long count = 0;

    /**
     * Creates a new client background listener job.
     * @param ip Server ip
     * @param port Server port
     * @param socket Socket for connecting to the server
     * @param uid Client UID
     * @param ackMessages Acknowledgment messages received by client
     * @param environment Client environment
     */
    public ClientWorker(InetAddress ip, int port, DatagramSocket socket, long uid,
                        BlockingQueue<AckMessage> ackMessages, ClientEnvironment environment) {
        super();
        this.ip = ip;
        this.port = port;
        this.socket = socket;
        this.uid = uid;
        this.ackMessages = ackMessages;
        this.environment = environment;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * <p>
     * Note that this method is executed only once.
     *
     * <p>
     * Note: this method is executed in a background thread.
     *
     * @return the computed result
     */
    @Override
    protected Void doInBackground() {
        while (true) {
            byte[] receiveBuffer = new byte[NetworkUtil.MAX_MESSAGE_LENGTH];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            try {
                this.socket.receive(receivePacket);
            } catch (IOException e) {
                continue;
            }

            byte[] receiveBufferCopy = Arrays.copyOf(receiveBuffer, receiveBuffer.length);

            try (ByteArrayInputStream bis = new ByteArrayInputStream(receiveBufferCopy);
                 DataInputStream dis = new DataInputStream(bis)) {
                byte type = dis.readByte();

                switch (type) {
                    case 2:
                        this.handleAck(receivePacket);
                        break;
                    case 5:
                        this.handleIn(receivePacket);
                        break;
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * Function for handling a received acknowledgement message.
     * @param inPacket Received acknowledgment message packet
     */
    private void handleAck(DatagramPacket inPacket) {
        AckMessage message = (AckMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) return;

        System.out.println("Received an ack message with number " + message.getNumber() + ".");

        while (true) {
            try {
                this.ackMessages.put(message);
                break;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Function for handling a received in message.
     * @param inPacket Received in message packet
     */
    private void handleIn(DatagramPacket inPacket) {
        InMessage message = (InMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) return;

        System.out.println("Received an in message with number " + message.getNumber() + ".");

        if (message.getNumber() > this.count) {
            this.count = message.getNumber();

            this.environment.handleNewMessage(message.getMessage(), message.getFullName(),
                    inPacket.getSocketAddress().toString());
        }

        try {
            NetworkUtil.sendMessage(this.ip, this.port, this.socket, new AckMessage(message.getNumber(), this.uid));
        } catch (Exception ignored) {
        }
    }

}
