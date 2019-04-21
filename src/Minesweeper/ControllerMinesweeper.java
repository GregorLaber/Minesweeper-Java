package Minesweeper;




/*TODO
   - ggf. mehrere Scences um Schwierigkeitsgrad zu ändern
   - am Ende auch alle Zahlen zeigen
   - gesetzte Flaggen am Ende stehen lassen
   - ggf. Zeile/Spalte im Profressional tauschen (Platzmangel)
   - geht Timer in Professional?
   - Highscore
   - GirlMode

 */

import javafx.scene.image.Image;

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
                }else {
                    view.disableEmptyButton(row, col);
                }
                model.setNumberOfEmptyFieldsMinusOne();
                if (model.checkWin()) {
                    view.stopTimer();
                    view.disableAllButtons();
                    showAllBombs(false, row, col);
                    view.winningNotification();
                }
            } else { // Bomb is hit
                view.stopTimer();
                view.disableAllButtons();
                showAllBombs(true, row, col);
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

    private void showAllBombs(boolean redBomb, int row, int col) {

        for (int i = 0; i < model.getNumberOfBombs(); i++) {

            view.setButton(getRightImage(ModelMinesweeper.MINE), model.bombListRow.get(i), model.bombListCol.get(i));
        }

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
        displayBombNumber = model.getNumberOfBombs();
        model.startSetup();
        view.startSetup(model.getNumberOfBombs());
        view.enableAllButtons();
        view.stopTimerReset();
    }

    @Override
    public void changeDifficultyClicked(int difficulty) {

        model.setDifficulty(difficulty);
        view.setDifficulty(difficulty);
        view.setScene(difficulty);
        newClicked();
    }


}
