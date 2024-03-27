package oprpp2.hw01.util;

import oprpp2.hw01.message.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class MessageUtil {

    public static Message getMessageFromByte(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             DataInputStream dis = new DataInputStream(bis)) {
            byte type = dis.readByte();

            return switch (type) {
                case 1 -> new HelloMessage(dis.readLong(), dis.readUTF(), dis.readLong());
                case 2 -> new AckMessage(dis.readLong(), dis.readLong());
                case 3 -> new ByeMessage(dis.readLong(), dis.readLong());
                case 4 -> new OutMessage(dis.readLong(), dis.readLong(), dis.readUTF());
                case 5 -> new InMessage(dis.readLong(), dis.readUTF(), dis.readUTF());
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }

}
