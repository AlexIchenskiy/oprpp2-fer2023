package oprpp2.hw01.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame extends JFrame {

    private final ClientEnvironment environment;

    private final String name;

    private JTextField textField;
    private JTextArea textArea;

    public ClientFrame(ClientEnvironment environment, String name, ClientWorker worker) {
        this.environment = environment;
        this.name = name;

        worker.setFrame(this);

        this.initGUI();
    }

    private void initGUI() {
        this.setTitle("Chat client: " + this.name);
        this.setSize(400, 300);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.textField = new JTextField();
        this.textField.addActionListener(e -> this.handleSendMessage());

        this.textArea = new JTextArea();
        this.textArea.setEditable(false);

        this.add(textField, BorderLayout.NORTH);
        this.add(new JScrollPane(textArea), BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {

            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e A window event.
             */
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    environment.sendCloseMessage();
                } catch (ClientException exception) {
                    System.out.println(exception.getMessage());
                    System.exit(0);
                }
            }
        });
    }

    public void handleReceiveMessage(String address, String fullName, String message) {
        this.textArea.append("[" + address + "] Poruka od korisnika: " + fullName + "\n" + message + "\n\n");
    }

    public void disableInput() {
        this.textField.setEnabled(false);
    }

    public void enableInput() {
        this.textField.setEnabled(true);
    }

    private void handleSendMessage() {
        String message = this.textField.getText();

        if (!message.isEmpty()) {
            try {
                this.environment.sendTextMessage(message);
            } catch (ClientException exception) {
                System.out.println(exception.getMessage());
                System.exit(0);
            }
            this.textField.setText("");
        }
    }

}
