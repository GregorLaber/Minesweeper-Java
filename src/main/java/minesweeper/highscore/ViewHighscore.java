package main.java.minesweeper.highscore;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.List;

class ViewHighscore {

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
        table.getColumns().addAll(rankColumn, nameColumn, timeColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        GridPane content = new GridPane();
        content.add(table, 0, 0);

        alert.getDialogPane().setExpandableContent(content);
        alert.showAndWait();

    }

}
