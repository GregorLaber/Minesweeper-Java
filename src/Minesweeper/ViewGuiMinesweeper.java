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

class ViewGuiMinesweeper {

    private final Stage window = new Stage();
    private Scene sceneBeginner = null, sceneAdvanced = null, sceneProfessional = null;

    private int difficulty;
    private int ROW;
    private int COL;
    private int numberOfBombs;
    private Button[][] buttonList;
    private TextField bombNumberTextField;
    private static int k = 0;
    private static int id = 0;
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

    void startSetup(int numberOfBombs) {

        k = 0;
        id = 0;
        clearButtons();
        this.ROW = getDifficultyRow();
        this.COL = getDifficultyCol();
        this.buttonList = new Button[ROW][COL];
        initButtonList();
        this.numberOfBombs = numberOfBombs;
        this.bombNumberTextField = new TextField(); // Displays the number of Bombs in the Field
        setBombNumberTextField(numberOfBombs);
    }

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

    private void initTimer() {

        timer = new AnimationTimer() {

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

            @Override
            public void stop() {
                super.stop();
                lastTime = 0;
                minutes = 0;
                seconds = 0;
            }
        };
    }

    void startTimer() {

        labelTimer.setText("00:00");
        timer.start();
    }

    void stopTimerReset() {

        timer.stop();
        labelTimer.setText("00:00");
    }

    void stopTimer() {

        timer.stop();
    }

    private void initButtonList() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {

                Button button = new Button();
//                fieldNumbering(button); // for Debug purpose
                button.setMinSize(30, 30);
                button.setMaxSize(30, 30);
                String stringId = Integer.toString(id++);
                button.setId(stringId);

                buttonList[i][j] = button;
                buttonList[i][j].setFont(Font.font("Arial", FontWeight.BOLD, 12));
                buttonList[i][j].setOnMouseClicked(this::actionPerformed);
                buttonList[i][j].setDisable(false);
                buttonList[i][j].setMouseTransparent(false);
            }
        }
    }

    private void clearButtons() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttonList[i][j].setGraphic(null);
                this.buttonList[i][j].setText("");
            }
        }
    }

    void disableAllButtons() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttonList[i][j].setDisable(true);
            }
        }
    }

    void disableButton(int row, int col) {

        this.buttonList[row][col].setMouseTransparent(true);
    }

    void disableEmptyButton(int row, int col) {

        this.buttonList[row][col].setDisable(true);
    }

    void enableAllButtons() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttonList[i][j].setDisable(false);
                this.buttonList[i][j].setMouseTransparent(false);
            }
        }
    }

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

    private int getDifficultyRow() {

        if (difficulty == 0) {
            return 8;
        }
        return 16; // Case 1/2
    }

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

    private void fieldNumbering(Button button) {

        String text = Integer.toString(k++);
        button.setText(text);
        button.setFont(new Font("Arial Unicode MS", 11));
    }

    private MenuBar addMenu() {

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        Menu difficultyItem = new Menu("Difficulty");
        RadioMenuItem beginner = new RadioMenuItem("Beginner");
        RadioMenuItem advanced = new RadioMenuItem("Advanced");
        RadioMenuItem professional = new RadioMenuItem("Professional");
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
        menuBar.getMenus().addAll(fileMenu, difficultyItem);

        newGameItem.setOnAction((ActionEvent event) -> newClicked());
        beginner.setOnAction((ActionEvent event) -> changeDifficultyClicked(0));
        advanced.setOnAction((ActionEvent event) -> changeDifficultyClicked(1));
        professional.setOnAction((ActionEvent event) -> changeDifficultyClicked(2));

        return menuBar;
    }

    private HBox addToolBar() {

        String bombs = Integer.toString(numberOfBombs);
        ImageView imageView = new ImageView(symbols.MINE);
        Label bombIcon = new Label("", imageView);
        bombNumberTextField.setText(bombs);
        bombNumberTextField.setPrefColumnCount(2);
        bombNumberTextField.setEditable(false);
        ToolBar toolBar = new ToolBar(bombIcon, bombNumberTextField);
        toolBar.getItems().add(labelTimer);

        return new HBox(toolBar);
    }

    void setBombNumberTextField(int number) {

        String bombs = Integer.toString(number);
        bombNumberTextField.setText(bombs);
    }

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

    private void newClicked() {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.newClicked();
        }
    }

    private void changeDifficultyClicked(int difficulty) {

        for (ViewListenerMinesweeper viewListener : viewListenerList) {
            viewListener.changeDifficultyClicked(difficulty);
        }
    }

    /**
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

    void setButton(Image figure, int row, int col) {

        buttonList[row][col].setGraphic(new ImageView(figure));
    }

    void setButton(int number, int row, int col) {

        if (number != 0) {
            String text = Integer.toString(number);
            buttonList[row][col].setTextFill(Paint.valueOf(setTextColor(number)));
//            buttonList[row][col].setFont(Font.font("Arial", FontWeight.BOLD, 12));
            buttonList[row][col].setText(text);
        }
    }

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

    void addViewListener(ViewListenerMinesweeper listener) {

        viewListenerList.add(listener);
    }

    void bombFieldNotification() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game over!");
        alert.setHeaderText(null);
        alert.setContentText("You clicked a Bomb. Good luck next time.");
        alert.showAndWait();
    }

    void winningNotification() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You won!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations. You won the Game.");
        alert.showAndWait();
    }


}
