package Minesweeper;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Start Methode von JavaFX
     *
     * @param args Argumente von commandline
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Erste Methode die zu Spielbeginn gerufen wird. Initialer Startpunkt.
     *
     * @param primaryStage setzt das Window
     */
    @Override
    public void start(Stage primaryStage) {

        ControllerMinesweeper controller = new ControllerMinesweeper();
        controller.startGame();
    }
}
