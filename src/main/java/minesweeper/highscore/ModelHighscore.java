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

        if (highscoreList.size() <= 10) {

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
        boolean sortedIn = false;
        Player newPlayer = new Player(0, name, time);

        for (int i = 0; i < playerList.size(); i++) {
            if (newPlayer.getMinutes() < playerList.get(i).getMinutes()) {
                rank = i;
                sortedIn = true;
                break;
            } else if (newPlayer.getMinutes() == playerList.get(i).getMinutes()
                    && newPlayer.getSeconds() < playerList.get(i).getSeconds()) {
                rank = i;
                sortedIn = true;
                break;
            }
        }

        if (!sortedIn && playerList.size() < 10) {

            newPlayer.setRank(playerList.size() + 1);
            playerList.add(newPlayer);

        } else {

            newPlayer.setRank(rank + 1);
            Player tmp = newPlayer;
            for (int i = rank; i < playerList.size(); i++) {

                Player oldPlayer = playerList.set(i, tmp);
                oldPlayer.setRank(i + 2);
                tmp = oldPlayer;
                if ((i == playerList.size() - 1) && playerList.size() < 10) {
                    tmp.setRank(i + 2);
                    playerList.add(tmp);
                    break;
                }
            }
        }

    }

    /**
     * Method to check if the newest won Game is in Highscore
     *
     * @param time       from the newest won Game
     * @param difficulty from the Game
     * @return bool
     */
    boolean isNewItemInHighscore(String time, int difficulty) {

        if (playerList.size() < 10) {
            return true;
        }

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
