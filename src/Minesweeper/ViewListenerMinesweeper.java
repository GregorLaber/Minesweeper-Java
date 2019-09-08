package Minesweeper;

interface ViewListenerMinesweeper {

    /**
     *  Click Action. To uncover a field
     * @param row   Index of the field (row/col)
     * @param col   Index of the field (row/col)
     */
    void buttonClickedPrimary(int row, int col);

    /**
     *  Click Action. To set a Flag on this field
     * @param row   Index of the field (row/col)
     * @param col   Index of the field (row/col)
     */
    void buttonClickedSecondary(int row, int col);

    /**
     *  Click Action Button New clicked. Start new Game.
     */
    void newClicked();

    /**
     * @param difficulty 0 = beginner
     *                   1 = advanced
     *                   2 = professional
     */
    void changeDifficultyClicked(int difficulty);
}
