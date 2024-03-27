package oprpp2.hw01.client;

/**
 * Class representing a client exception that is thrown only if could not connect to the server.
 * It can be handled by application terminating.
 */
public class ClientException extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ClientException(String message) {
        super(message);
    }

}
