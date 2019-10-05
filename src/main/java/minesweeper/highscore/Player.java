package main.java.minesweeper.highscore;

/**
 * Class for storing in Highscore File
 */
class Player {

    private int rank;
    private final String name;
    private final String time;
    private final int minutes;
    private final int seconds;

    Player(int rank, String name, String time) {
        this.rank = rank;
        this.name = name;
        this.time = time;

        String[] line = time.split("[:]");
        this.minutes = Integer.parseInt(line[0]);
        this.seconds = Integer.parseInt(line[1]);
    }

    /**
     * Getter for Rank
     *
     * @return rank in Highscore
     */
    private int getRank() {
        return rank;
    }

    /**
     * Setter for Rank
     *
     * @param rank in Highscore
     */
    void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Getter for Name
     *
     * @return Name of the Player
     */
    private String getName() {
        return name;
    }

    /**
     * Getter for Time Format(00:00)
     *
     * @return time as String
     */
    private String getTime() {
        return time;
    }

    /**
     * Getter for Minutes of the Time
     *
     * @return int minutes
     */
    int getMinutes() {
        return minutes;
    }

    /**
     * Getter for Seconds of the Time
     *
     * @return int seconds
     */
    int getSeconds() {
        return seconds;
    }

    /**
     * Method is used to write Player to the Highscore File
     *
     * @return Player in Format Player(rank|name|time(mm:ss))
     */
    @Override
    public String toString() {
        return (getRank() + "|" + getName() + "|" + getTime());
    }
}
