package main.java.minesweeper.highscore;

import java.util.ArrayList;
import java.util.List;

class ModelHighscore {

    private List<String> highscoreList = new ArrayList<>();
    private List<Player> playerList = new ArrayList<>();

    public ModelHighscore() {


    }

    private void initializePlayerList() {

    }

    public void setHighscoreList(List<String> highscoreList) {
        this.highscoreList = highscoreList;

        System.out.println("File read");
    }
}
