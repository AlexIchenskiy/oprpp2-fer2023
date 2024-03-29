package oprpp2.hw01.message;

import java.io.IOException;

/**
 * Abstract class representing a message.
 */
public abstract class Message {

    /**
     * A message code (1 - HELLO, 2 - ACK, 3 - BYE, 4 - OUT, 5 - IN)
     */
    protected byte code;

    /**
     * A message number
     */
    protected long number;

    /**
     * Constructs a new message with a provided type byte and a number.
     * @param code Message type
     * @param number Message number
     */
    public Message(byte code, long number) {
        this.code = code;
        this.number = number;
    }

    /**
     * Returns a message number
     * @return Message number
     */
    public long getNumber() {
        return number;
    }

    /**
     * Generates an array of bytes based on the message content. It always includes message
     * code, message number and any optional additional data.
     *
     * @return Byte array based on the message type and content
     * @throws IOException Should not be thrown, used only by byte data streams.
     */
    abstract public byte[] getBytes() throws IOException;

}
