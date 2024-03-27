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

public class ServerEnvironment {

    private final InetAddress ip;
    private final int port;
    private final DatagramSocket socket;

    private final Map<Long, Long> uidByKey = new HashMap<>();

    private final Map<Long, ServerClientData> userById = new HashMap<>();

    private long count = 0;

    public ServerEnvironment(int port) {
        this.ip = (InetAddress) null;
        this.port = port;

        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            throw new ServerException("Could not initialize server.", e);
        }
    }

    public void listen() {
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

                System.out.println(type);

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
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void handleHello(DatagramPacket inPacket) {
        HelloMessage message = (HelloMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        if (uidByKey.get(message.getKey()) != null) {
            try {
                NetworkUtil.sendMessage(this.ip, this.port, this.socket,
                        new AckMessage(message.getNumber(), uidByKey.get(message.getKey())));
            } catch (Exception e) {
                return;
            }

            return;
        }

        Long uid = new SecureRandom().nextLong();

        uidByKey.put(message.getKey(), uid);
        userById.put(uid, new ServerClientData(uid, message.getFullName(), inPacket.getSocketAddress()));

        new Thread(new ServerWorker(userById.get(uid), this.socket)).start();

        try {
            NetworkUtil.sendAnswer(this.socket, inPacket,
                    new AckMessage(message.getNumber(), uidByKey.get(message.getKey())));
        } catch (Exception e) {
            return;
        }
    }

    private void handleAck(DatagramPacket inPacket) {
        AckMessage message = (AckMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        ServerClientData clientData = userById.get(message.getKey());

        while (true) {
            try {
                clientData.handleNewAckMessage(message);
                break;
            } catch (Exception ignored) {
            }
        }
    }

    private void handleBye(DatagramPacket inPacket) {
        ByeMessage message = (ByeMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        userById.remove(message.getKey());

        try {
            NetworkUtil.sendAnswer(this.socket, inPacket,
                    new AckMessage(message.getNumber(), message.getKey()));
        } catch (Exception e) {
            return;
        }
    }

    private void handleOut(DatagramPacket inPacket) {
        OutMessage message = (OutMessage) MessageUtil.getMessageFromByte(inPacket.getData());

        if (message == null) {
            return;
        }

        this.userById.forEach((key, value) -> {
            while (true) {
                try {
                    value.handleNewMessage(
                            new InMessage(count,
                                    this.userById.get(message.getKey()).getFullName(), message.getMessage()));
                    break;
                } catch (Exception ignored) {
                }
            }
        });

        try {
            NetworkUtil.sendAnswer(this.socket, inPacket,
                    new AckMessage(message.getNumber(), uidByKey.get(message.getKey())));
        } catch (Exception e) {
            return;
        }

        this.count++;
    }

}
