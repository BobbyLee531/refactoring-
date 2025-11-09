package theater;

/**
 * Class representing a performance of a play.
 */
public class Performance {

    private String playID;
    private int audience;

    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    // Getter for playID
    public String getPlayID() {
        return playID;
    }

    // Setter for playID
    public void setPlayID(String playID) {
        this.playID = playID;
    }

    // Getter for audience
    public int getAudience() {
        return audience;
    }

    // Setter for audience
    public void setAudience(int audience) {
        this.audience = audience;
    }
}
