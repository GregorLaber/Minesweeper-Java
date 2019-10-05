package main.java.minesweeper.highscore;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Class for Highscore
 */
class ModelHighscore {

    private List<String> highscoreList = new ArrayList<>();
    private List<Player> playerList = new ArrayList<>();

    ModelHighscore() {
    }

    /**
     * Method to create the PlayerList.
     * Either a new List of Players or from the Content of the Highscore File.
     */
    private void initializePlayerList() {

        if (highscoreList.isEmpty()) {

            //Create PlayerList without HighscoreList
            for (int i = 0; i < 10; i++) {
                playerList.add(new Player(i + 1, " ", " "));
            }

        } else if (highscoreList.size() == 10) {

            //Read from HighscoreList to create PlayerList
            for (String s : highscoreList) {
                String[] line = s.split("[|]");
                playerList.add(new Player(Integer.parseInt(line[0]), line[1], line[2]));
            }

        }
    }

    /**
     * Setter for HighscoreList
     *
     * @param highscoreList Content of the Highscore File
     */
    void setHighscoreList(List<String> highscoreList) {

        this.highscoreList = highscoreList;
        initializePlayerList();
        System.out.println("File read");
    }

    /**
     * Getter for PlayerList
     *
     * @return List<Player>
     */
    List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * Method to sort the new Player into the right Place
     *
     * @param name       PlayerName
     * @param time       Player Time
     * @param difficulty Difficulty
     */
    void sortIntoHighscore(String name, String time, int difficulty) {

        int rank = 0;
        Player newPlayer = new Player(0, name, time);

        for (int i = 0; i < playerList.size(); i++) {
            if (newPlayer.getMinutes() < playerList.get(i).getMinutes()) {
                rank = i;
                break;
            } else if (newPlayer.getMinutes() == playerList.get(i).getMinutes()
                    && newPlayer.getSeconds() < playerList.get(i).getSeconds()) {
                rank = i;
                break;
            }
        }

        newPlayer.setRank(rank + 1);
        Player tmp = newPlayer;
        for (int i = rank; i < playerList.size(); i++) {

            Player oldPlayer = playerList.set(i, tmp);
            oldPlayer.setRank(i + 2);
            tmp = oldPlayer;
        }
    }

    boolean isNewItemInHighscore(String time, int difficulty) {

        String[] line = time.split("[:]");
        int minutes = Integer.parseInt(line[0]);
        int seconds = Integer.parseInt(line[1]);

        if (minutes < playerList.get(9).getMinutes()) {
            return true;
        } else {
            return minutes == playerList.get(9).getMinutes() && seconds < playerList.get(9).getSeconds();
        }
    }

}
