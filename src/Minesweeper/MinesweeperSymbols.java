package Minesweeper;

import javafx.scene.image.Image;

/**
 *  Symbols
 *  1. Mine
 *  2. Redmine (detonated)
 *  3. Flag
 *  4. Question Mark (for Hint)
 */
class MinesweeperSymbols {

    final Image MINE = new Image(getClass().getResourceAsStream("pictures/mine.png"));
    final Image REDMINE = new Image(getClass().getResourceAsStream("pictures/redmine.png"));
    final Image FLAG = new Image(getClass().getResourceAsStream("pictures/flag.png"));
    final Image QUESTION_MARK = new Image(getClass().getResourceAsStream("pictures/question-mark.png"));
}
