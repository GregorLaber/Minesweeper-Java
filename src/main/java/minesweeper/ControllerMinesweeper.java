package minesweeper;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;
import minesweeper.highscore.ControllerHighscore;

import java.util.ArrayList;
import java.util.List;

/**
 * Enthält die Steuerung der Anwendung. Klassisch nach MVC Architektur
 */
public class ControllerMinesweeper implements ViewListenerMinesweeper {

    private static ModelMinesweeper model;
    private static ViewGuiMinesweeper view;
    private MinesweeperSymbols symbols;
    private ControllerHighscore controllerHighscore;
    private boolean firstClickDone;
    private static int displayBombNumber;
    private final List<Integer> emptyTileRowList;
    private final List<Integer> emptyTileColList;
    private static int recursionIndex;
    private final boolean debug; // For Debug purpose

    @SuppressWarnings("ConstantConditions")
    ControllerMinesweeper() {

        model = new ModelMinesweeper();
        view = new ViewGuiMinesweeper(model.getNumberOfBombs());
        try {
            this.controllerHighscore = new ControllerHighscore();
            this.symbols = new MinesweeperSymbols(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        firstClickDone = true;
        displayBombNumber = model.getNumberOfBombs();
        emptyTileRowList = new ArrayList<>();
        emptyTileColList = new ArrayList<>();
        recursionIndex = -1;
        debug = false; // For Debug purpose

        view.addViewListener(this);
        if (debug) {
            showAllBombs(false, 0, 0); // For Debug purpose
        }
    }

    /**
     * Startet rendering der Anwendung
     */
    void startGame() {

        view.startView();
    }

    /**
     * Hauptmethode des Spielflusses. Bei jedem Klick wird diese Methode gerufen.
     *
     * @param primaryClick Links(true) oder Rechtsklick(false)
     * @param row          Pos(row/col)
     * @param col          Pos(row/col)
     */
    private void gameLoop(boolean primaryClick, int row, int col) {

        if (firstClickDone) {
            firstClickDone = false;
            view.enablePauseButton();
            view.startToolbarTimer();
            model.startCooldownTimer();
            model.setIsGameRunning(true);
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

                    performOpening(row, col, false);

                } else { // Bomb is hit
                    model.stopCooldownTimer();
                    model.setIsGameRunning(false);
                    view.stopToolbarTimer();
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
     * Methode zum Standard öffnen von Feldern
     *
     * @param row             Pos(row/col)
     * @param col             Pos(row/col)
     * @param backgroundColor to mark the hint opened tile
     */
    private void performOpening(int row, int col, boolean backgroundColor) {
        // This tiles gets a coloured number
        int surroundingBombs = model.calculateSurroundingBombs(row, col);
        model.setAlreadyOpenedListAt(row, col);
        view.setButton(null, row, col);
        view.setButton(surroundingBombs, row, col, backgroundColor);
        if (surroundingBombs != 0) {
            view.disableButton(row, col);
        } else {
            // hier kommt Nullen aufdecken hin
            view.disableEmptyButton(row, col);
            findSurroundingEmptyTiles(row, col);
            if (emptyTileRowList.isEmpty()) {
                findColoredNeighbors(row, col);
            } else {
                openUpEmptyTiles();
            }
        }
        if (model.checkWin()) {
            model.stopCooldownTimer();
            model.setIsGameRunning(false);
            String time = view.getLabelTimer();
            view.stopToolbarTimer();
            view.disableAllButtons();
            openAllTiles(false, row, col);
            if (controllerHighscore.isNewItemInHighscore(time, model.getDifficulty())) {
                String name = controllerHighscore.highscoreNotification();
                controllerHighscore.createNewHighscoreItem(name, time, model.getDifficulty());
            } else {
                view.winningNotification();
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

        /*
         Beachten calculateSurroundingBombs liefert eine -1 zurück wenn auf
         (Index < 0) || (Index > ROW oder COL) zugegriffen wird!
        */
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
        //Corners
        if (compareIndexRecursion(row - 1, col - 1)) {
            surroundingBombs = model.calculateSurroundingBombs(row - 1, col - 1);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row - 1);
                emptyTileColList.add(col - 1);
                foundNewEmptyTile = true;
            }
        }
        if (compareIndexRecursion(row - 1, col + 1)) {
            surroundingBombs = model.calculateSurroundingBombs(row - 1, col + 1);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row - 1);
                emptyTileColList.add(col + 1);
                foundNewEmptyTile = true;
            }
        }
        if (compareIndexRecursion(row + 1, col - 1)) {
            surroundingBombs = model.calculateSurroundingBombs(row + 1, col - 1);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row + 1);
                emptyTileColList.add(col - 1);
                foundNewEmptyTile = true;
            }
        }
        if (compareIndexRecursion(row + 1, col + 1)) {
            surroundingBombs = model.calculateSurroundingBombs(row + 1, col + 1);
            if (surroundingBombs == 0) {
                emptyTileRowList.add(row + 1);
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

    /**
     * Methode um alle gesammelten leeren Felder die zu öffnen sind zu öffnen. Die Liste der zu öffnenden Felder wird
     * in der Methode findSurroundingEmptyTiles() gefüllt.
     */
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
            findColoredNeighbors(row, col);
        }

        emptyTileRowList.clear();
        emptyTileColList.clear();
    }

    /**
     * Methode um die umliegenden Zahlenfelder anhand eines Startpunktes Pos (row/col) zu finden.
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     */
    private void findColoredNeighbors(int row, int col) {

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

    /**
     * Methode um Zahlenfeld an Pos (row/col) zu öffnen.
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     */
    private void openColoredTile(int row, int col) {

        int surroundingBombs = model.calculateSurroundingBombs(row, col);
        model.setAlreadyOpenedListAt(row, col);
        view.setButton(null, row, col);
        if (surroundingBombs != 0) {
            view.setButton(surroundingBombs, row, col, false);
            view.disableButton(row, col);
        } else {
            view.disableEmptyButton(row, col);
        }
    }

    /**
     * Wenn das Spiel beendet ist egal ob durch Gewinn oder Verlust werden anhand dieser Methode alle Felder geöffnet.
     * Bei Verlust wird die getroffene Mine als rote Mine angezeigt.
     *
     * @param redBomb Rote Mine an Pos (row/col)
     * @param row     Pos (row/col)
     * @param col     Pos (row/col)
     */
    private void openAllTiles(boolean redBomb, int row, int col) {

        showNumberTiles();
        showAllBombs(redBomb, row, col);
    }

    /**
     * Methode um alle Zahlenfelder aufzudecken.
     */
    private void showNumberTiles() {

        int row = model.getROW();
        int col = model.getCOL();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (model.isFieldListEmptyAt(i, j)) {

                    int surroundingBombs = model.calculateSurroundingBombs(i, j);
                    view.setButton(null, i, j);
                    view.setButton(surroundingBombs, i, j, false);
                }
            }
        }
    }

