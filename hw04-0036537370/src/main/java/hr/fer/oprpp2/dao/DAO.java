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

    void vote(long pollOptionId);

    List<Poll> getPolls();

    List<PollOption> getPollOptions(long pollId);

    List<PollOption> getPollOptions(long pollId, String orderBy);

    List<String> getWinnerVideos(long pollId);

    Poll getPollById(long pollId);

}