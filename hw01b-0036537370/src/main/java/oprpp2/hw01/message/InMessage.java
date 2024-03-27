package oprpp2.hw01.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InMessage extends Message {

    private String fullName;
    private String message;

    public InMessage(long number, String fullName, String message) {
        super((byte) 5, number);
        this.fullName = fullName;
        this.message = message;
    }

    public String getFullName() {
        return fullName;
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
        dos.writeUTF(fullName);
        dos.writeUTF(message);
        dos.close();
        return bos.toByteArray();
    }

}
