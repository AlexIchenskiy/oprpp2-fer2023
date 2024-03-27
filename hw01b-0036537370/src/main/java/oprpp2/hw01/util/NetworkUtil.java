package oprpp2.hw01.util;

import oprpp2.hw01.client.ClientException;
import oprpp2.hw01.message.AckMessage;
import oprpp2.hw01.message.Message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class NetworkUtil {

    public static final int TIMEOUT_DURATION = 5000;
    // byte + long + long + char * 256 (max message length) + char * 256 (max fullName length)
    public static final int MAX_MESSAGE_LENGTH = 1 + 8 + 8 + 2 * 256 + 2 * 256;

    public static void sendMessage(InetAddress ip, int port, DatagramSocket socket, Message message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);

        packet.setAddress(ip);
        packet.setPort(port);

        socket.send(packet);
    }

    public static void sendMessageByAddress(SocketAddress address, DatagramSocket socket, Message message)
            throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);

        packet.setSocketAddress(address);

        socket.send(packet);
    }

    public static void sendAnswer(DatagramSocket socket, DatagramPacket inPacket, Message message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);

        packet.setSocketAddress(inPacket.getSocketAddress());

        socket.send(packet);
    }

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

            System.out.println("Trying to receive answer.");

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

}