package oprpp2.hw01.server;

import oprpp2.hw01.message.AckMessage;
import oprpp2.hw01.message.InMessage;

import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Stored data for client.
 */
public class ServerClientData {

    /**
     * User ID
     */
    private final long uid;

    /**
     * User full name
     */
    private final String fullName;

    /**
     * User socket address
     */
    private final SocketAddress address;

    /**
     * List of messages to be sent to the user
     */
    private final BlockingQueue<InMessage> messagesToSend = new LinkedBlockingQueue<>();

    /**
     * List of received acknowledgment messages from user
     */
    private final BlockingQueue<AckMessage> ackMessages = new LinkedBlockingQueue<>();

    /**
     * Client message count
     */
    private long count = 1;

    /**
     * Creates a new user representation.
     * @param uid User ID
     * @param fullName User full name
     * @param address User socket address
     */
    public ServerClientData(long uid, String fullName, SocketAddress address) {
        this.uid = uid;
        this.fullName = fullName;
        this.address = address;
    }

    /**
     * Returns a user ID.
     * @return User ID
     */
    public long getUid() {
        return uid;
    }

    /**
     * Returns a user full name.
     * @return User full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns messages to send.
     * @return List of messages to be sent
     */
    public BlockingQueue<InMessage> getMessagesToSend() {
        return messagesToSend;
    }

    /**
     * Returns acknowledgment messages.
     * @return List of received acknowledgment messages
     */
    public BlockingQueue<AckMessage> getAckMessages() {
        return ackMessages;
    }

    /**
     * Returns a user socket address.
     * @return User socket address
     */
    public SocketAddress getAddress() {
        return address;
    }

    /**
     * Returns a user message count.
     * @return User message count
     */
    public long getCount() {
        return count;
    }

    /**
     * Put a new message into sending list.
     * @param message Message to be sent
     * @throws InterruptedException Thrown if could not put a value into the list
     */
    public void handleNewMessage(InMessage message) throws InterruptedException {
        messagesToSend.put(message);
    }

    /**
     * Put a new received message into acknowledgment list.
     * @param message Message to be sent
     * @throws InterruptedException Thrown if could not a value into the list
     */
    public void handleNewAckMessage(AckMessage message) throws InterruptedException {
        ackMessages.put(message);
    }

    /**
     * A message server count.
     * @param count New count value
     */
    public void setCount(long count) {
        this.count = count;
    }

}
