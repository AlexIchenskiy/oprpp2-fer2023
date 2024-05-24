package hr.fer.oprpp2.dao;

import hr.fer.oprpp2.model.Poll;
import hr.fer.oprpp2.model.PollOption;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

    /**
     * Method for adding a single vote.
     * @param pollOptionId Poll option to be voted for
     */
    void vote(long pollOptionId);

    /**
     * Method for retrieving a list of available polls.
     * @return A list of available polls
     */
    List<Poll> getPolls();

    /**
     * Method for retrieving a list of available poll options for the poll.
     * @param pollId Poll to fetch its poll options
     * @return List of poll options for the specified poll
     */
    List<PollOption> getPollOptions(long pollId);

    /**
     * Method for retrieving a list of available poll options for the poll in specified order.
     * @param pollId Poll to fetch its poll options
     * @param orderBy Parameter to order the poll options by
     * @return List of poll options for the specified poll ordered by a provided parameter
     */
    List<PollOption> getPollOptions(long pollId, String orderBy);

    /**
     * List of winner videos (links) of the poll.
     * @param pollId Poll to fetch its winner links
     * @return List of winner videos (links) for the specified poll
     */
    List<String> getWinnerVideos(long pollId);

    /**
     * Method for retrieving a single poll data by its ID.
     * @param pollId Poll ID
     * @return Poll data
     */
    Poll getPollById(long pollId);

}