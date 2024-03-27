package oprpp2.hw01.server;

import oprpp2.hw01.message.AckMessage;
import oprpp2.hw01.message.InMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.TimeUnit;

public class ServerWorker implements Runnable {

    private final ServerClientData clientData;
    private final DatagramSocket socket;

    public ServerWorker(ServerClientData clientData, DatagramSocket socket) {
        this.clientData = clientData;
        this.socket = socket;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Started worker for user: " + this.clientData.getUid());
        while (true) {
            InMessage message;
            DatagramPacket packet;

            try {
                message = this.clientData.getMessagesToSend().take();
                System.out.println("Got: " + message);
                byte[] data = message.getBytes();

                packet = new DatagramPacket(data, data.length);
            } catch (Exception e) {
                continue;
            }

            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Sending message to " + this.clientData.getAddress());
                    packet.setSocketAddress(this.clientData.getAddress());
                    socket.send(packet);
                    System.out.println("Sent message");
                } catch (Exception e) {
                    continue;
                }

                AckMessage ackMessage;

                try {
                    ackMessage = this.clientData.getAckMessages().poll(5000, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    continue;
                }

                if (ackMessage != null) break;
            }
        }
    }

}
