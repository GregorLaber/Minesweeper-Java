package Minesweeper;

import javafx.scene.image.Image;



/*TODO
   - Highscore
   - GirlMode

 */


public class ControllerMinesweeper implements ViewListenerMinesweeper {

    private static final ModelMinesweeper model = new ModelMinesweeper();
    private static final ViewGuiMinesweeper view = new ViewGuiMinesweeper(model.getNumberOfBombs());
    private final MinesweeperSymbols symbols = new MinesweeperSymbols();
    private boolean firstClick = true;
    private static int displayBombNumber = model.getNumberOfBombs();

    public ControllerMinesweeper() {

        view.addViewListener(this);
    }

    public void startGame() {

        view.startView();
    }

    private void gameLoop(boolean primaryClick, int row, int col) {

//        showAllBombs(false, 0, 0); // For Debug purpose

        if (firstClick) {
            firstClick = false;
            view.startTimer();
        }

        // Primary Click to open up the tiles
        if (primaryClick) {
            if (model.isFieldListEmptyAt(row, col)) { // This tiles get a coloured number
                int surroundingBombs = model.calculateSurroundingBombs(row, col);
                view.setButton(null, row, col);
                view.setButton(surroundingBombs, row, col);
                if (surroundingBombs != 0) {
                    view.disableButton(row, col);
                } else {
                    view.disableEmptyButton(row, col);
                }
                model.setNumberOfEmptyFieldsMinusOne();
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
    }

    @Override
    public void changeDifficultyClicked(int difficulty) {

        model.setDifficulty(difficulty);
        view.setDifficulty(difficulty);
        newClicked();
        view.setScenes();
    }


}
