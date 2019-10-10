package main.java.minesweeper;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Symbols
 * 1. Mine
 * 2. Redmine (detonated)
 * 3. Flag
 * 4. Question Mark (for Hint)
 */
class MinesweeperSymbols {

    final Image MINE;
    final Image REDMINE;
    final Image FLAG;
    final Image QUESTION_MARK;

    MinesweeperSymbols() throws FileNotFoundException {

        String path = System.getProperty("user.dir") + "\\out\\production\\07 Minesweeper\\main\\resources\\images\\";
        this.MINE = new Image(new FileInputStream(path + "mine.png"));
        this.REDMINE = new Image(new FileInputStream(path + "redmine.png"));
        this.FLAG = new Image(new FileInputStream(path + "flag.png"));
        this.QUESTION_MARK = new Image(new FileInputStream(path + "question-mark.png"));
    }
}
