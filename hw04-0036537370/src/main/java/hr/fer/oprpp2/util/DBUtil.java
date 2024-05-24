package hr.fer.oprpp2.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import hr.fer.oprpp2.model.Poll;
import hr.fer.oprpp2.model.PollOption;

import javax.servlet.ServletContextEvent;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Database util class.
 */
public class DBUtil {

    /**
     * Method for generating a connection URL for the database.
     * @param propertiesPath Path of the properties file
     * @param sce Servlet context
     * @return A database connection url
     */
    public static String getConnectionUrl(String propertiesPath, ServletContextEvent sce) {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(sce.getServletContext().getRealPath(propertiesPath))) {
            properties.load(fis);
            return String.format("jdbc:derby://%s:%d/%s;user=%s;password=%s",
                    properties.get("host"),
                    Integer.parseInt(properties.get("port").toString()),
                    properties.get("name"),
                    properties.get("user"),
                    properties.get("password"));
        } catch (Exception e) {
            throw new RuntimeException("Could not load connection URL.");
        }
    }

    /**
     * Method for populating database with some data.
     * @param cpds Data source
     */
    public static void initializeData(ComboPooledDataSource cpds) {

        try (Connection con = cpds.getConnection()) {
            List<Long> generatedIds = new ArrayList<>();

            if (!isTablePresent(con, "Polls")) {
                PreparedStatement statement = con.prepareStatement(
                        """
                            CREATE TABLE Polls
                                    (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                            title VARCHAR(150) NOT NULL,
                                            message CLOB(2048) NOT NULL
                                    )
                            """
                );

                statement.execute();

                List<Poll> polls = List.of(
                        new Poll(1, "Glasanje za omiljeni bend:",
                                "Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako\n" +
                                        "biste glasali!"),
                        new Poll(2, "Glasanje za najbolji prog. jezik:",
                                "Od sljedećih jezika, koji Vam je bend najdraži? Kliknite na link kako\n" +
                                        "                                        \"biste glasali!")
                );

                for (Poll poll : polls) {
                    PreparedStatement statement1 = con.prepareStatement(
                            "INSERT INTO Polls (title, message) VALUES ( ?, ? )");

                    statement1.setString(1, poll.getTitle());
                    statement1.setString(2, poll.getMessage());

                    statement1.executeUpdate();

                    try (ResultSet rs = statement1.getGeneratedKeys()) {
                        if (rs != null) {
                            while (rs.next()) {
                                generatedIds.add(rs.getLong(1));
                            }
                        } else {
                            generatedIds.add(1L);
                            generatedIds.add(2L);
                        }
                    }
                }
            }

            if (!isTablePresent(con, "PollOptions")) {
                PreparedStatement statement = con.prepareStatement(
                        """
                                CREATE TABLE PollOptions
                                    (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                    optionTitle VARCHAR(100) NOT NULL,
                                    optionLink VARCHAR(150) NOT NULL,
                                    pollID BIGINT,
                                    votesCount BIGINT,
                                    FOREIGN KEY (pollID) REFERENCES Polls(id)
                                )
                                """
                );

                statement.execute();

                List<PollOption> options = List.of(
                        new PollOption(1, "The Beatles",
                                "https://www.youtube.com/watch?v=z9ypq6_5bsg", 1, 0),
                        new PollOption(2, "The Platters",
                                "https://www.youtube.com/watch?v=H2di83WAOhU", 1, 0),
                        new PollOption(3, "The Beach Boys",
                                "https://www.youtube.com/watch?v=2s4slliAtQU", 1, 0),
                        new PollOption(4, "The Four Seasons",
                                "https://www.youtube.com/watch?v=y8yvnqHmFds", 1, 0),
                        new PollOption(5, "The Marcels",
                                "https://www.youtube.com/watch?v=qoi3TH59ZEs", 1, 0),
                        new PollOption(6, "The Everly Brothers",
                                "https://www.youtube.com/watch?v=tbU3zdAgiX8", 1, 0),
                        new PollOption(7, "The Mamas And The Papas",
                                "https://www.youtube.com/watch?v=N-aK6JnyFmk", 1, 0),

                        new PollOption(8, "Java 8",
                                "https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html", 1, 0),
                        new PollOption(9, "Java 9",
                                "https://www.oracle.com/java/technologies/javase/javase9-archive-downloads.html", 1, 0),
                        new PollOption(10, "Java 11",
                                "https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html", 1, 0),
                        new PollOption(12, "Java 17",
                                "https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html", 1, 0),
                        new PollOption(12, "Java 21",
                                "https://www.oracle.com/java/technologies/downloads/#java21", 1, 0)
                );

                for (PollOption option : options) {
                    PreparedStatement statement1 = con.prepareStatement(
                            "INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) " +
                                    "VALUES ( ?, ?, ?, ? )");

                    statement1.setString(1, option.getOptionTitle());
                    statement1.setString(2, option.getOptionLink());
                    statement1.setLong(3, option.getPollID());
                    statement1.setLong(4, option.getVotesCount());

                    statement1.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize data.");
        }

    }

    /**
     * Method for checking if the specified table is present in the database.
     * @param con Connection
     * @param tableName Name of the table to check
     * @return True if the specified table exists in the connected database
     * @throws SQLException SQL exception
     */
    public static boolean isTablePresent(Connection con, String tableName) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();

        try (var rs = metaData.getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        }
    }

}
