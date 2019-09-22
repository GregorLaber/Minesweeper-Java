package Minesweeper;


import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enthält den Datenzustand der Anwendung. Klassisch nach MVC Architektur
 */
class ModelMinesweeper {

    private int difficulty;
    private int ROW;
    private int COL;
    private String[][] fieldList;
    private String[][] flagList;
    private String[][] alreadyOpenedList;
    final List<Integer> bombListRow = new ArrayList<>();
    final List<Integer> bombListCol = new ArrayList<>();
    private int numberOfEmptyFields;
    private int numberOfBombs;
    static final String MINE = "MINE";
    static final String REDMINE = "REDMINE";
    static final String FLAG = "FLAG";
    private static final String EMPTY = "EMPTY";
    private static final String OPENED = "OPENED";
    private boolean hintCooldownActive = true;
    private static final long COOL_DOWN_TIME = 10;
    private AnimationTimer timer;
    private long seconds;

    ModelMinesweeper() {

        this.difficulty = 0;
        startSetup();
    }

    /**
     * Methode für jeden Neustart der Anwendung.
     * - Zeile und Spalten werden auf neuen Zustand gesetzt
     * - Feldliste wird neu initialisiert
     * - Flaggenliste wird neu initialisiert
     * - Liste der bereits geöffneten Felder wird neu initialisiert
     * - Anzahl der zu öffnenden Felder ergibt sich aus Zeile mal Spalte
     * - Die beiden Listen für Zeile und Spalten der Bomben werden zurück gesetzt.
     * - Danach wird das Spielfeld initialisiert. Anzahl der Minen wird berechnet und zufällig verteilt.
     */
    void startSetup() {

        this.ROW = getDifficultyRow();
        this.COL = getDifficultyCol();
        this.fieldList = new String[ROW][COL];
        this.flagList = new String[ROW][COL];
        this.alreadyOpenedList = new String[ROW][COL];
        numberOfEmptyFields = ROW * COL;
        bombListRow.clear();
        bombListCol.clear();
        initFields();
        setNumberOfBombs();
        setRandomBombs();
        setHintCooldownActive(true);
        this.initTimer();
    }

    /**
     * Getter für Anzahl der Minen
     *
     * @return Anzahl der Minen
     */
    int getNumberOfBombs() {
        return numberOfBombs;
    }

    /**
     * Methode um zu prüfen ob das Spielfeld an Stelle (row/col) ohne Mine ist.
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     * @return if empty true else false
     */
    boolean isFieldListEmptyAt(int row, int col) {

        return (fieldList[row][col].equals(EMPTY));
    }

    /**
     * Methode um gesetzten Zustand an Pos (row/col) zu bekommen.
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     * @return Zustand von Pos (row/col)
     */
    private String getFieldListAt(int row, int col) {

        return fieldList[row][col];
    }

    /**
     * Methode um an Pos (row/col) eine Mine zu setzen
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     */
    private void setBombAt(int row, int col) {

        fieldList[row][col] = MINE;
    }

    /**
     * Methode um herauszufinden ob an Pos (row/col) eine Flagge sitzt
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     * @return bool if flag at Pos (row/col) then true else false
     */
    boolean isFlagAt(int row, int col) {

        return flagList[row][col].equals(FLAG);
    }

    /**
     * Methode um an Pos (row/col) eine Flagge zu setzen
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     */
    void setFlagAt(int row, int col) {

        flagList[row][col] = FLAG;
    }

    /**
     * Methode um Flagge von Feld an Pos (row/col) zu entfernen
     *
     * @param row Pos (row/col)
     * @param col Pos (row/col)
     */
    void resetFlagAt(int row, int col) {

        flagList[row][col] = EMPTY;
    }

