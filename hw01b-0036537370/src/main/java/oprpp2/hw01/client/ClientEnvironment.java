package oprpp2.hw01.client;

import oprpp2.hw01.message.AckMessage;
import oprpp2.hw01.message.ByeMessage;
import oprpp2.hw01.message.HelloMessage;
import oprpp2.hw01.message.OutMessage;
import oprpp2.hw01.util.NetworkUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Client environment handling main client logic using GUI.
 */
public class ClientEnvironment extends JFrame {

    /**
     * Server ip
     */
    private final InetAddress ip;

    /**
     * Server port
     */
    private final int port;

    /**
     * Server socket
     */
    private final DatagramSocket socket;

    /**
     * User full name
     */
    private final String fullName;

    /**
     * User ID
     */
    private final long uid;

    /**
     * GUI input text field
     */
    private JTextField textField;

    /**
     * GUI output text are
     */
    private JTextArea textArea;

    /**
     * GUI scroll pane
     */
    private JScrollPane pane;

    /**
     * Client message count
     */
    private long count = 0;

    /**
     * Received acknowledgment messages
     */
    private final BlockingQueue<AckMessage> ackMessages = new LinkedBlockingQueue<>();

    /**
     * Client listening worker
     */
    private final ClientWorker worker;

    /**
     * Constructs a new client environment by connecting to the server and retrieving a client UID.
     * @param ip Server ip
     * @param port Server port
     * @param socket Socket for server connection
     * @param fullName Client full name
     */
    public ClientEnvironment(InetAddress ip, int port, DatagramSocket socket, String fullName) {
        super();
        this.ip = ip;
        this.port = port;
        this.socket = socket;
        this.fullName = fullName;

        System.out.println("Trying to connect to the server...");

        AckMessage ackMessage = NetworkUtil.getResponseBySendMessage(this.ip, this.port, this.socket,
                new HelloMessage(count++, fullName, new SecureRandom().nextLong()));

        if (ackMessage == null) {
            throw new ClientException("Could not establish connection with a server.");
        }

        this.uid = ackMessage.getKey();

        System.out.println("Connected to the server with uid " + this.uid + ".");

        this.initGUI();

        this.worker = new ClientWorker(this.ip, this.port, this.socket, this.uid, this.ackMessages, this);
        this.worker.execute();

        System.out.println("Started message listener worker.");
    }

    /**
     * Function for handling sending a message to server by user.
     * @param text Message text
     */
    public void sendTextMessage(String text) {
        this.disableInput();

        new Thread(() -> {
            OutMessage message = new OutMessage(this.count++, this.uid, text);

            NetworkUtil.sendMessageByAckListCheck(this.ip, this.port, this.socket, message, this.ackMessages);

            enableInput();
        }).start();
    }

    /**
     * Function handling closing a connection with a server.
     */
    public void sendCloseMessage() {
        this.disableInput();

        new Thread(() -> {
            ByeMessage message = new ByeMessage(this.count++, this.uid);

            NetworkUtil.sendMessageByAckListCheck(this.ip, this.port, this.socket, message, this.ackMessages);

            this.worker.cancel(true);
        }).start();
    }

    /**
     * Function for handling a new text message received.
     * @param message Message text
     * @param fullName Sender's full name
     * @param address Sender's socket address
     */
    public void handleNewMessage(String message, String fullName, String address) {
        this.textArea.append("[" + address + "] Poruka od korisnika: " + fullName + "\n" + message + "\n\n");

        JScrollBar bar = this.pane.getVerticalScrollBar();
        if (bar != null) bar.setValue(bar.getMaximum());

        this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
    }

    /**
     * Function for enabling a GUI text input.
     */
    public void enableInput() {
        this.textField.setEditable(true);
    }

    /**
     * Function for disabling a GUI text input.
     */
    public void disableInput() {
        this.textField.setEditable(false);
    }

    /**
     * Function for client GUI initialization.
     */
    private void initGUI() {
        this.setTitle("Chat client: " + this.fullName.split(" ")[0]);
        this.setSize(400, 300);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.textField = new JTextField();
        this.textField.addActionListener(e -> {
            String message = this.textField.getText();

            if (!message.isEmpty()) {
                sendTextMessage(message);
                this.textField.setText("");
            }
        });

        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.enableInput();

        this.pane = new JScrollPane(textArea);

        this.add(textField, BorderLayout.NORTH);
        this.add(this.pane, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {

            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e A window event.
             */
            @Override
            public void windowClosing(WindowEvent e) {
                sendCloseMessage();
                dispose();
                System.exit(0);
            }
        });
    }

}
