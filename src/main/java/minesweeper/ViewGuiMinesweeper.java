package minesweeper;


import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enthält das Aussehen der Anwendung. Klassisch nach MVC Architektur
 */
class ViewGuiMinesweeper {

    private final Stage window = new Stage();
    private Scene sceneBeginner = null, sceneAdvanced = null, sceneProfessional = null;

    private int difficulty;
    private int ROW;
    private int COL;
    private int numberOfBombs;
    private Button[][] buttonList;
    private TextField bombNumberTextField;
    private Button pauseButton;
    private static int tileNumber = 0;
    private static int buttonID = 0;
    private final List<ViewListenerMinesweeper> viewListenerList = new ArrayList<>();
    private MinesweeperSymbols symbols;
    private int mode;
    private String style;
    private static final String BLACK = "-fx-background-color: #000000";
    private static final String INDIAN_RED = "-fx-background-color: #CD5C5C";

    private AnimationTimer timer;
    private final Label labelTimer = new Label("00:00");
    private int minutes;
    private int seconds;

    ViewGuiMinesweeper(int numberOfBombs) {

        try {
            this.symbols = new MinesweeperSymbols(0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setDifficulty(0);
        this.ROW = getDifficultyRow();
        this.COL = getDifficultyCol();
        this.numberOfBombs = numberOfBombs;
        this.buttonList = new Button[ROW][COL];
        this.bombNumberTextField = new TextField(); // Displays the number of Bombs in the Field
        this.setMode(0);
        style = BLACK;

        initButtonList();
    }

    /**
     * Method refreshes game after "NEW GAME" is clicked.
     *
     * @param numberOfBombs to set up
     */
    void startSetup(int numberOfBombs) {

        tileNumber = 0;      //Debug purpose. Number on Tiles if method fieldNumbering is active.
        buttonID = 0;
        clearButtons();
        this.ROW = getDifficultyRow();
        this.COL = getDifficultyCol();
        this.buttonList = new Button[ROW][COL];
        initButtonList();
        this.numberOfBombs = numberOfBombs;
        this.bombNumberTextField = new TextField(); // Displays the number of Bombs in the Field
        setBombNumberTextField(numberOfBombs);
        this.disablePauseButton();
    }

    /**
     * Displays the GUI.
     * Only called once. First opening of the application.
     * Difficulty at beginning is beginner (easiest)
     */
    void startView() {

        // Scenes
        setSceneBeginner();

        // Window
        setWindowIcon();
        window.setTitle("Minesweeper");
        window.setScene(sceneBeginner);
        window.setResizable(true);
        window.show();

        window.setOnCloseRequest(e -> exitClicked());
    }

    /**
     * Set the Scene to beginner. Set Menu, Toolbar and in the mid the Buttons.
     */
    private void setSceneBeginner() {

        BorderPane beginner = new BorderPane();
        beginner.setStyle(style);
        initToolbarTimer();
        beginner.setTop(addMenu());
        beginner.setCenter(addGridPane());
        beginner.setBottom(addToolBar());
        sceneBeginner = new Scene(beginner, 500, 450);
    }

    /**
     * Set the Scene to advanced. Set Menu, Toolbar and in the mid the Buttons.
     */
    private void setSceneAdvanced() {

        BorderPane advanced = new BorderPane();
        advanced.setStyle(style);
        initToolbarTimer();
        advanced.setTop(addMenu());
        advanced.setCenter(addGridPane());
        advanced.setBottom(addToolBar());
        sceneAdvanced = new Scene(advanced, 750, 600);
    }

    /**
     * Set the Scene to professional. Set Menu, Toolbar and in the mid the Buttons.
     */
    private void setSceneProfessional() {

        BorderPane professional = new BorderPane();
        professional.setStyle(style);
        initToolbarTimer();
        professional.setTop(addMenu());
        professional.setCenter(addGridPane());
        professional.setBottom(addToolBar());
        sceneProfessional = new Scene(professional, 975, 700);
    }

    /**
     * toolbar Timer. Initialisation, doing and stop.
     */
    private void initToolbarTimer() {

        this.timer = new AnimationTimer() {

            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime != 0) {
                    if (now > lastTime + 1_000_000_000) {
                        seconds++;
                        if (seconds == 60) {
                            minutes++;
                            seconds = 0;
                        }

                        String displaySeconds;
                        if (seconds < 10) {
                            displaySeconds = "0" + seconds;
                        } else {
                            displaySeconds = Integer.toString(seconds);
                        }

                        String displayMinutes;
                        if (minutes < 10) {
                            displayMinutes = "0" + minutes;
                        } else {
                            displayMinutes = Integer.toString(minutes);
                        }
                        labelTimer.setText(displayMinutes + ":" + displaySeconds);
                        lastTime = now;
                    }
                } else {
                    lastTime = now;
                }
            }
        };
    }

