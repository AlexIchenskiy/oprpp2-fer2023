package oprpp2.hw01.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OutMessage extends Message {

    private long key;
    private String message;

    public OutMessage(long number, long key, String message) {
        super((byte) 4, number);
        this.key = key;
        this.message = message;
    }

    public long getKey() {
        return key;
    }

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
