package hr.fer.oprpp2.dao.sql;

import hr.fer.oprpp2.dao.DAO;
import hr.fer.oprpp2.model.Poll;
import hr.fer.oprpp2.model.PollOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *
 * @author marcupic
 */
public class SQLDAO implements DAO {

    public void vote(long pollOptionId) {
        Connection con = SQLConnectionProvider.getConnection();

        try (PreparedStatement statement = con.prepareStatement(
                "UPDATE PollOptions SET votesCount = votesCount + 1 WHERE id = ?")) {
            statement.setLong(1, pollOptionId);
            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException("Could not vote.");
        }
    }

    public List<PollOption> getPollOptions(long pollId) {
        return this.getPollOptionsInner(pollId, "");
    }

    public List<PollOption> getPollOptions(long pollId, String orderBy) {
        return this.getPollOptionsInner(pollId, "ORDER BY " + orderBy);
    }

    private List<PollOption> getPollOptionsInner(long pollId, String orderBy) {
        Connection con = SQLConnectionProvider.getConnection();

        try (PreparedStatement statement = con.prepareStatement(
                "SELECT id, optionTitle, optionLink, pollID, votesCount FROM PollOptions WHERE pollid = ? "
                + orderBy)) {
            List<PollOption> pollOptions = new ArrayList<>();

            statement.setLong(1, pollId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                pollOptions.add(new PollOption(
                        rs.getLong("id"),
                        rs.getString("optionTitle"),
                        rs.getString("optionLink"),
                        rs.getLong("pollID"),
                        rs.getLong("votesCount")
                ));
            }

            return pollOptions;
        } catch (Exception e) {
            throw new RuntimeException("Could not get poll options list.");
        }
    }

    public List<Poll> getPolls() {
        Connection con = SQLConnectionProvider.getConnection();

        try (PreparedStatement statement = con.prepareStatement(
                "SELECT id, title, message FROM POLLS");
             ResultSet rs = statement.executeQuery()) {
            List<Poll> polls = new ArrayList<>();

            while (rs.next()) {
                polls.add(new Poll(rs.getLong("id"), rs.getString("title"), rs.getString("message")));
            }

            return polls;
        } catch (Exception e) {
            throw new RuntimeException("Could not get polls list.");
        }
    }

    @Override
    public List<String> getWinnerVideos(long pollId) {
        Connection con = SQLConnectionProvider.getConnection();

        String query = """
                SELECT optionLink
                FROM PollOptions
                WHERE pollID = ?
                AND votesCount = (
                    SELECT MAX(votesCount)
                    FROM PollOptions
                    WHERE pollID = ?
                )
                ORDER BY optionTitle
                """;

        try (PreparedStatement statement = con.prepareStatement(query)) {
            List<String> videos = new ArrayList<>();

            statement.setLong(1, pollId);
            statement.setLong(2, pollId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                videos.add(rs.getString("optionLink"));
            }

            return videos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get winner videos list.");
        }
    }

    public Poll getPollById(long pollId) {
        Connection con = SQLConnectionProvider.getConnection();

        try (PreparedStatement statement = con.prepareStatement(
                "SELECT id, title, message FROM POLLS WHERE id = ?")) {
            statement.setLong(1, pollId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Poll(rs.getLong("id"), rs.getString("title"), rs.getString("message"));
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get polls list.");
        }
    }

}