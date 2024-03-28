package oprpp2.hw01.client;

import javax.swing.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        String fullName, name, surname;
        int port;
        InetAddress ip;
        DatagramSocket socket;

        try {
            ip = InetAddress.getByName(args[0]);
            port = Integer.parseInt(args[1]);
            fullName = args[2];
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please provide three arguments: ip, port and your full name!");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid numeric port!");
            return;
        } catch (UnknownHostException e) {
            System.out.println("Please provide a valid IP address!");
            return;
        }

        try {
            String[] parts = fullName.split(" ");
            name = parts[0];
            surname = parts[1];
        } catch (Exception e) {
            System.out.println("Please provide your full name like \"Name Surname\"!");
            return;
        }

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Could not create a socket.");
            return;
        }

        try {
            ClientEnvironment environment = new ClientEnvironment(ip, port, socket, fullName);
            SwingUtilities.invokeLater(() -> environment.setVisible(true));
        } catch (ClientException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

}
