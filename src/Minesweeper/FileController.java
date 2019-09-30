package Minesweeper;

import java.io.*;

/**
 * Class for managing the Highscore File.
 */
class FileController {

    private BufferedReader reader;
    private BufferedWriter writer;

    FileController() throws IOException {

        this.reader = new BufferedReader(new FileReader(new File("C:\\Users\\laber\\OneDrive - DPD Deutschland GmbH\\Gregor\\Dev\\Sprachen\\java\\02 Games\\07 Minesweeper\\src\\Minesweeper\\highscore\\highscore.txt")));
        this.writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\laber\\OneDrive - DPD Deutschland GmbH\\Gregor\\Dev\\Sprachen\\java\\02 Games\\07 Minesweeper\\src\\Minesweeper\\highscore\\highscore.txt")));
    }
}
