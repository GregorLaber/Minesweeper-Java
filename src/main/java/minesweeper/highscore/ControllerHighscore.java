package main.java.minesweeper.highscore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for managing the Highscore File.
 */
public class ControllerHighscore {

    /*TODO
        - im Moment wird File vor dem ersten Aufruf gelöscht
        - Problemlösung könnte sein reader und writer jedesmal neu das File zuzuordnen
     */

    private static final String path = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscore.txt";
    private static final ModelHighscore model = new ModelHighscore();
    private File file;
    private Scanner reader;
    private FileWriter writer;

    public ControllerHighscore() throws Exception {

        this.file = new File(this.path);
        this.reader = new Scanner(this.file);
        this.writer = new FileWriter(this.file);
        this.readFile();
    }

    /**
     * Only Test Method. To Test the functionality
     *
     * @throws Exception FileNotFoundException
     */
    public void readWrite() throws Exception {

        System.out.println("Path to file: ");
        System.out.println(this.path);

        String input = "Old Content";
        writer.write(input);
        writer.close();

        System.out.println("First in File: ");
        while (reader.hasNextLine()) {

            System.out.println(reader.nextLine());
        }

        String output = "New Content";
        writer = new FileWriter(this.file);
        writer.write(output);
        writer.close();

        reader = new Scanner(this.file);
        System.out.println("Now in File: ");
        while (reader.hasNextLine()) {

            System.out.println(reader.nextLine());
        }

    }

    private void readFile() {

        List<String> fileContent = new ArrayList<>();

        while (reader.hasNextLine()) {

            fileContent.add(reader.nextLine());
        }
        model.setHighscoreList(fileContent);

    }

}
