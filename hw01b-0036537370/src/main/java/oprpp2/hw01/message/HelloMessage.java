package oprpp2.hw01.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class representing a hello message.
 */
public class HelloMessage extends Message {

    /**
     * User full name
     */
    private final String fullName;

    /**
     * A message key
     */
    private final long key;

    /**
     * Constructs a new hello message.
     * @param number Message number
     * @param fullName User full name
     * @param key Message key (random key)
     */
    public HelloMessage(long number, String fullName, long key) {
        super((byte) 1, number);
        this.fullName = fullName;
        this.key = key;
    }

    /**
     * Returns a user full name.
     * @return User full name
     */
    public String getFullName() {
        return fullName;
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
        dos.writeUTF(fullName);
        dos.writeLong(key);
        dos.close();
        return bos.toByteArray();
    }

}
