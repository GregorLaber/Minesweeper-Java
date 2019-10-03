package main.java.minesweeper.highscore;

class Player {

    private int rank;
    private String name;
    private String time;

    public Player(int rank, String name, String time) {
        this.rank = rank;
        this.name = name;
        this.time = time;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
