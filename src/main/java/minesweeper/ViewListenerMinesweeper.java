package main.java.minesweeper;

interface ViewListenerMinesweeper {

    /**
     * Click Action. To uncover a field
     *
     * @param row Index of the field (row/col)
     * @param col Index of the field (row/col)
     */
    void buttonClickedPrimary(int row, int col);

    /**
     * Click Action. To set a Flag on this field
     *
     * @param row Index of the field (row/col)
     * @param col Index of the field (row/col)
     */
    void buttonClickedSecondary(int row, int col);

    /**
     * Click Action Button "New Game". Start new Game.
     */
    void newClicked();

    /**
     * Click Action Button "Exit Game". Exit the Game.
     */
    void exitClicked();

    /**
     * @param difficulty 0 = beginner
     *                   1 = advanced
     *                   2 = professional
     */
    void changeDifficultyClicked(int difficulty);

    /**
     * Click Action Button "Get Hint" clicked. Get Hint.
     */
    void hintClicked();

    /**
     * Click Action Button "Show Highscore" clicked. Show Highscore List.
     */
    void showHighscoreClicked(int difficulty);

    /**
     * Click Action Button "Delete Highscore" clicked. Delete specific table or all.
     */
    void deleteHighscoreClicked();

    /**
     * Click Action Button "Change Mode" clicked.
     *
     * @param mode 0 = Normal
     *             1 = Girl
     */
    void changeModeClicked(int mode);

    /**
     * Click Action Button "Pause" clicked.
     */
    void pauseClicked();
}
