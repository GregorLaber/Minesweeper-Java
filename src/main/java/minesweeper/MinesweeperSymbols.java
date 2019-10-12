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
    final Image WINDOW_ICON;

    /**
     * Constructor for Symbols
     *
     * @param style 0 = Normal, 1 = Girl
     * @throws FileNotFoundException if the images are missing
     */
    MinesweeperSymbols(int style) throws FileNotFoundException {

        String path = System.getProperty("user.dir") + "\\out\\production\\07 Minesweeper\\main\\resources\\images\\";
        if (style == 0) {
            this.MINE = new Image(new FileInputStream(path + "mine.png"));
            this.REDMINE = new Image(new FileInputStream(path + "redmine.png"));
            this.FLAG = new Image(new FileInputStream(path + "flag.png"));
            this.QUESTION_MARK = new Image(new FileInputStream(path + "question-mark.png"));
        } else {
            this.MINE = new Image(new FileInputStream(path + "unicorn.png"));
            this.REDMINE = new Image(new FileInputStream(path + "inflatable.png"));
            this.FLAG = new Image(new FileInputStream(path + "lgtb.png"));
            this.QUESTION_MARK = new Image(new FileInputStream(path + "question-mark.png"));
        }

        this.WINDOW_ICON = new Image(new FileInputStream(path + "redmine.png"));
    }
}
