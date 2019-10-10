package main.java.minesweeper.highscore;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ViewHighscore {

    private final List<ViewListenerHighscore> viewListenerList = new ArrayList<>();

    ViewHighscore() {
    }

    /**
     * Popup with Highscore table as content.
     *
     * @param playerList is the Data that will be displayed. Depending on Difficulty.
     */
    void showHighscore(List<Player> playerList, int difficulty) {

        ObservableList<Player> data = FXCollections.observableArrayList();
        data.addAll(playerList);

        String difficultyName;
        switch (difficulty) {
            case 1:
                difficultyName = "Advanced";
                break;
            case 2:
                difficultyName = "Professional";
                break;
            default:
                difficultyName = "Beginner";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Highscore table " + difficultyName);
        alert.setHeaderText(null);
        alert.setGraphic(null);

        TableView<Player> table = new TableView<>();
        table.setEditable(false);

        TableColumn<Player, Integer> rankColumn = new TableColumn<>("Rank");
        TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Player, String> timeColumn = new TableColumn<>("Time");

        rankColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getRank()));
        nameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        timeColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTime()));

        table.setItems(data);
        //noinspection unchecked
        table.getColumns().addAll(rankColumn, nameColumn, timeColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        GridPane content = new GridPane();
        content.add(table, 0, 0);
        content.setMaxWidth(Double.MAX_VALUE);

        alert.getDialogPane().setContent(content);
        alert.showAndWait();

    }

    /**
     * Notification to ask the User for his/her Name<br>
     * - If the dialog is closed/rejected without any Input the method return "Player".<br>
     * - If the ok button is pressed without any input a new Dialog pop up.<br>
     * - If the user writes type something in the textfield, the method returns this input.
     *
     * @return Name of the User or default Name: Player
     */
    String highscoreNotification() {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Highscore");
        dialog.setHeaderText("Congratulation, you won the Game and are also in the Highscore. \n" +
                "Please type in your name.");
        dialog.setContentText("Name:");

        String result = dialog.showAndWait().orElse(null);
        if ((result != null && !result.isEmpty())) {
            return result;
        } else if (result == null) {
            return "Player";
        } else {
            highscoreNotification();
        }

        return "Player";
    }

    /**
     * Dialog for delete one or all Highscore tables.
     */
    void deleteHighscoreDialog() {

        List<String> choices = new ArrayList<>();
        choices.add("Nothing");
        choices.add("Beginner");
        choices.add("Advanced");
        choices.add("Professional");
        choices.add("All tables");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Nothing", choices);
        dialog.setTitle("Delete Highscore table");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Choose your table you want to delete: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::confirmationDialog);

    }

    /**
     * If the User want to delete a table he had to confirm that.
     */
    private void confirmationDialog(String table) {

        if (!table.equals("Nothing")) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Are you sure, you want to delete " + table);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(null) == ButtonType.OK) {
                switch (table) {
                    case "Beginner":
                        this.deleteHighscoreClicked(0);
                        break;
                    case "Advanced":
                        this.deleteHighscoreClicked(1);
                        break;
                    case "Professional":
                        this.deleteHighscoreClicked(2);
                        break;
                    case "All tables":
                        this.deleteHighscoreClicked(3);
                        break;
                }
            }
        }

    }

    /**
     * Start Methode um den Listener zu registrieren
     *
     * @param listener Klasse die sich registriert
     */
    void addViewListener(ViewListenerHighscore listener) {

        viewListenerList.add(listener);
    }

    /**
     * Interface Methode. Wenn Button geklickt wird, wird der Listener benachrichtigt.
     */
    private void deleteHighscoreClicked(int difficulty) {

        for (ViewListenerHighscore viewListener : viewListenerList) {
            viewListener.deleteHighscoreClicked(difficulty);
        }
    }

}
