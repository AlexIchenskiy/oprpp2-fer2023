package oprpp2.hw01.server;

import oprpp2.hw01.message.*;
import oprpp2.hw01.util.MessageUtil;
import oprpp2.hw01.util.NetworkUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Server environment handling main server logic.
 */
public class ServerEnvironment {

    /**
     * Server IP
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
     * Map of user IDs by random key
     */
    private final Map<Long, Long> uidByKey = new HashMap<>();

    /**
     * Map of user data by user ID
     */
    private final Map<Long, ServerClientData> userById = new HashMap<>();

    /**
     * Initializes a server on the provided port.
     * @param port Server port
     */
    public ServerEnvironment(int port) {
        this.ip = (InetAddress) null;
        this.port = port;

        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            throw new ServerException("Could not initialize server.", e);
        }

        System.out.println("Started server.");
    }

    /**
     * Method for listening for any incoming messages.
     */
    public void listen() {
        System.out.println("Started listening for messages.");

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
                    case 1:
                        new Thread(() -> handleHello(receivePacket)).start();
                        break;
                    case 2:
                        new Thread(() -> handleAck(receivePacket)).start();
                        break;
                    case 3:
                        new Thread(() -> handleBye(receivePacket)).start();
                        break;
                    case 4:
                        new Thread(() -> handleOut(receivePacket)).start();
                        break;
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Function for handling a received hello message.
     * @param inPacket Received hello message packet
     */
    private void handleHello(DatagramPacket inPacket) {
        HelloMessage message = (HelloMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        System.out.println("Got hello message with number " + message.getNumber()
                + " from user on address " + inPacket.getSocketAddress() + " with key "
                + message.getKey() + ".");

        if (uidByKey.get(message.getKey()) != null) {
            try {
                NetworkUtil.sendMessage(this.ip, this.port, this.socket,
                        new AckMessage(message.getNumber(), uidByKey.get(message.getKey())));
                System.out.println("User with key " + message.getKey() + " already exists, sending ack with number "
                        + message.getNumber() + ".");
            } catch (Exception e) {
                return;
            }

            return;
        }

        Long uid = new SecureRandom().nextLong();

        uidByKey.put(message.getKey(), uid);
        userById.put(uid, new ServerClientData(uid, message.getFullName(), inPacket.getSocketAddress()));

        new Thread(new ServerWorker(userById.get(uid), this.socket)).start();
        System.out.println("Created worker for user with key " + message.getKey() + " and a new uid " + uid + ".");

        try {
            NetworkUtil.sendAnswer(this.socket, inPacket,
                    new AckMessage(message.getNumber(), uidByKey.get(message.getKey())));
            System.out.println("Sent ack message with number " + message.getNumber()
                    + " to user with uid " + uid + ".");
        } catch (Exception ignored) {
        }
    }

    /**
     * Function for handling a received acknowledgment message.
     * @param inPacket Received acknowledgment message packet
     */
    private void handleAck(DatagramPacket inPacket) {
        AckMessage message = (AckMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        ServerClientData data = this.userById.get(message.getKey());

        if (message.getNumber() > data.getCount()) {
            data.setCount(message.getNumber());
        }

        System.out.println("Got ack message with number " + message.getNumber()
                + " from user on address " + inPacket.getSocketAddress() + " with key "
                + message.getKey() + ".");

        ServerClientData clientData = userById.get(message.getKey());

        while (true) {
            try {
                clientData.handleNewAckMessage(message);
                break;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Function for handling a received bye message.
     * @param inPacket Received bye message packet
     */
    private void handleBye(DatagramPacket inPacket) {
        ByeMessage message = (ByeMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        System.out.println("Got bye message with number " + message.getNumber()
                + " from user on address " + inPacket.getSocketAddress() + " with key "
                + message.getKey() + ".");

        userById.remove(message.getKey());

        try {
            NetworkUtil.sendAnswer(this.socket, inPacket,
                    new AckMessage(message.getNumber(), message.getKey()));
            System.out.println("Sent ack message with number " + message.getNumber()
                    + " to user with uid " + message.getKey() + ".");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Function for handling a received out message.
     * @param inPacket Received out message packet
     */
    private void handleOut(DatagramPacket inPacket) {
        OutMessage message = (OutMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        ServerClientData data = this.userById.get(message.getKey());

        if (message.getNumber() > data.getCount()) {
            return;
        }

        System.out.println("Got out message with number " + message.getNumber()
                + " and text: \"" + message.getMessage() + "\" from user on address "
                + inPacket.getSocketAddress() + " with key "
                + message.getKey() + ".");

        this.userById.forEach((key, value) -> {
            try {
                value.handleNewMessage(
                        new InMessage(value.getCount(),
                                this.userById.get(message.getKey()).getFullName(), message.getMessage()));
                value.setCount(value.getCount() + 1);
            } catch (Exception ignored) {
            }
        });

        try {
            NetworkUtil.sendAnswer(this.socket, inPacket,
                    new AckMessage(message.getNumber(), message.getKey()));
            System.out.println("Sent ack message with number " + message.getNumber()
                    + " to user with uid " + message.getKey() + ".");
        } catch (Exception ignored) {
        }
    }

}
