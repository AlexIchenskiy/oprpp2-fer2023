package oprpp2.hw01.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class representing an in message.
 */
public class InMessage extends Message {

    /**
     * User full name
     */
    private final String fullName;

    /**
     * A message text
     */
    private final String message;

    /**
     * Constructs a new in message.
     * @param number Message number
     * @param fullName User full name
     * @param message Message text
     */
    public InMessage(long number, String fullName, String message) {
        super((byte) 5, number);
        this.fullName = fullName;
        this.message = message;
    }

    /**
     * Returns a user full name.
     * @return User full name
     */
    public String getFullName() {
        return fullName;
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
        dos.writeUTF(fullName);
        dos.writeUTF(message);
        dos.close();
        return bos.toByteArray();
    }

}
