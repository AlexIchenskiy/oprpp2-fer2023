package oprpp2.hw01.server;

/**
 * A UDP server main class.
 */
public class Main {

    /**
     * Method to run a UDP server.
     * @param args Numeric port of the server to be run on like 6000
     */
    public static void main(String[] args) {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("Please provide a valid numeric port!");
            return;
        }

        try {
            ServerEnvironment environment = new ServerEnvironment(port);

            environment.listen();
        } catch (ServerException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

}
