package oprpp2.hw01.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class representing an out message.
 */
public class OutMessage extends Message {

    /**
     * A message key
     */
    private final long key;

    /**
     * A message text
     */
    private final String message;

    /**
     * Constructs a new out message.
     * @param number Message number
     * @param key Message key
     * @param message Message text
     */
    public OutMessage(long number, long key, String message) {
        super((byte) 4, number);
        this.key = key;
        this.message = message;
    }

    /**
     * Returns a message key.
     * @return Message key
     */
    public long getKey() {
        return key;
    }

    /**
     * Returns a message text.
     * @return Message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Generates an array of bytes based on the message content. It always includes message
     * code, message number and any optional additional data.
     *
     * @return Byte array based on the message type and content
     * @throws IOException Should not be thrown, used only by byte data streams.
     */
    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeByte(this.code);
        dos.writeLong(this.number);
        dos.writeLong(key);
        dos.writeUTF(message);
        dos.close();
        return bos.toByteArray();
    }

}
