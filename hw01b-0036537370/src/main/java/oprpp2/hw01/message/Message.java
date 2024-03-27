package oprpp2.hw01.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Message {

    protected byte code;
    protected long number;

    public Message(byte code, long number) {
        this.code = code;
        this.number = number;
    }

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
