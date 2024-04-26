package hr.fer.oprpp2.util.glasanje;

/**
 * A class for storing the voting data for a single band.
 */
public class GlasanjeData {

    private final Integer id;

    private final String name;

    private final String video;

    private Integer score = null;

    public GlasanjeData(Integer id, String name, String video, Integer score) {
        this(id, name, video);
        this.score = score;
    }

    public GlasanjeData(Integer id, String name, String video) {
        this.id = id;
        this.name = name;
        this.video = video;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVideo() {
        return video;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}
