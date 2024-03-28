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
import java.util.concurrent.TimeUnit;

public class ClientEnvironment extends JFrame {

    private final InetAddress ip;
    private final int port;
    private final DatagramSocket socket;
    private final String fullName;
    private final long uid;

    private JTextField textField;
    private JTextArea textArea;
    private JScrollPane pane;

    private long count = 0;

    private final BlockingQueue<AckMessage> ackMessages = new LinkedBlockingQueue<>();
    private final ClientWorker worker;

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

    public void sendTextMessage(String text) {
        this.disableInput();

        new Thread(() -> {
            OutMessage message = new OutMessage(this.count++, this.uid, text);

            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Trying to send an out text message: \"" + text
                            + "\" with number " + message.getNumber() + ".");
                    NetworkUtil.sendMessage(this.ip, this.port, this.socket, message);
                } catch (Exception e) {
                    continue;
                }

                AckMessage ackMessage;

                try {
                    ackMessage = this.ackMessages.poll(5000, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    continue;
                }

                if (ackMessage != null && ackMessage.getNumber() == message.getNumber()) {
                    System.out.println("Got ack message with number " + ackMessage.getNumber() + ".");
                    break;
                }
            }

            enableInput();
        }).start();
    }

    public void sendCloseMessage() {
        this.disableInput();

        new Thread(() -> {
            ByeMessage message = new ByeMessage(this.count++, this.uid);

            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Trying to send a bye message with number " + message.getNumber() + ".");
                    NetworkUtil.sendMessage(this.ip, this.port, this.socket, message);
                } catch (Exception e) {
                    continue;
                }

                AckMessage ackMessage;

                try {
                    ackMessage = this.ackMessages.poll(5000, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    continue;
                }

                if (ackMessage != null && ackMessage.getNumber() == message.getNumber()) {
                    System.out.println("Got ack message with number " + ackMessage.getNumber() + ".");
                    break;
                }
            }
        }).start();

        this.worker.cancel(true);
    }

    public void handleNewMessage(String message, String fullName, String address) {
        this.textArea.append("[" + address + "] Poruka od korisnika: " + fullName + "\n" + message + "\n\n");

        JScrollBar bar = this.pane.getVerticalScrollBar();
        if (bar != null) bar.setValue(bar.getMaximum());
    }

    public void enableInput() {
        this.textField.setEditable(true);
    }

    public void disableInput() {
        this.textField.setEditable(false);
    }

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
