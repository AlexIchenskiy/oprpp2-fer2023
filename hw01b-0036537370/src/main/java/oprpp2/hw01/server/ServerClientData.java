package oprpp2.hw01.server;

import oprpp2.hw01.message.AckMessage;
import oprpp2.hw01.message.InMessage;

import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerClientData {

    private final long uid;

    private final String fullName;

    private final SocketAddress address;

    private final BlockingQueue<InMessage> messagesToSend = new LinkedBlockingQueue<>();
    private final BlockingQueue<AckMessage> ackMessages = new LinkedBlockingQueue<>();

    public ServerClientData(long uid, String fullName, SocketAddress address) {
        this.uid = uid;
        this.fullName = fullName;
        this.address = address;
    }

    public long getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public BlockingQueue<InMessage> getMessagesToSend() {
        return messagesToSend;
    }

    public BlockingQueue<AckMessage> getAckMessages() {
        return ackMessages;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public void handleNewMessage(InMessage message) throws InterruptedException {
        messagesToSend.put(message);
    }

    public void handleNewAckMessage(AckMessage message) throws InterruptedException {
        ackMessages.put(message);
    }

}
