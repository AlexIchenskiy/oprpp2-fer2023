package hr.fer.oprpp2.model;

public class PollOption {

    private final long id;

    private final String optionTitle;

    private final String optionLink;

    private final long pollID;

    private final long votesCount;

    public PollOption(long id, String optionTitle, String optionLink, long pollID, long votesCount) {
        this.id = id;
        this.optionTitle = optionTitle;
        this.optionLink = optionLink;
        this.pollID = pollID;
        this.votesCount = votesCount;
    }

    public long getId() {
        return id;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public String getOptionLink() {
        return optionLink;
    }

    public long getPollID() {
        return pollID;
    }

    public long getVotesCount() {
        return votesCount;
    }

}
