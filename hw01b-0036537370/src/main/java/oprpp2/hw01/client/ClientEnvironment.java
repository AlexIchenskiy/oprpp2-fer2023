package oprpp2.hw01.client;

import oprpp2.hw01.message.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.SecureRandom;

public class ClientEnvironment {

    private final InetAddress ip;
    private final int port;
    private final DatagramSocket socket;
    private final ClientWorker worker;
    private ClientFrame frame;

    private int count = 0;

    private long uid;

    public ClientEnvironment(InetAddress ip, int port, DatagramSocket socket, String fullName, ClientWorker worker) {
        this.ip = ip;
        this.port = port;
        this.socket = socket;
        this.worker = worker;

        this.worker.setPendingMessage(new HelloMessage(count++, fullName, new SecureRandom().nextLong()));
        new Thread(worker::listen).start();
    }

    public void sendTextMessage(String text) {
        if (this.frame != null) this.frame.disableInput();
        this.worker.setPendingMessage(new OutMessage(this.count++, this.uid, text));
    }

    public void sendCloseMessage() {
        if (this.frame != null) this.frame.disableInput();
        this.worker.setPendingMessage(new ByeMessage(this.count++, this.uid));
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setFrame(ClientFrame frame) {
        this.frame = frame;
    }

}
