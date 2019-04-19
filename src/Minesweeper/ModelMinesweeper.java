package Minesweeper;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ModelMinesweeper {

    private int difficulty;
    private final int ROW = getDifficultyRow();
    private final int COL = getDifficultyCol();
    private final String[][] fieldList = new String[ROW][COL];
    private final String[][] flagList = new String[ROW][COL];
    final List<Integer> bombListRow = new ArrayList<>();
    final List<Integer> bombListCol = new ArrayList<>();
    private int numberOfEmptyFields;
    private int numberOfBombs;
    static final String MINE = "MINE";
    static final String REDMINE = "REDMINE";
    static final String FLAG = "FLAG";
    private static final String EMPTY = "EMPTY";

    ModelMinesweeper() {

        this.difficulty = 0;
        startSetup();
    }

    void startSetup() {

        numberOfEmptyFields = ROW * COL;
        bombListRow.clear();
        bombListCol.clear();
        initFields();
        setNumberOfBombs();
        setRandomBombs();
    }

    int getNumberOfBombs() {
        return numberOfBombs;
    }

    boolean isFieldListEmptyAt(int row, int col) {

        return (fieldList[row][col].equals(EMPTY));
    }

    private String getFieldListAt(int row, int col) {

        return fieldList[row][col];
    }

    private void setBombAt(int row, int col) {

        fieldList[row][col] = MINE;
    }

    boolean isFlagAt(int row, int col) {

        return flagList[row][col].equals(FLAG);
    }

    void setFlagAt(int row, int col) {

        flagList[row][col] = FLAG;
    }

    void resetFlagAt(int row, int col) {

        flagList[row][col] = EMPTY;
    }

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
    }

    public void setDifficulty(int difficulty) {

        this.difficulty = difficulty;
    }

    private int getDifficultyRow() {

        switch (difficulty) {
            case 1:
                return 16;
            case 2:
                return 30;
            default: // Case 0
                return 8;
        }
    }

    private int getDifficultyCol() {

        if (difficulty == 0) {
            return 8;
        }
        return 16; // Case 1/2
    }

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

    int calculateSurroundingBombs(int row, int col) {

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

    private void setRandomBombs() {

        Random random = new Random();

        int counter = 0;
        while (counter < numberOfBombs) {

            boolean found = false;
            int randomRow = random.nextInt(ROW - 1);
            int randomCol = random.nextInt(COL - 1);

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

    public void setNumberOfEmptyFieldsMinusOne() {

        numberOfEmptyFields--;
    }

    boolean checkWin() {

        return (numberOfEmptyFields == numberOfBombs);
    }


}