    /**
     * toolbar Timer. Starts the timer at Zero.
     */
    void startToolbarTimer() {

        timer.start();
    }

    /**
     * toolbar Timer. Stop and Reset the Timer
     */
    void stopToolbarTimerReset() {

        timer.stop();
        labelTimer.setText("00:00");
        this.initToolbarTimer();
        this.minutes = 0;
        this.seconds = 0;
    }

    /**
     * toolbar Timer. Only Stop the Timer
     */
    void stopToolbarTimer() {

        timer.stop();
    }

    /**
     * Getter for Time. For the Highscore.
     *
     * @return Time in MM:SS
     */
    String getLabelTimer() {
        return labelTimer.getText();
    }

    /**
     * Initialise the buttons. Set Style, Font, Size, ID.
     * ROW and COL vary depending on the difficulty.
     * If a button is clicked the action triggers here
     */
    private void initButtonList() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {

                Button button = new Button();
//                fieldNumbering(button); // for Debug purpose
                button.setMinSize(30, 30);
                button.setMaxSize(30, 30);
                String stringId = Integer.toString(buttonID++);
                button.setId(stringId);

                buttonList[i][j] = button;
                buttonList[i][j].setFont(Font.font("Arial", FontWeight.BOLD, 12));
                buttonList[i][j].setOnMouseClicked(this::actionPerformed);
                buttonList[i][j].setDisable(false);
                buttonList[i][j].setMouseTransparent(false);
            }
        }
    }

    /**
     * clear all the buttons
     */
    private void clearButtons() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttonList[i][j].setGraphic(null);
                this.buttonList[i][j].setText("");
            }
        }
    }

    /**
     * disable all buttons
     */
    void disableAllButtons() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttonList[i][j].setDisable(true);
            }
        }

        disablePauseButton();
    }

    /**
     * disable specific button. Method for Buttons with numbers on it after clicking.
     *
     * @param row index (row/col)
     * @param col index (row/col)
     */
    void disableButton(int row, int col) {

        this.buttonList[row][col].setMouseTransparent(true);
    }

    /**
     * disable specific Button. Method for empty Buttons without any number and bomb.
     *
     * @param row index (row/col)
     * @param col index (row/col)
     */
    void disableEmptyButton(int row, int col) {

        this.buttonList[row][col].setDisable(true);
    }

    /**
     * enables all buttons.
     */
    void enableAllButtons() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttonList[i][j].setDisable(false);
                this.buttonList[i][j].setMouseTransparent(false);
            }
        }
    }

    /**
     * method to set difficulty
     *
     * @param difficulty three kinds of difficulty
     */
    void setDifficulty(int difficulty) {

        switch (difficulty) {
            case 0: // Beginner
                this.difficulty = 0;
                break;
            case 1: // Advanced
                this.difficulty = 1;
                break;
            case 2: // Professional
                this.difficulty = 2;
                break;
        }
    }

    /**
     * set the right scene depending on difficulty.
     */
    void setScenes() {

        switch (difficulty) {
            case 0: // Beginner
                setSceneBeginner();
                window.setScene(sceneBeginner);
                break;
            case 1: // Advanced
                setSceneAdvanced();
                window.setScene(sceneAdvanced);
                break;
            case 2: // Professional
                setSceneProfessional();
                window.setScene(sceneProfessional);
                break;
        }
    }

    /**
     * liefert die Anzahl der Zeilen abhängig von der Schwierigkeit
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
     * liefert die Anzahl der Spalten abhängig von der Schwierigkeit
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
     * DEBUG Methode. Dient zum nummerieren aller Buttons.
     *
     * @param button der Button der eine Nummer bekommt
     */
    @SuppressWarnings("unused")
    private void fieldNumbering(Button button) {

        String text = Integer.toString(tileNumber++);
        button.setText(text);
        button.setFont(new Font("Arial Unicode MS", 11));
    }

    /**
     * Methode zum erstellen der Menubar
     *
     * @return menuBar
     */
    private MenuBar addMenu() {

        // Menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        MenuItem exitItem = new MenuItem("Exit Game");
        Menu difficultyMenu = new Menu("Difficulty");
        RadioMenuItem beginner = new RadioMenuItem("Beginner");
        RadioMenuItem advanced = new RadioMenuItem("Advanced");
        RadioMenuItem professional = new RadioMenuItem("Professional");
        Menu hintMenu = new Menu(); //Only Dummy for ICON
        Menu highscoreMenu = new Menu("Highscore");
        MenuItem highscoreItemBeginner = new MenuItem("Show Beginner");
        MenuItem highscoreItemAdvanced = new MenuItem("Show Advanced");
        MenuItem highscoreItemProfessional = new MenuItem("Show Professional");
        MenuItem highscoreItemDelete = new MenuItem("Delete Highscore");
        Menu modeMenu = new Menu("Mode");
        RadioMenuItem modeItemNormal = new RadioMenuItem("Normal");
        RadioMenuItem modeItemGirl = new RadioMenuItem("Girl");

        // Properties of Menu
        ToggleGroup groupDifficulty = new ToggleGroup();
        beginner.setToggleGroup(groupDifficulty);
        advanced.setToggleGroup(groupDifficulty);
        professional.setToggleGroup(groupDifficulty);
        switch (difficulty) {
            case 0:
                beginner.setSelected(true);
                break;
            case 1:
                advanced.setSelected(true);
                break;
            case 2:
                professional.setSelected(true);
                break;
        }
        ImageView imageView = new ImageView(symbols.QUESTION_MARK);
        Label hintIcon = new Label();
        hintIcon.setGraphic(imageView);
        hintIcon.setPickOnBounds(true);
        hintMenu.setGraphic(hintIcon);
        ToggleGroup groupMode = new ToggleGroup();
        modeItemNormal.setToggleGroup(groupMode);
        modeItemGirl.setToggleGroup(groupMode);
        switch (this.getMode()) {
            case 0:
                modeItemNormal.setSelected(true);
                break;
            case 1:
                modeItemGirl.setSelected(true);
                break;
        }

        // Add all together
        fileMenu.getItems().addAll(newGameItem, exitItem);
        difficultyMenu.getItems().addAll(beginner, advanced, professional);
        modeMenu.getItems().addAll(modeItemNormal, modeItemGirl);
        highscoreMenu.getItems().addAll(highscoreItemBeginner, highscoreItemAdvanced, highscoreItemProfessional,
                highscoreItemDelete);
        menuBar.getMenus().addAll(fileMenu, difficultyMenu, hintMenu, modeMenu, highscoreMenu);

        // Click Events
        newGameItem.setOnAction((ActionEvent event) -> newClicked());
        exitItem.setOnAction((ActionEvent event) -> exitClicked());
        beginner.setOnAction((ActionEvent event) -> changeDifficultyClicked(0));
        advanced.setOnAction((ActionEvent event) -> changeDifficultyClicked(1));
        professional.setOnAction((ActionEvent event) -> changeDifficultyClicked(2));
        hintIcon.setOnMouseClicked(mouseEvent -> hintClicked());
        modeItemNormal.setOnAction((ActionEvent event) -> changeModeClicked(0));
        modeItemGirl.setOnAction((ActionEvent event) -> changeModeClicked(1));
        highscoreItemBeginner.setOnAction((ActionEvent event) -> showHighscoreClicked(0));
        highscoreItemAdvanced.setOnAction((ActionEvent event) -> showHighscoreClicked(1));
        highscoreItemProfessional.setOnAction((ActionEvent event) -> showHighscoreClicked(2));
        highscoreItemDelete.setOnAction((ActionEvent event) -> deleteHighscoreClicked());

        return menuBar;
    }

    /**
     * Methode um die Statusleiste zu erzeugen.
     * Von links nach rechts: BombenIcon, Textfeld mit der Anzahl der übrigen Bomben, Timer, Pausebutton
     *
     * @return ToolBar
     */
    private HBox addToolBar() {

        String bombs = Integer.toString(numberOfBombs);
        ImageView imageView = new ImageView(symbols.MINE);
        Label bombIcon = new Label("", imageView);
        bombNumberTextField.setText(bombs);
        bombNumberTextField.setPrefColumnCount(2);
        bombNumberTextField.setEditable(false);
        this.pauseButton = new Button("Pause");
        this.pauseButton.setOnAction((ActionEvent event) -> pauseClicked());
        disablePauseButton();

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(bombIcon, bombNumberTextField, labelTimer, pauseButton);

        return new HBox(toolBar);
    }

    /**
     * Method to see if the Pausebutton is disabled
     *
     * @return true if the Button is disabled
     */
    boolean isPauseButtonDisabled() {

        return this.pauseButton.isDisabled();
    }

    /**
     * Method to disable the Pause Button
     */
    void disablePauseButton() {

        this.pauseButton.setDisable(true);
    }

    /**
     * Method to enable the Pause Button
     */
    void enablePauseButton() {

        this.pauseButton.setDisable(false);
    }

    /**
     * Methode um das Textfeld mit der Anzahl der übrigen Bomben auf den Wert vom Parameter zusetzen.
     *
     * @param number Anzahl die in der Statusleiste angezeigt wird
     */
    void setBombNumberTextField(int number) {

        String bombs = Integer.toString(number);
        bombNumberTextField.setText(bombs);
    }

    /**
     * Methode um das Gitternetz mit den Buttons zu setzen.
     *
     * @return Grid
     */
    private GridPane addGridPane() {

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.addToGrid(grid, buttonList[i][j], i, j);
            }
        }

        return grid;
    }

    /**
     * Workaround to turn (Col, Row) to (Row, Col)
     */
    private void addToGrid(GridPane g, Button b, int row, int col) {
        g.add(b, col, row);
    }

    /**
     * Wenn ein Spielfeld-Button geklickt wird, wird der Listener benachrichtigt.
     *
     * @param event Links oder Rechtsklick
     */
    private void actionPerformed(MouseEvent event) {

        if (event.getButton() == MouseButton.SECONDARY) {

            String stringId = ((Button) event.getSource()).getId();
            int[] coordinates = getCoordinates(stringId);

            for (ViewListenerMinesweeper viewListener : viewListenerList) {
                viewListener.buttonClickedSecondary(coordinates[0], coordinates[1]);
            }
        } else {

            String stringId = ((Button) event.getSource()).getId();
            int[] coordinates = getCoordinates(stringId);

            for (ViewListenerMinesweeper viewListener : viewListenerList) {
                viewListener.buttonClickedPrimary(coordinates[0], coordinates[1]);
            }
        }
    }

    /**
     * Interface Methode. Wenn NEW GAME geklickt wird, wird der Listener benachrichtigt.
     */
    private void newClicked() {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.newClicked();
        }
    }

    /**
     * Interface Methode. Wenn EXIT GAME geklickt wird, wird der Listener benachrichtigt.
     */
    private void exitClicked() {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.exitClicked();
        }
    }

    /**
     * Interface Methode. Wenn der Schwierigkeitsgrad verändert wird, wird der Listener benachrichtigt.
     *
     * @param difficulty Neu gesetzter Schwierigkeitsgrad
     */
    private void changeDifficultyClicked(int difficulty) {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.changeDifficultyClicked(difficulty);
        }
    }

    /**
     * Interface Methode. Wenn ein Hinweis angefordert wird, wird der Listener benachrichtigt.
     */
    private void hintClicked() {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.hintClicked();
        }
    }

    /**
     * Interface Methode. Wenn Highscore Liste angefordert wird, wird der Listener benachrichtigt.
     */
    private void showHighscoreClicked(int difficulty) {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.showHighscoreClicked(difficulty);
        }
    }

    /**
     * Interface Methode. Wenn Button geklickt wird, wird der Listener benachrichtigt.
     */
    private void deleteHighscoreClicked() {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.deleteHighscoreClicked();
        }
    }

    /**
     * Interface Method. When triggered, the listener get notified.
     *
     * @param mode 0 = Normal
     *             1 = Girl
     */
    private void changeModeClicked(int mode) {

        this.setMode(mode);

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.changeModeClicked(mode);
        }
    }

    /**
     * Getter for Mode
     *
     * @return current Mode
     * 0 = Normal
     * 1 = Girl
     */
    private int getMode() {
        return mode;
    }

    /**
     * Setter for Mode
     *
     * @param mode 0 = Normal
     *             1 = Girl
     */
    private void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Setter for Style (Background Color)
     *
     * @param style 0 = BLACK
     *              1 = INDIAN_RED
     */
    void setStyle(int style) {

        String color = null;
        if (style == 0) {
            color = BLACK;
        } else if (style == 1) {
            color = INDIAN_RED;
        }
        this.style = color;
    }

    /**
     * Set the Images dependent on the style
     *
     * @param style 0 = Normal
     *              1 = Girl
     */
    void setImages(int style) {

        try {
            this.symbols = new MinesweeperSymbols(style);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Window Icon
     **/
    private void setWindowIcon() {

        window.getIcons().add(symbols.WINDOW_ICON);
    }

    /**
     * Interface Methode. Wenn Pause angefordert wird, wird der Listener benachrichtigt.
     */
    private void pauseClicked() {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.pauseClicked();
        }
    }

    /**
     * Methode um anhand der ID des Buttons dessen Koordinaten zu bekommen
     *
     * @param index ID des Buttons
     * @return an Int Array of 2 Elements
     * [0] = row
     * [1] = col
     */
    private int[] getCoordinates(String index) {

        int[] coordinates = new int[2];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (buttonList[i][j].getId().equals(index)) {
                    coordinates[0] = i;
                    coordinates[1] = j;
                    break;
                }
            }
        }

        return coordinates;
    }

    /**
     * Methode um dem Button an der Position (row/col) das Symbol(figure) zu setzen
     *
     * @param figure Bombe oder Flag
     * @param row    Pos(row/col)
     * @param col    Pos(row/col)
     */
    void setButton(Image figure, int row, int col) {

        buttonList[row][col].setGraphic(new ImageView(figure));
    }

    /**
     * Methode um dem Button an der Position (row/col) die Nummer(number) zu setzen
     *
     * @param number Zahl von 1 bis 8
     * @param row    Pos(row/col)
     * @param col    Pos(row/col)
     */
    void setButton(int number, int row, int col, boolean backgroundColor) {

        if (number != 0) {
            String text = Integer.toString(number);
            buttonList[row][col].setTextFill(Paint.valueOf(setTextColor(number)));
            buttonList[row][col].setText(text);
            if (backgroundColor) {
                buttonList[row][col].setBackground(new Background(new BackgroundFill(Color.web("FFFF00"),
                        CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    /**
     * Je nach Nummer auf einem Button hat diese eine andere Farbe
     *
     * @param number Zahl die auf dem aufgedeckten Button steht
     * @return Farbe in der die Zahl geschrieben wird
     */
    private String setTextColor(int number) {

        switch (number) {
            case 1:
                return "#1F3590";
            case 2:
                return "#539024";
            case 3:
                return "#B01D1A";
            case 4:
                return "#15214C";
            case 5:
                return "#663F21";
            case 6:
                return "#FFB5B5";
            default:
                return "#000000";
        }
    }

    /**
     * Start Methode um den Listener zu registrieren
     *
     * @param listener Klasse die sich registriert
     */
    void addViewListener(ViewListenerMinesweeper listener) {

        viewListenerList.add(listener);
    }

    /**
     * Verlierer Benachrichtigung
     */
    void bombFieldNotification() {

        String content = "You clicked a Bomb. Good luck next time.";

        playAgainNotification("Game over!", content);
    }

    /**
     * Gewinner Benachrichtigung
     */
    void winningNotification() {

        String content = "Congratulations. You won the Game. \n" +
                "But you are not in Highscore. \n" +
                "Next time, you can do it.";

        playAgainNotification("You won!", content);
    }

    /**
     * Standard Notification Dialog for Replay
     *
     * @param title       Title of the Dialog
     * @param contentText Text of the Dialog
     */
    private void playAgainNotification(String title, String contentText) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setContentText(contentText);

        ButtonType buttonTypePlayAgain = new ButtonType("Play again");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypePlayAgain, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(null) == buttonTypePlayAgain) {
            newClicked();
        }  // ... user chose CANCEL or closed the dialog
    }

    /**
     * Benachrichtigung für Cooldown von der Tipp funktion.
     *
     * @param firstClickDone bool
     * @param seconds        seconds of timeout
     */
    void hintTimeoutNotification(boolean firstClickDone, long seconds) {

        String notificationText = (firstClickDone) ?
                "You must first click a field" :
                "Sorry, the hint is disabled for another " + seconds + " seconds.";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint Cooldown!");
        alert.setHeaderText(null);
        alert.setContentText(notificationText);
        alert.showAndWait();
    }

    /**
     * Notification for the Pause
     */
    void pauseNotification() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pause!");
        alert.setHeaderText(null);
        alert.setContentText("Pause");
        alert.showAndWait();
        timer.start();
    }

    /**
     * If the "New Game" Button is pressed the User have to confirm to start a
     *
     * @return bool if the User have confirmed then its true
     */
    boolean confirmationDialogNewClicked() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setContentText("Are u sure u want to start a new game?");

        ButtonType buttonTypePlayAgain = new ButtonType("Start new Game");
        ButtonType buttonTypeCancel = new ButtonType("Stay in old one", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypePlayAgain, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        return result.orElse(null) == buttonTypePlayAgain;
    }

}
