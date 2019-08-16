package Minesweeper;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;



/*TODO
   - Change recursive Method (so every empty Tile opens up)
   - pause button
   - write a User manual
   - Highscore
   - Implement hint? (with cool down)
   - Menü an Scene eine Ebene höher (vllt. hat erst nicht funktioniert)
   - Benutzerdefinierter Mode (max 30 x 24) und 667 Mines
   - GirlMode
   - Tracker (how many Games played in a row)
 */


public class ControllerMinesweeper implements ViewListenerMinesweeper {

    private static final ModelMinesweeper model = new ModelMinesweeper();
    private static final ViewGuiMinesweeper view = new ViewGuiMinesweeper(model.getNumberOfBombs());
    private final MinesweeperSymbols symbols = new MinesweeperSymbols();
    private boolean firstClick = true;
    private static int displayBombNumber = model.getNumberOfBombs();
    private final List<Integer> emptyTileRowList = new ArrayList<>();
    private final List<Integer> emptyTileColList = new ArrayList<>();
    private static int recursionIndex = -1;
    private final boolean debug = false; // For Debug purpose

    ControllerMinesweeper() {

        view.addViewListener(this);
        if (debug) {
            showAllBombs(false, 0, 0); // For Debug purpose
        }
    }

    void startGame() {

        view.startView();
    }

    private void gameLoop(boolean primaryClick, int row, int col) {

        if (firstClick) {
            firstClick = false;
            view.startTimer();
        }

        // Primary Click to open up the tiles
        if (primaryClick) {

            if (model.isFlagAt(row, col)) {

                displayBombNumber++;
                if (displayBombNumber <= model.getNumberOfBombs()) {
                    view.setBombNumberTextField(displayBombNumber);
                }
                model.resetFlagAt(row, col);
                view.setButton(null, row, col);

            } else {

                if (model.isFieldListEmptyAt(row, col)) {

                    // This tiles gets a coloured number
                    int surroundingBombs = model.calculateSurroundingBombs(row, col);
                    model.setAlreadyOpenedListAt(row, col);
                    view.setButton(null, row, col);
                    view.setButton(surroundingBombs, row, col);
                    if (surroundingBombs != 0) {
                        view.disableButton(row, col);
                    } else {
                        // hier kommt Nullen aufdecken hin
                        view.disableEmptyButton(row, col);
                        findSurroundingEmptyTiles(row, col);
                        if (emptyTileRowList.isEmpty()) {
                            openColoredNeighbors(row, col);
                        } else {
                            openUpEmptyTiles();
                        }
                    }
                    if (model.checkWin()) {
                        view.stopTimer();
                        view.disableAllButtons();
                        openAllTiles(false, row, col);
                        view.winningNotification();
                    }

                } else { // Bomb is hit
                    view.stopTimer();
                    view.disableAllButtons();
                    openAllTiles(true, row, col);
                    view.bombFieldNotification();
                }
            }

        } else { // Secondary Click to set a Flag

            if (model.isFlagAt(row, col)) {

                displayBombNumber++;
                if (displayBombNumber <= model.getNumberOfBombs()) {
                    view.setBombNumberTextField(displayBombNumber);
                }
                model.resetFlagAt(row, col);
                view.setButton(null, row, col);

            } else {
                displayBombNumber--;
                if (displayBombNumber >= 0) {
                    view.setBombNumberTextField(displayBombNumber);
                }
                model.setFlagAt(row, col);
                view.setButton(getRightImage(ModelMinesweeper.FLAG), row, col);
            }
        }
    }

