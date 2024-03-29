package oprpp2.hw01.util;

import oprpp2.hw01.client.ClientException;
import oprpp2.hw01.message.AckMessage;
import oprpp2.hw01.message.Message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Class containing utility methods for working with network.
 */
public class NetworkUtil {

    /**
     * Duration of the waiting timeout
     */
    public static final int TIMEOUT_DURATION = 5000;

    /**
     * Max message length =
     * byte + long + long + char * 256 (max message length) + char * 256 (max fullName length)
     */
    public static final int MAX_MESSAGE_LENGTH = 1 + 8 + 8 + 2 * 256 + 2 * 256;

    /**
     * Function for sending a single message to the server.
     * @param ip Server ip
     * @param port Server port
     * @param socket Socket for connecting to the server
     * @param message Message to be sent
     * @throws IOException Thrown if message is invalid or could not be sent
     */
    public static void sendMessage(InetAddress ip, int port, DatagramSocket socket, Message message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);

        packet.setAddress(ip);
        packet.setPort(port);

        socket.send(packet);
    }

    /**
     * Function for sending an answer to the received packet.
     * @param socket Socket
     * @param inPacket Received packet
     * @param message Message to be sent
     * @throws IOException Thrown if message is invalid or could not be sent
     */
    public static void sendAnswer(DatagramSocket socket, DatagramPacket inPacket, Message message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);

        packet.setSocketAddress(inPacket.getSocketAddress());

        socket.send(packet);
    }

    /**
     * A client-side function for getting a response by sending a message 10 times.
     * @param ip Server ip
     * @param port Server port
     * @param socket Socket for connecting to the server
     * @param message Message to be sent
     * @return Received acknowledgment message
     */
    public static AckMessage getResponseBySendMessage(InetAddress ip, int port, DatagramSocket socket, Message message) {
        byte[] receiveBuffer = new byte[NetworkUtil.MAX_MESSAGE_LENGTH];
        DatagramPacket receivePacket = new DatagramPacket(
                receiveBuffer, receiveBuffer.length
        );

        for (int i = 0; i < 10; i++) {
            try {
                NetworkUtil.sendMessage(ip, port, socket, message);
            } catch (IOException e) {
                return null;
            }

            try {
                socket.setSoTimeout(NetworkUtil.TIMEOUT_DURATION);
                socket.receive(receivePacket);
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                return null;
            }

            break;
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(receiveBuffer);
             DataInputStream dis = new DataInputStream(bis)) {
            byte testByte = dis.readByte();

            if (testByte == 0) {
                throw new ClientException("Cannot connect to the server. Exiting.");
            }

            return new AckMessage(dis.readLong(), dis.readLong());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Function for getting a response from an acknowledgment list by sending a message 10 times.
     * @param ip Server ip
     * @param port Server port
     * @param socket Socket for connecting to the server
     * @param message Message to be sent
     * @param ackMessages List of acknowledgment messages received
     */
    public static void sendMessageByAckListCheck(InetAddress ip, int port, DatagramSocket socket,
                                                       Message message, BlockingQueue<AckMessage> ackMessages) {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Trying to send a message with number " + message.getNumber() + ".");
                NetworkUtil.sendMessage(ip, port, socket, message);
            } catch (Exception e) {
                continue;
            }

            AckMessage ackMessage;

            try {
                ackMessage = ackMessages.poll(5000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                continue;
            }

            if (ackMessage != null && ackMessage.getNumber() == message.getNumber()) {
                System.out.println("Got ack message with number " + ackMessage.getNumber() + ".");
                break;
            }
        }
    }

}
