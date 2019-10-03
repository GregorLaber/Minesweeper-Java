package main.java.minesweeper.highscore;

import java.io.*;
import java.util.Scanner;

/**
 * Class for managing the Highscore File.
 */
public class FileController {

    private String path = System.getProperty("user.dir") + "\\src\\main\\resources\\highscore\\highscore.txt";
    private File file;
    private Scanner reader;
    private FileWriter writer;

    public FileController() throws Exception {

        this.file = new File(this.path);
        this.reader = new Scanner(this.file);
        this.writer = new FileWriter(this.file);
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
}