    /**
     * Recursive Method to find the surrounding null tiles.
     *
     * @param row position of the tile (row = x) (x,y)
     * @param col position of the tile (col = y) (x,y)
     */
    private void findSurroundingEmptyTiles(int row, int col) {

        int surroundingBombs;
        boolean foundNewEmptyTile = false;

        // Beachten calculateSurroundingBombs liefert eine -1 zurück wenn auf (Index < 0) || (Index > ROW oder COL) zugegriffen wird!
        if (compareIndexRecursion(row - 1, col)) {
            surroundingBombs = model.calculateSurroundingBombs(row - 1, col);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row - 1);
                emptyTileColList.add(col);
                foundNewEmptyTile = true;
            }
        }
        if (compareIndexRecursion(row + 1, col)) {
            surroundingBombs = model.calculateSurroundingBombs(row + 1, col);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row + 1);
                emptyTileColList.add(col);
                foundNewEmptyTile = true;
            }
        }
        if (compareIndexRecursion(row, col - 1)) {
            surroundingBombs = model.calculateSurroundingBombs(row, col - 1);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row);
                emptyTileColList.add(col - 1);
                foundNewEmptyTile = true;
            }
        }
        if (compareIndexRecursion(row, col + 1)) {
            surroundingBombs = model.calculateSurroundingBombs(row, col + 1);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row);
                emptyTileColList.add(col + 1);
                foundNewEmptyTile = true;
            }
        }

        if (foundNewEmptyTile) {
            recursionIndex++;
            if (recursionIndex < emptyTileRowList.size()) {
                findSurroundingEmptyTiles(emptyTileRowList.get(recursionIndex), emptyTileColList.get(recursionIndex));
            }
        } else {
            recursionIndex++;
            if (recursionIndex < emptyTileRowList.size()) {
                findSurroundingEmptyTiles(emptyTileRowList.get(recursionIndex), emptyTileColList.get(recursionIndex));
            }
        }
        recursionIndex = -1;
    }

    /**
     * compare if new indexes already exist
     *
     * @param row coordinates (row/col)
     * @param col coordinates (row/col)
     * @return true if the input tile is not in the emptyList yet
     */
    private boolean compareIndexRecursion(int row, int col) {

        for (int i = 0; i < emptyTileRowList.size(); i++) {
            if (row == emptyTileRowList.get(i) && col == emptyTileColList.get(i)) {
                return false;
            }
        }

        return true;
    }

    private void openUpEmptyTiles() {

        for (int i = 0; i < emptyTileRowList.size(); i++) {
            int row = emptyTileRowList.get(i);
            int col = emptyTileColList.get(i);
            if (!model.isAlreadyOpenedAt(row, col)) {

                model.setAlreadyOpenedListAt(row, col);
                view.disableEmptyButton(row, col);
            }
        }

        for (int i = 0; i < emptyTileRowList.size(); i++) {
            int row = emptyTileRowList.get(i);
            int col = emptyTileColList.get(i);
            openColoredNeighbors(row, col);
        }

        emptyTileRowList.clear();
        emptyTileColList.clear();
    }

    private void openColoredNeighbors(int row, int col) {

        // Maximum
        int ROW = model.getROW();
        int COL = model.getCOL();

        if (row == 0 && col == 0) {

            if (!model.isAlreadyOpenedAt(row, col + 1)) {
                openColoredTile(row, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col)) {
                openColoredTile(row + 1, col);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col + 1)) {
                openColoredTile(row + 1, col + 1);
            }

        } else if (row == 0 && col == COL - 1) {

            if (!model.isAlreadyOpenedAt(row, col - 1)) {
                openColoredTile(row, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col)) {
                openColoredTile(row + 1, col);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col - 1)) {
                openColoredTile(row + 1, col - 1);
            }

        } else if (row == ROW - 1 && col == COL - 1) {

            if (!model.isAlreadyOpenedAt(row - 1, col - 1)) {
                openColoredTile(row - 1, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col)) {
                openColoredTile(row - 1, col);
            }
            if (!model.isAlreadyOpenedAt(row, col - 1)) {
                openColoredTile(row, col - 1);
            }

        } else if (row == ROW - 1 && col == 0) {

            if (!model.isAlreadyOpenedAt(row, col + 1)) {
                openColoredTile(row, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col)) {
                openColoredTile(row - 1, col);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col + 1)) {
                openColoredTile(row - 1, col + 1);
            }

        } else if (row == 0) {

            if (!model.isAlreadyOpenedAt(row + 1, col - 1)) {
                openColoredTile(row + 1, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col + 1)) {
                openColoredTile(row + 1, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col)) {
                openColoredTile(row + 1, col);
            }
            if (!model.isAlreadyOpenedAt(row, col - 1)) {
                openColoredTile(row, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row, col + 1)) {
                openColoredTile(row, col + 1);
            }

        } else if (col == 0) {

            if (!model.isAlreadyOpenedAt(row + 1, col)) {
                openColoredTile(row + 1, col);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col)) {
                openColoredTile(row - 1, col);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col + 1)) {
                openColoredTile(row - 1, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col + 1)) {
                openColoredTile(row + 1, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row, col + 1)) {
                openColoredTile(row, col + 1);
            }

        } else if (row == ROW - 1) {

            if (!model.isAlreadyOpenedAt(row - 1, col - 1)) {
                openColoredTile(row - 1, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row, col - 1)) {
                openColoredTile(row, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col)) {
                openColoredTile(row - 1, col);
            }
            if (!model.isAlreadyOpenedAt(row, col + 1)) {
                openColoredTile(row, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col + 1)) {
                openColoredTile(row - 1, col + 1);
            }

        } else if (col == COL - 1) {

            if (!model.isAlreadyOpenedAt(row - 1, col - 1)) {
                openColoredTile(row - 1, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col)) {
                openColoredTile(row - 1, col);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col)) {
                openColoredTile(row + 1, col);
            }
            if (!model.isAlreadyOpenedAt(row, col - 1)) {
                openColoredTile(row, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col - 1)) {
                openColoredTile(row + 1, col - 1);
            }

        } else {

            if (!model.isAlreadyOpenedAt(row - 1, col - 1)) {
                openColoredTile(row - 1, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col)) {
                openColoredTile(row - 1, col);
            }
            if (!model.isAlreadyOpenedAt(row - 1, col + 1)) {
                openColoredTile(row - 1, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row, col + 1)) {
                openColoredTile(row, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col + 1)) {
                openColoredTile(row + 1, col + 1);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col)) {
                openColoredTile(row + 1, col);
            }
            if (!model.isAlreadyOpenedAt(row + 1, col - 1)) {
                openColoredTile(row + 1, col - 1);
            }
            if (!model.isAlreadyOpenedAt(row, col - 1)) {
                openColoredTile(row, col - 1);
            }
        }
    }

    private void openColoredTile(int row, int col) {

        int surroundingBombs = model.calculateSurroundingBombs(row, col);
        model.setAlreadyOpenedListAt(row, col);
        view.setButton(null, row, col);
        if (surroundingBombs != 0) {
            view.setButton(surroundingBombs, row, col);
            view.disableButton(row, col);
        } else {
            view.disableEmptyButton(row, col);
        }
    }

    private void openAllTiles(boolean redBomb, int row, int col) {

        showNumberTiles();
        showAllBombs(redBomb, row, col);
    }

    private void showNumberTiles() {

        int row = model.getROW();
        int col = model.getCOL();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (model.isFieldListEmptyAt(i, j)) {

                    int surroundingBombs = model.calculateSurroundingBombs(i, j);
                    view.setButton(null, i, j);
                    view.setButton(surroundingBombs, i, j);
                }
            }
        }
    }

    private void showAllBombs(boolean redBomb, int row, int col) {

        for (int i = 0; i < model.getNumberOfBombs(); i++) {

            // If there is no flag, show bomb
            if (!(model.isFlagAt(model.bombListRow.get(i), model.bombListCol.get(i)))) {
                view.setButton(getRightImage(ModelMinesweeper.MINE), model.bombListRow.get(i), model.bombListCol.get(i));
            }
        }

        // the clicked tile
        if (redBomb) {
            view.setButton(getRightImage(ModelMinesweeper.REDMINE), row, col);
        }
    }

    private Image getRightImage(String symbol) {

        switch (symbol) {
            case "MINE":
                return symbols.MINE;
            case "FLAG":
                return symbols.FLAG;
            case "REDMINE":
                return symbols.REDMINE;
            default:
                return null;
        }
    }

    @Override
    public void buttonClickedPrimary(int row, int col) {

        gameLoop(true, row, col);
    }

    @Override
    public void buttonClickedSecondary(int row, int col) {

        gameLoop(false, row, col);
    }

    @Override
    public void newClicked() {

        firstClick = true;
        model.startSetup();
        view.startSetup(model.getNumberOfBombs());
        displayBombNumber = model.getNumberOfBombs();
        view.enableAllButtons();
        view.stopTimerReset();
        view.setScenes();
        if (debug) {
            showAllBombs(false, 0, 0); // For Debug purpose
        }
    }

    @Override
    public void changeDifficultyClicked(int difficulty) {

        model.setDifficulty(difficulty);
        view.setDifficulty(difficulty);
        newClicked();
        view.setScenes();
    }


}
