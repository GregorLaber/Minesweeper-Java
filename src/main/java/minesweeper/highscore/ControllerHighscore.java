package main.java.minesweeper.highscore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for managing the Highscore and File.
 */
public class ControllerHighscore {

    private static final ModelHighscore model = new ModelHighscore();
    private static final ViewHighscore view = new ViewHighscore();
    private final List<File> files = new ArrayList<>();

    public ControllerHighscore() throws Exception {

        String pathBeginner = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscoreBeginner.txt";
        String pathAdvanced = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscoreAdvanced.txt";
        String pathProfessional = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscoreProfessional.txt";
        files.add(new File(pathBeginner));
        files.add(new File(pathAdvanced));
        files.add(new File(pathProfessional));
        this.readFile();
        this.writeToHighscoreFile();
    }

    /**
     * Read the Highscore File at the Starting of the Application
     */
    private void readFile() {

        ArrayList[] listArray = new ArrayList[3];
        for (int i = 0; i < listArray.length; i++) {
            listArray[i] = new ArrayList<String>();
        }

        for (int i = 0; i < listArray.length; i++) {

            Scanner reader = null;
            try {
                reader = new Scanner(files.get(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (reader.hasNextLine()) {

                listArray[i].add(reader.nextLine());
            }
        }

        model.setHighscoreList(listArray);

    }

    /**
     * Writing to the Highscore File
     *
     * @throws IOException Method uses FileWriter
     */
    private void writeToHighscoreFile() throws IOException {

        ArrayList<Player>[] playerList = new ArrayList[3];
        for (int i = 0; i < model.getPlayerList().length; i++) {
            playerList[i] = model.getPlayerList()[i];
        }

        for (int i = 0; i < playerList.length; i++) {
            if (playerList[i].size() != 0) {
                FileWriter writer = new FileWriter(files.get(i));
                for (Player player : playerList[i]) {
                    writer.write(player.toString());
                    writer.write("\n");
                }
                writer.close();
            }
        }


    }

    /**
     * Method to create a New Item in the Highscore Table.
     *
     * @param name       of the Player
     * @param time       of the Player
     * @param difficulty of the Game
     */
    public void createNewHighscoreItem(String name, String time, int difficulty) {

        model.sortIntoHighscore(name, time, difficulty);
        try {
            this.writeToHighscoreFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check if the newest won Game is in Highscore
     *
     * @param time       from the newest won Game
     * @param difficulty from the Game
     * @return bool
     */
    public boolean isNewItemInHighscore(String time, int difficulty) {
        return model.isNewItemInHighscore(time, difficulty);
    }

    /**
     * Method to call the view to display the Highscore table
     */
    public void showHighscore(int difficulty) {

        ArrayList<Player>[] playerList = model.getPlayerList();
        view.showHighscore(playerList[difficulty]);
    }

    /**
     * Method to call the view. Ask the user about his/her name.
     */
    public String highscoreNotification() {

        return view.highscoreNotification();
    }

}
