package Minesweeper;


import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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
    private static int tileNumber = 0;
    private static int buttonID = 0;
    private final List<ViewListenerMinesweeper> viewListenerList = new ArrayList<>();
    private final MinesweeperSymbols symbols = new MinesweeperSymbols();

    private AnimationTimer timer;
    private final Label labelTimer = new Label("00:00");
    private int minutes;
    private int seconds;

    ViewGuiMinesweeper(int numberOfBombs) {

        setDifficulty(0);
        this.ROW = getDifficultyRow();
        this.COL = getDifficultyCol();
        this.numberOfBombs = numberOfBombs;
        this.buttonList = new Button[ROW][COL];
        this.bombNumberTextField = new TextField(); // Displays the number of Bombs in the Field

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
        Image icon = new Image(getClass().getResourceAsStream("pictures/redmineIcon.png"));
        window.getIcons().add(icon);
        window.setTitle("Minesweeper");
        window.setScene(sceneBeginner);
//        window.setMaximized(true); // For Professional Difficulty also have to switch resizable to true
        window.setResizable(true);
        window.show();
    }

    /**
     * Set the Scene to beginner. Set Menu, Toolbar and in the mid the Buttons.
     */
    private void setSceneBeginner() {

        BorderPane beginner = new BorderPane();
        String style = "-fx-background-color: #000000";
        beginner.setStyle(style);
        initTimer();
        beginner.setTop(addMenu());
        beginner.setCenter(addGridPane());
        beginner.setBottom(addToolBar());
        sceneBeginner = new Scene(beginner, 500, 450);
    }

    /**
     * Set the Scene to advanced. Set Menu, Toolbar and in the mid the Buttons.
     */
    private void setSceneAdvanced() {

        String style = "-fx-background-color: #000000";
        BorderPane advanced = new BorderPane();
        advanced.setStyle(style);
        initTimer();
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
        String style = "-fx-background-color: #000000";
        professional.setStyle(style);
        initTimer();
        professional.setTop(addMenu());
        professional.setCenter(addGridPane());
        professional.setBottom(addToolBar());
        sceneProfessional = new Scene(professional, 975, 700);
    }

    /**
     * toolbar Timer. Initialisation, doing and stop.
     */
    private void initTimer() {

        this.timer = new AnimationTimer() {

            private long lastTime = 0;
            private String displaySeconds;
            private String displayMinutes;

            @Override
            public void handle(long now) {
                if (lastTime != 0) {
                    if (now > lastTime + 1_000_000_000) {
                        seconds++;
                        if (seconds == 60) {
                            minutes++;
                            seconds = 0;
                        }

                        if (seconds < 10) {
                            displaySeconds = "0" + seconds;
                        } else {
                            displaySeconds = Integer.toString(seconds);
                        }

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
    void startTimer() {

        timer.start();
    }

    /**
     * toolbar Timer. Stop and Reset the Timer
     */
    void stopTimerReset() {

        timer.stop();
        labelTimer.setText("00:00");
        this.initTimer();
        this.minutes = 0;
        this.seconds = 0;
    }

    /**
     * toolbar Timer. Only Stop the Timer
     */
    void stopTimer() {

        timer.stop();
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

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        Menu difficultyItem = new Menu("Difficulty");
        RadioMenuItem beginner = new RadioMenuItem("Beginner");
        RadioMenuItem advanced = new RadioMenuItem("Advanced");
        RadioMenuItem professional = new RadioMenuItem("Professional");
        Menu hintMenu = new Menu("Hint");
        MenuItem hintItem = new MenuItem("Get Hint");
        ToggleGroup group = new ToggleGroup();
        beginner.setToggleGroup(group);
        advanced.setToggleGroup(group);
        professional.setToggleGroup(group);
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

        fileMenu.getItems().add(newGameItem);
        difficultyItem.getItems().addAll(beginner, advanced, professional);
        hintMenu.getItems().add(hintItem);
        menuBar.getMenus().addAll(fileMenu, difficultyItem, hintMenu);

        newGameItem.setOnAction((ActionEvent event) -> newClicked());
        beginner.setOnAction((ActionEvent event) -> changeDifficultyClicked(0));
        advanced.setOnAction((ActionEvent event) -> changeDifficultyClicked(1));
        professional.setOnAction((ActionEvent event) -> changeDifficultyClicked(2));
        hintItem.setOnAction((ActionEvent event) -> hintClicked());

        return menuBar;
    }

    /**
     * Methode um die Statusleiste zu erzeugen.
     * Von links nach rechts: BombenIcon, Textfeld mit der Anzahl der übrigen Bomben, Timer
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
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction((ActionEvent event) -> pauseClicked());

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(bombIcon, bombNumberTextField, labelTimer, pauseButton);

        return new HBox(toolBar);
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
    void setButton(int number, int row, int col) {

        if (number != 0) {
            String text = Integer.toString(number);
            buttonList[row][col].setTextFill(Paint.valueOf(setTextColor(number)));
//            buttonList[row][col].setFont(Font.font("Arial", FontWeight.BOLD, 12));
            buttonList[row][col].setText(text);
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game over!");
        alert.setHeaderText(null);
        alert.setContentText("You clicked a Bomb. Good luck next time.");
        alert.showAndWait();
    }

    /**
     * Gewinner Benachrichtigung
     */
    void winningNotification() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You won!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations. You won the Game.");
        alert.showAndWait();
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

    void pauseNotification() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pause!");
        alert.setHeaderText(null);
        alert.setContentText("Pause");
        alert.showAndWait();
        timer.start();
    }

}
