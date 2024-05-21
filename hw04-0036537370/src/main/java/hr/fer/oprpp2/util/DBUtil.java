package hr.fer.oprpp2.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.servlet.ServletContextEvent;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

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

    public static void initializeData(ComboPooledDataSource cpds) {

        try (Connection con = cpds.getConnection()) {
            if (!isTablePresent(con, "Polls")) {
                PreparedStatement statement = con.prepareStatement(
                        """
                            CREATE TABLE Polls
                                    (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                            title VARCHAR(150) NOT NULL,
                                            message CLOB(2048) NOT NULL
                                    );
                            """
                );

                statement.executeQuery();
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
                                );
                                """
                );

                statement.executeQuery();
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize data.");
        }

    }

    public static boolean isTablePresent(Connection con, String tableName) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();

        try (var rs = metaData.getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        }
    }

}
