package main.java.minesweeper.highscore;

import java.io.*;
import java.util.Scanner;

/**
 * Class for managing the Highscore File.
 */
public class FileController {

    private String path = System.getProperty("user.dir")
            + "\\src\\main\\resources\\highscore\\highscore.txt";
    private File file;
    private Scanner reader;
    private FileWriter writer;

    public FileController() {
    }

    /**
     * Only Test Method. To Test the functionality
     * @throws Exception throws FileNotFoundException
     */
    public void readWrite() throws Exception {

        System.out.println("Path to file: ");
        System.out.println(this.path);

        this.file = new File(this.path);

        String input = "Old Content";
        FileWriter writer = new FileWriter(this.file);
        writer.write(input);
        writer.close();

        Scanner scanner = new Scanner(this.file);

        System.out.println("First in File: ");
        while (scanner.hasNextLine()) {

            System.out.println(scanner.nextLine());
        }

        String output = "New Content";
        writer = new FileWriter(this.file);
        writer.write(output);
        writer.close();

        scanner = new Scanner(this.file);
        System.out.println("Now in File: ");
        while (scanner.hasNextLine()) {

            System.out.println(scanner.nextLine());
        }

    }
}
