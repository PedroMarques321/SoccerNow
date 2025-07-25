package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.ul.fc.css.soccernow.javafx.Main;
import pt.ul.fc.css.soccernow.javafx.model.Equipa;
import pt.ul.fc.css.soccernow.javafx.model.Jogador;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EquipaListView {
    private final ObservableList<Equipa> equipas = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    public void show(Stage stage) {
        TableView<Equipa> table = new TableView<>(equipas);

        TableColumn<Equipa, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(data -> data.getValue().nomeProperty());

        TableColumn<Equipa, String> cidadeCol = new TableColumn<>("Cidade");
        cidadeCol.setCellValueFactory(data -> data.getValue().cidadeProperty());

        TableColumn<Equipa, String> historicoCol = new TableColumn<>("Histórico");
        historicoCol.setCellValueFactory(data -> data.getValue().historicoJogosProperty());

        TableColumn<Equipa, String> conquistasCol = new TableColumn<>("Conquistas");
        conquistasCol.setCellValueFactory(data -> data.getValue().conquistasProperty());

        TableColumn<Equipa, Number> numJogadoresCol = new TableColumn<>("Nº Jogadores");
            numJogadoresCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getJogadores() != null ? data.getValue().getJogadores().size() : 0
            ));

        table.getColumns().addAll(nomeCol, cidadeCol, historicoCol, conquistasCol, numJogadoresCol);

        Button btnAtualizar = new Button("Atualizar");
        Button btnRemover = new Button("Remover");
        Button btnVoltar = new Button("Voltar");

        btnAtualizar.setOnAction(e -> {
            Equipa selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                new EquipaUpdateView().show(stage, selected);
            }
        });

        btnRemover.setOnAction(e -> {
            Equipa selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                removerEquipa(selected.getId(), this::buscarEquipas);
            }
        });

        btnVoltar.setOnAction(e -> Main.showMenu(stage));

        btnAtualizar.setMinWidth(120);
        btnRemover.setMinWidth(120);
        btnVoltar.setMinWidth(120);
        btnAtualizar.setFont(javafx.scene.text.Font.font(15));
        btnRemover.setFont(javafx.scene.text.Font.font(15));
        btnVoltar.setFont(javafx.scene.text.Font.font(15));

        HBox buttons = new HBox(20, btnAtualizar, btnRemover, btnVoltar);
        buttons.setPadding(new javafx.geometry.Insets(20, 0, 30, 0));
        buttons.setAlignment(javafx.geometry.Pos.CENTER);

        VBox root = new VBox(16, table, buttons);
        root.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Lista de Equipas");
        stage.show();

        buscarEquipas();
    }

    private void buscarEquipas() {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/equipas"))
            .build();

    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(json -> Platform.runLater(() -> {
                equipas.clear();
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    java.util.List<Jogador> jogadores = new java.util.ArrayList<>();
                    if (obj.has("jogadores")) {
                        JSONArray jogadoresArr = obj.getJSONArray("jogadores");
                        for (int j = 0; j < jogadoresArr.length(); j++) {
                            JSONObject jogObj = jogadoresArr.getJSONObject(j);
                            jogadores.add(new Jogador(
                                jogObj.getLong("id"),
                                jogObj.getString("username"),
                                jogObj.getString("email"),
                                jogObj.optString("posicao", ""),
                                jogObj.optInt("numeroCamisa", 0)
                            ));
                        }
                    }

                    equipas.add(new Equipa(
                            obj.getLong("id"),
                            obj.getString("nome"),
                            obj.getString("cidade"),
                            obj.optString("historicoJogos", ""),
                            obj.optString("conquistas", ""),
                            jogadores
                    ));
                }
            }));
    }

    private void removerEquipa(long id, Runnable onSuccess) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/equipas/" + id))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> Platform.runLater(() -> {
                    if (resp.statusCode() == 200 || resp.statusCode() == 204) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Equipa removida com sucesso!");
                        alert.showAndWait();
                        onSuccess.run();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao remover: " + resp.statusCode());
                        alert.showAndWait();
                    }
                }));
    }
}