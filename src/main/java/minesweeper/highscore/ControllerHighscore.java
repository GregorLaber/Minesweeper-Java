package main.java.minesweeper.highscore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for managing the Highscore and File.
 */
public class ControllerHighscore {

    private static final String path = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscore.txt";
    private static final ModelHighscore model = new ModelHighscore();
    private File file;
    private Scanner reader;
    private FileWriter writer;

    public ControllerHighscore() throws Exception {

        this.file = new File(path);
        this.readFile();
        this.writeToHighscoreFile();
    }

    /**
     * Read the Highscore File at the Starting of the Application
     */
    private void readFile() {

        List<String> fileContent = new ArrayList<>();
        try {
            this.reader = new Scanner(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (reader.hasNextLine()) {

            fileContent.add(reader.nextLine());
        }
        model.setHighscoreList(fileContent);

    }

    /**
     * Writing to the Highscore File
     *
     * @throws IOException Method uses FileWriter
     */
    private void writeToHighscoreFile() throws IOException {

        List<Player> playerList = model.getPlayerList();

        if (playerList.size() != 0) {
            this.writer = new FileWriter(this.file);
            for (Player player : playerList) {
                this.writer.write(player.toString());
                this.writer.write("\n");
            }
            this.writer.close();
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

}
