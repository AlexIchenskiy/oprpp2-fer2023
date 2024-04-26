package hr.fer.oprpp2.util.glasanje;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Utility functions for manipulating with voting data.
 */
public class GlasanjeUtil {

    /**
     * Save a single vote.
     * @param req HTTP Request
     * @throws IOException File manipulation exception
     */
    public static void vote(HttpServletRequest req) throws IOException {
        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Integer id = Integer.parseInt(req.getParameter("id"));
        StringBuilder fileContent = new StringBuilder();

        boolean exists = false;

        for (String line : Files.readAllLines(Path.of(fileName))) {
            String[] parts = line.split("\t");
            int bandId = Integer.parseInt(parts[0]);
            if (bandId != id) {
                fileContent.append(line).append("\n");
                continue;
            }

            exists = true;
            fileContent.append(id).append("\t").append(Integer.parseInt(parts[1]) + 1).append("\n");
        }

        if (exists) {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(fileContent.toString());
            writer.close();
        } else {
            FileWriter writer = new FileWriter(fileName, true);
            writer.append(id.toString()).append("\t").append("1").append("\n");
            writer.close();
        }
    }

    /**
     * Get all data unsorted
     * @param req HTTP Request
     * @return Unsorted list of bands data
     * @throws IOException File manipulation exception
     */
    public static List<GlasanjeData> getData(HttpServletRequest req) throws IOException {
        String rezultati = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String definicija = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

        List<GlasanjeData> data = new ArrayList<>();

        for (String line : Files.readAllLines(Path.of(definicija))) {
            String[] parts = line.split("\t");
            data.add(new GlasanjeData(Integer.parseInt(parts[0]), parts[1], parts[2], 0));
        }

        Map<String, Integer> resByName = new HashMap<>();

        for (String line : Files.readAllLines(Path.of(rezultati))) {
            String[] parts = line.split("\t");

            data.forEach((entry) -> {
                if (entry.getId() == Integer.parseInt(parts[0])) entry.setScore(Integer.parseInt(parts[1]));
            });
        }

        return data;
    }

    /**
     * Get all data sorted by band ids
     * @param req HTTP Request
     * @return List of bands data sorted by ids
     * @throws IOException File manipulation exception
     */
    public static List<GlasanjeData> getDataSortedById(HttpServletRequest req) throws IOException {
        return getData(req).stream().sorted(Comparator.comparingInt(GlasanjeData::getId)).toList();
    }

    /**
     * Get all data sorted by band scores
     * @param req HTTP Request
     * @return List of bands data sorted by scores
     * @throws IOException File manipulation exception
     */
    public static List<GlasanjeData> getDataSortedByScore(HttpServletRequest req) throws IOException {
        return getData(req).stream().sorted((entry1, entry2) -> entry2.getScore() - entry1.getScore()).toList();
    }

    /**
     * Get videos of the winner bands
     * @param req HTTP Request
     * @return Videos of the winner bands
     * @throws IOException File manipulation exception
     */
    public static List<String> getWinnerVideos(HttpServletRequest req) throws IOException {
        List<GlasanjeData> data = getDataSortedByScore(req);
        Integer max = data.get(0).getScore();

        return data.stream().filter((entry) -> entry.getScore() >= max).map(GlasanjeData::getVideo).toList();
    }

}
