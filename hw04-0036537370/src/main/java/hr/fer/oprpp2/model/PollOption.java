package hr.fer.oprpp2.model;

/**
 * Class for modelling a single poll option.
 */
public class PollOption {

    /**
     * Poll option id
     */
    private final long id;

    /**
     * Poll option title
     */
    private final String optionTitle;

    /**
     * Poll option link
     */
    private final String optionLink;

    /**
     * Poll option ID
     */
    private final long pollID;

    /**
     * Poll option votes count
     */
    private final long votesCount;

    /**
     * Creates a single poll option with provided data.
     * @param id Poll option id
     * @param optionTitle Poll option title
     * @param optionLink Poll option link
     * @param pollID Poll option's poll ID
     * @param votesCount Poll option votes count
     */
    public PollOption(long id, String optionTitle, String optionLink, long pollID, long votesCount) {
        this.id = id;
        this.optionTitle = optionTitle;
        this.optionLink = optionLink;
        this.pollID = pollID;
        this.votesCount = votesCount;
    }

    /**
     * Poll option ID getter
     * @return Poll option ID
     */
    public long getId() {
        return id;
    }

    /**
     * Poll option title getter
     * @return Poll option title
     */
    public String getOptionTitle() {
        return optionTitle;
    }

    /**
     * Poll option link getter
     * @return Poll option link
     */
    public String getOptionLink() {
        return optionLink;
    }

    /**
     * Poll option's poll ID getter
     * @return Poll option's poll ID
     */
    public long getPollID() {
        return pollID;
    }

    /**
     * Poll option votes count
     * @return Poll option votes count
     */
    public long getVotesCount() {
        return votesCount;
    }

}
