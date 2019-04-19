package Minesweeper;

interface ViewListenerMinesweeper {

    void buttonClickedPrimary(int row, int col);

    void buttonClickedSecondary(int row, int col);

    void newClicked();

    /**
     * @param difficulty 0 = beginner
     *                   1 = advanced
     *                   2 = professional
     */
    void changeDifficultyClicked(int difficulty);
}
