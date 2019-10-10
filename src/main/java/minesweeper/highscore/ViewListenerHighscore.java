package main.java.minesweeper.highscore;

interface ViewListenerHighscore {

    /**
     * //Click Action Button "Delete Highscore" clicked. Delete specific table or all.
     *
     * @param difficulty which table is to delete.<br>
     *                   Value:<br>
     *                   0, 1, 2 = beginner, advanced, professional.<br>
     *                   3 = all three tables will be deleted.
     */
    void deleteHighscoreClicked(int difficulty);

}
