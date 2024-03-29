package oprpp2.hw01.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class representing a bye message.
 */
public class ByeMessage extends Message {

    /**
     * A message key
     */
    private final long key;

    /**
     * Constructs a new bye message.
     * @param number Message number
     * @param key Message key (UID)
     */
    public ByeMessage(long number, long key) {
        super((byte) 3, number);
        this.key = key;
    }

    /**
     * Returns a message key.
     * @return Message key
     */
    public long getKey() {
        return key;
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
        dos.close();
        return bos.toByteArray();
    }

}