    /**
     * Methode um alle Bomben aufzudecken. Wenn redBomb true ist wird die Mine an Pos (row/col)
     * mit der roten Mine angezeigt.
     *
     * @param redBomb Bei Verlust true, bei Gewinn false
     * @param row     Pos (row/col)
     * @param col     Pos (row/col)
     */
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

    /**
     * Methode um anhand des Symbols aus dem Datenbestand in der View das richtige Symbol anzuzeigen.
     *
     * @param symbol (Flag, Mine oder Redmine)
     * @return Image für View
     */
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

    /**
     * Methode overridden vom Interface. Ruft den Spielablauf und sagt ihm, dass primäre Klick war.
     *
     * @param row Index of the field (row/col)
     * @param col Index of the field (row/col)
     */
    @Override
    public void buttonClickedPrimary(int row, int col) {

        gameLoop(true, row, col);
    }

    /**
     * Methode overridden vom Interface. Ruft den Spielablauf und sagt ihm, dass sekundäre Klick war.
     *
     * @param row Index of the field (row/col)
     * @param col Index of the field (row/col)
     */
    @Override
    public void buttonClickedSecondary(int row, int col) {

        gameLoop(false, row, col);
    }

    /**
     * Methode overridden vom Interface. Wenn durch den NEW GAME Button ein neues Spiel angefordert wird und das Spiel
     * noch läuft wird der User um Bestätigung gebeten.
     * Gibt er diese setzt die Methode die View und das Model wieder auf Ausgangszustand für ein neues Spiel.
     */
    @Override
    public void newClicked() {

        if (model.isGameRunning()) {

            view.stopToolbarTimer();
            boolean confirmation = view.confirmationDialogNewClicked();
            if (confirmation) {
                model.setIsGameRunning(false);
                newClicked();
            } else {
                view.startToolbarTimer();
            }

        } else {

            firstClickDone = true;
            model.stopCooldownTimer();
            model.startSetup();
            view.startSetup(model.getNumberOfBombs());
            displayBombNumber = model.getNumberOfBombs();
            view.enableAllButtons();
            view.stopToolbarTimerReset();
            view.setScenes();
            view.disablePauseButton();
            if (debug) {
                showAllBombs(false, 0, 0); // For Debug purpose
            }
        }
    }

