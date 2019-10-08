package main.java.minesweeper.highscore;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

import java.util.List;

class ViewHighscore {

    /**
     * Popup with Highscore table as content.
     * @param playerList is the Data that will be displayed. Depending in Difficulty.
     */
    void showHighscore(List<Player> playerList) {

        ObservableList<Player> data = FXCollections.observableArrayList();
        data.addAll(playerList);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Highscore table");
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
     * Notification to ask the User for his/her Name
     *
     * @return Name of the User
     */
    String highscoreNotification() {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Highscore");
        dialog.setHeaderText("Congratulation, you won the Game and are also in the Highscore. \n" +
                "Please type in your name.");
        dialog.setContentText("Name:");

        while (true) {

            //TODO handle reject
            String result = dialog.showAndWait().orElse(null);
            if (!result.isEmpty()) {
                return result;
            } else if (result == null) {
                return "User";
            } else {
                highscoreNotification();
            }
        }

    }

}
