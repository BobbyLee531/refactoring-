package theater;

/**
 * Represents a play with a name and type.
 * @null no fields in this class should be null
 */
public class Play {

    /**
     * Name of the play.
     * @null never
     */
    private String name;

    /**
     * Type of the play, e.g. "tragedy", "comedy".
     * @null never
     */
    private String type;

    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
