package minesweeper;

import javafx.scene.image.Image;

import java.util.Objects;

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
     */
    MinesweeperSymbols(int style) {

        if (style == 0) {
            this.MINE = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/mine.png")));
            this.REDMINE = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/redmine.png")));
            this.FLAG = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/flag.png")));
        } else {
            this.MINE = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/unicorn.png")));
            this.REDMINE = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/inflatable.png")));
            this.FLAG = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/lgtb.png")));
        }

        this.WINDOW_ICON = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/redmine.png")));
        this.QUESTION_MARK = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/question-mark.png")));
    }
}
