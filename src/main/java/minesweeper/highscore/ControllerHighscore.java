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
public class ControllerHighscore implements ViewListenerHighscore {

    private static final ModelHighscore model = new ModelHighscore();
    private static final ViewHighscore view = new ViewHighscore();
    private final List<File> files = new ArrayList<>();

    public ControllerHighscore() throws Exception {

        String pathBeginner = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscoreBeginner.txt";
        String pathAdvanced = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscoreAdvanced.txt";
        String pathProfessional = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscoreProfessional.txt";
        //Creates the files only if the files does not exist
        File fileBeginner = new File(pathBeginner);
        File fileAdvanced = new File(pathAdvanced);
        File fileProfessional = new File(pathProfessional);
        //noinspection ResultOfMethodCallIgnored    //dont want to use the result
        fileBeginner.createNewFile();
        //noinspection ResultOfMethodCallIgnored
        fileAdvanced.createNewFile();
        //noinspection ResultOfMethodCallIgnored
        fileProfessional.createNewFile();
        files.add(fileBeginner);
        files.add(fileAdvanced);
        files.add(fileProfessional);
        this.readFile();
        this.writeToHighscoreFile();
        view.addViewListener(this);
    }

    /**
     * Read the Highscore File at the Starting of the Application
     */
    @SuppressWarnings("unchecked")
    private void readFile() {

        ArrayList[] listArray = new ArrayList[3];
        //noinspection ExplicitArrayFilling //sonst ist der Inhalt der Highscore Tabelle falsch
        for (int i = 0; i < listArray.length; i++) {
            listArray[i] = new ArrayList<String>();
        }

        for (int i = 0; i < listArray.length; i++) {

            try {
                Scanner reader = new Scanner(files.get(i));

                while (reader.hasNextLine()) {

                    listArray[i].add(reader.nextLine());
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        model.setHighscoreList(listArray);

    }

    /**
     * Writing to the Highscore File
     *
     * @throws IOException Method uses FileWriter
     */
    @SuppressWarnings("unchecked")
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
        view.showHighscore(playerList[difficulty], difficulty);
    }

    /**
     * Method to call the view. Ask the user about his/her name.
     */
    public String highscoreNotification() {

        return view.highscoreNotification();
    }

    /**
     * Dialog for delete one or all Highscore tables
     */
    public void deleteHighscoreDialog() {

        view.deleteHighscoreDialog();
    }

    /**
     * Click Action Button "Delete Highscore" clicked. Delete specific table or all.
     *
     * @param table which table is to delete.<br>
     *              Value:<br>
     *              0, 1, 2 = beginner, advanced, professional.<br>
     *              3 = all three tables will be deleted.
     */
    @Override
    public void deleteHighscoreClicked(int table) {

        model.deletePlayerList(table);
        try {
            this.clearHighscoreFile(table);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Performs the delete on the File
     *
     * @param file which file is to clear.<br>
     *             Value:<br>
     *             0, 1, 2 = beginner, advanced, professional.<br>
     *             3 = all three files will be cleared.
     */
    private void clearHighscoreFile(int file) throws IOException {

        if (file == 3) {
            for (File delete : files) {
                FileWriter writer = new FileWriter(delete);
                writer.write("");
                writer.close();
            }
        } else {
            FileWriter writer = new FileWriter(files.get(file));
            writer.write("");
            writer.close();
        }
    }

}
