package hr.fer.oprpp2.model;

public class Poll {

    private final long id;

    private final String title;

    private final String message;

    public Poll(long id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

}