    /**
     * Methode overridden vom Interface. Schließt die Anwendung.
     * Wenn das Spiel läuft wird der User um Bestätigung gebeten
     *
     * @param event WindowEvent onClosing (can be null, is secured)
     */
    @Override
    public void exitClicked(WindowEvent event) {

        if (model.isGameRunning()) {

            view.stopToolbarTimer();
            boolean confirmation = view.confirmationDialogExitClicked();
            if (confirmation) {
                model.setIsGameRunning(false);
                exitClicked(event);
            } else {
                if (event != null) {
                    event.consume();
                }
                view.startToolbarTimer();
            }

        } else {
            Platform.exit();
            System.exit(0);
        }
    }

    /**
     * Methode um Spiel mit einer anderen Schwierigkeit neu zu beginnen.
     *
     * @param difficulty 0 = beginner
     *                   1 = advanced
     *                   2 = professional
     */
    @Override
    public void changeDifficultyClicked(int difficulty) {

        model.stopCooldownTimer();
        model.setDifficulty(difficulty);
        view.setDifficulty(difficulty);
        newClicked();
    }

    /**
     * Methode um ein zufälliges Feld zu öffnen.
     */
    @Override
    public void hintClicked() {

        /*
        - Model check Cooldown
        if no Cooldown
            - Model get field for opening
            - Controller performOpening
            - Model start Cooldown
        else show Popup with Cooldown Seconds
         */
        if (!model.isHintCooldownActive()) {

            model.startCooldownTimer();
            int[] coordinates = model.getCoordinatesForHint();
            performOpening(coordinates[0], coordinates[1], true);
        } else {
            // Popup with Cooldown in Seconds
            view.hintTimeoutNotification(firstClickDone, model.getTimeoutSeconds());
        }
    }

    /**
     * Click Action Button "Show Highscore" clicked. Show Highscore List.
     */
    @Override
    public void showHighscoreClicked(int difficulty) {


        if (view.isPauseButtonDisabled()) {
            controllerHighscore.showHighscore(difficulty);
        } else {
            view.stopToolbarTimer();
            controllerHighscore.showHighscore(difficulty);
            view.startToolbarTimer();
        }

    }

    /**
     * Click Action Button "Delete Highscore" clicked. Delete specific table or all.
     */
    @Override
    public void deleteHighscoreClicked() {

        if (view.isPauseButtonDisabled()) {
            controllerHighscore.deleteHighscoreDialog();
        } else {
            view.stopToolbarTimer();
            controllerHighscore.deleteHighscoreDialog();
            view.startToolbarTimer();
        }
    }

    /**
     * Click Action Button "Change Mode" clicked.
     * If in Method newClicked the User dont confirm to start a new Game. The Mode is changed to previous one.
     *
     * @param mode 0 = Normal <br>
     *             1 = Girl
     */
    @Override
    public void changeModeClicked(int mode) {

        boolean tmp = (mode != 0);

        try {
            this.symbols = new MinesweeperSymbols(mode);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        view.setStyle(mode);
        view.setImages(mode);
        newClicked();

        if (!firstClickDone) {
            mode = (!tmp) ? 1 : 0;
            try {
                this.symbols = new MinesweeperSymbols(mode);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            view.setStyle(mode);
            view.setImages(mode);
            view.setMode(mode);
            view.setSelectedMode();
        }

    }

    /**
     * Methode um Spiel zu pausieren
     */
    @Override
    public void pauseClicked() {

        view.stopToolbarTimer();
        view.pauseNotification();
    }
}
