package hr.fer.oprpp2.model;

/**
 * Class for modelling a single poll.
 */
public class Poll {

    /**
     * Poll ID
     */
    private final long id;

    /**
     * Poll title
     */
    private final String title;

    /**
     * Poll message
     */
    private final String message;

    /**
     * Creates a new poll with an id, title and a message.
     * @param id Poll ID
     * @param title Poll title
     * @param message Poll message
     */
    public Poll(long id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    /**
     * Poll ID getter.
     * @return Poll ID
     */
    public long getId() {
        return id;
    }

    /**
     * Poll title getter.
     * @return Poll title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Poll message getter.
     * @return Poll message
     */
    public String getMessage() {
        return message;
    }

}