    /**
     * Setzt Feldliste, Flaggenliste und Liste der geöffneten Felder wieder auf Standard empty.
     */
    private void initFields() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                fieldList[i][j] = EMPTY;
            }
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                flagList[i][j] = EMPTY;
            }
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                alreadyOpenedList[i][j] = EMPTY;
            }
        }
    }

    /**
     * Setter für Schwierigkeitsgrad
     *
     * @param difficulty 0 = beginner
     *                   1 = advanced
     *                   2 = professional
     */
    void setDifficulty(int difficulty) {

        this.difficulty = difficulty;
    }

    /**
     * Getter für Anzahl der Zeilen (Spielfeld) abhängig vom Schwierigkeitsgrad
     *
     * @return Anzahl der Zeilen
     */
    private int getDifficultyRow() {

        if (difficulty == 0) {
            return 8;
        }
        return 16; // Case 1/2
    }

    /**
     * Getter für Anzahl der Spalten (Spielfeld) abhängig vom Schwierigkeitsgrad
     *
     * @return Anzahl der Spalten
     */
    private int getDifficultyCol() {

        switch (difficulty) {
            case 1:
                return 16;
            case 2:
                return 30;
            default: // Case 0
                return 8;
        }
    }

    /**
     * Setzt Anzahl der Minen abhängig vom Schwierigkeitsgrad
     */
    private void setNumberOfBombs() {

        switch (difficulty) {
            case 0:
                numberOfBombs = 10;
                break;
            case 1:
                numberOfBombs = 40;
                break;
            case 2:
                numberOfBombs = 99;
                break;
        }
    }

    /**
     * Methode um Anhand der Pos(row/col) die Anzahl der umliegenden Minen zu ermitteln.
     *
     * @param row Pos(row/col)
     * @param col Pos(row/col)
     * @return Anzahl der umliegenden Minen
     */
    int calculateSurroundingBombs(int row, int col) {

        // Exceptions
        if (row < 0 || col < 0) {
            return -1;
        } else if (row >= ROW || col >= COL) {
            return -1;
        }

        int totalSurroundBombs = 0;

        if (row == 0 && col == 0) {

            if (getFieldListAt(row + 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else if (row == 0 && col == COL - 1) {

            if (getFieldListAt(row, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else if (row == ROW - 1 && col == COL - 1) {

            if (getFieldListAt(row - 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else if (row == ROW - 1 && col == 0) {

            if (getFieldListAt(row, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else if (row == 0) {

            if (getFieldListAt(row + 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else if (col == 0) {

            if (getFieldListAt(row + 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else if (row == ROW - 1) {

            if (getFieldListAt(row - 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else if (col == COL - 1) {

            if (getFieldListAt(row - 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }

        } else {

            if (getFieldListAt(row - 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row - 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col + 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row + 1, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
            if (getFieldListAt(row, col - 1).equals(MINE)) {
                totalSurroundBombs++;
            }
        }

        return totalSurroundBombs;
    }

    /**
     * Methode um Minen zufällig auf das Spielfeld zu verteilen
     */
    private void setRandomBombs() {

        Random random = new Random();

        int counter = 0;
        while (counter < numberOfBombs) {

            boolean found = false;
            int randomRow = random.nextInt(ROW);
            int randomCol = random.nextInt(COL);

            if (fieldList[randomRow][randomCol].equals(MINE)) {
                found = true;
            }

            if (!found) {
                bombListRow.add(randomRow);
                bombListCol.add(randomCol);
                setBombAt(randomRow, randomCol);
                counter++;
            }
        }
    }

    /**
     * Setter um an der Pos(row/col) die Liste der bereits geöffneten Felder auf geöffnet zu setzen.
     * Außerdem wird die Anzahl der gesamt geöffneten Felder um eins verringert.
     * Wird zur Überprüfung des Gewinns benötigt.
     *
     * @param row Pos(row/col)
     * @param col Pos(row/col)
     */
    void setAlreadyOpenedListAt(int row, int col) {

        this.alreadyOpenedList[row][col] = OPENED;
        numberOfEmptyFields--;
    }

    /**
     * Getter um herauszufinden ob an Pos(row/col) das Feld bereits geklickt (geöffnet) wurde.
     *
     * @param row Pos(row/col)
     * @param col Pos(row/col)
     * @return bool
     */
    boolean isAlreadyOpenedAt(int row, int col) {

        return (alreadyOpenedList[row][col].equals(OPENED));
    }

    /**
     * Getter für Zeile
     *
     * @return Anzahl der Zeilen
     */
    int getROW() {
        return ROW;
    }

    /**
     * Getter für Spalte
     *
     * @return Anzahl der Spalten
     */
    int getCOL() {
        return COL;
    }

    /**
     * Überprüft ob Spieler gewonnen hat.
     *
     * @return bool
     */
    boolean checkWin() {

        return (numberOfEmptyFields == numberOfBombs);
    }

    /**
     * Methode um Koordinaten für den Hinweis zu bekommen
     *
     * @return an Int Array of 2 Elements
     * [0] = row
     * [1] = col
     */
    int[] getCoordinatesForHint() {

        Random random = new Random();
        boolean found = false;
        int randomRow;
        int randomCol;
        int[] coordinates = new int[2];

        while (!found) {

            randomRow = random.nextInt(ROW);
            randomCol = random.nextInt(COL);

            if (this.isFieldListEmptyAt(randomRow, randomCol) &&
                    !this.isAlreadyOpenedAt(randomRow, randomCol) &&
                    !this.isFlagAt(randomRow, randomCol)) {

                coordinates[0] = randomRow;
                coordinates[1] = randomCol;
                found = true;
            }
        }

        return coordinates;
    }

    /**
     * Getter für HintCooldownActive
     *
     * @return bool
     */
    boolean isHintCooldownActive() {
        return hintCooldownActive;
    }

    /**
     * Setter für HintCooldownActive
     *
     * @param bool bool
     */
    private void setHintCooldownActive(boolean bool) {
        this.hintCooldownActive = bool;
    }

    /**
     * Initialization for the Cooldown Timer
     */
    private void initTimer() {

        this.timer = new AnimationTimer() {

            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime != 0) {
                    if (now > lastTime + 1_000_000_000) {
                        seconds++;
                        System.out.println(seconds);
                        if (seconds == COOL_DOWN_TIME) {
                            setHintCooldownActive(false);
                            seconds = 0;
                            timer.stop();
                        }
                        lastTime = now;
                    }
                } else {
                    lastTime = now;
                }
            }

            @Override
            public void stop() {
                super.stop();
                lastTime = 0;
                seconds = 0;
            }

            @Override
            public void start() {
                seconds = 0;
                super.start();
                setHintCooldownActive(true);
            }
        };
    }

    /**
     * Getter für Seconds
     *
     * @return HintCooldown in Seconds
     */
    long getTimeoutSeconds() {
        return (COOL_DOWN_TIME - this.seconds);
    }

    /**
     * Methode um HintCooldown Timer zu starten
     */
    void startTimer() {
        this.timer.start();
    }

    /**
     * Methode um HintCooldown Timer zu stoppen
     */
    void stopTimer() {
        this.timer.stop();
    }

}
