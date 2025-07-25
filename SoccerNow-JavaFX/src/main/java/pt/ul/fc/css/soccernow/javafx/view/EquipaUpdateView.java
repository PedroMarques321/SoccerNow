package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.javafx.model.Equipa;
import pt.ul.fc.css.soccernow.javafx.model.Jogador;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EquipaUpdateView {
    private final ObservableList<Jogador> jogadoresDisponiveis = FXCollections.observableArrayList();
    private final ObservableList<Jogador> jogadoresSelecionados = FXCollections.observableArrayList();

    public void show(Stage stage, Equipa equipa) {
        stage.setTitle("Atualizar Equipa");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField(equipa.getNome());

        Label lblCidade = new Label("Cidade:");
        TextField txtCidade = new TextField(equipa.getCidade());

        Label lblHistorico = new Label("Histórico de Jogos:");
        TextField txtHistorico = new TextField(equipa.getHistoricoJogos());

        Label lblConquistas = new Label("Conquistas:");
        TextField txtConquistas = new TextField(equipa.getConquistas());

        ComboBox<Jogador> comboJogadores = criarComboJogadores();
        ListView<Jogador> listJogadoresSelecionados = criarListViewJogadores();
        Label lblStatus = new Label();

        Button btnAdicionarJogador = criarBtnAdicionarJogador(comboJogadores);
        Button btnRemoverJogador = criarBtnRemoverJogador(comboJogadores, listJogadoresSelecionados);

        Button btnAtualizar = new Button("Atualizar");
        Button btnCancelar = new Button("Cancelar");

        btnAtualizar.setOnAction(e -> {
            String nome = txtNome.getText();
            String cidade = txtCidade.getText();
            String historico = txtHistorico.getText();
            String conquistas = txtConquistas.getText();

            String json = new JSONObject()
                    .put("nome", nome)
                    .put("cidade", cidade)
                    .put("historicoJogos", historico)
                    .put("conquistas", conquistas)
                    .toString();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/equipas/" + equipa.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .thenAccept(status -> Platform.runLater(() -> {
                        if (status == 200) {
                            atualizarJogadoresDaEquipa(equipa.getId(), jogadoresSelecionados, stage);
                        } else {
                            lblStatus.setText("Erro ao atualizar: " + status);
                        }
                    }));
        });

        btnCancelar.setOnAction(e -> new EquipaListView().show(stage));

        grid.add(lblNome, 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(lblCidade, 0, 1);
        grid.add(txtCidade, 1, 1);
        grid.add(lblHistorico, 0, 2);
        grid.add(txtHistorico, 1, 2);
        grid.add(lblConquistas, 0, 3);
        grid.add(txtConquistas, 1, 3);
        grid.add(new Label("Jogadores:"), 0, 4);
        grid.add(comboJogadores, 1, 4);
        grid.add(btnAdicionarJogador, 2, 4);
        grid.add(new Label("Selecionados:"), 0, 5);
        grid.add(listJogadoresSelecionados, 1, 5);
        grid.add(btnRemoverJogador, 2, 5);
        HBox hBoxBotoes = new HBox(20, btnAtualizar, btnCancelar);
        hBoxBotoes.setAlignment(Pos.CENTER_LEFT);
        grid.add(hBoxBotoes, 1, 6);
        grid.add(lblStatus, 0, 7, 2, 1);

        stage.setScene(new Scene(grid, 650, 420));
        buscarJogadoresDisponiveis(comboJogadores, equipa);
    }

    private ComboBox<Jogador> criarComboJogadores() {
        ComboBox<Jogador> combo = new ComboBox<>();
        combo.setPromptText("Selecionar jogador");
        combo.setCellFactory(lv -> new ListCell<Jogador>() {
            @Override
            protected void updateItem(Jogador jogador, boolean empty) {
                super.updateItem(jogador, empty);
                setText(empty || jogador == null ? "" : jogador.getUsername());
            }
        });
        combo.setButtonCell(new ListCell<Jogador>() {
            @Override
            protected void updateItem(Jogador jogador, boolean empty) {
                super.updateItem(jogador, empty);
                setText(empty || jogador == null ? "" : jogador.getUsername());
            }
        });
        return combo;
    }

    private ListView<Jogador> criarListViewJogadores() {
        ListView<Jogador> list = new ListView<>(jogadoresSelecionados);
        list.setPrefHeight(100);
        list.setCellFactory(lv -> new ListCell<Jogador>() {
            @Override
            protected void updateItem(Jogador jogador, boolean empty) {
                super.updateItem(jogador, empty);
                setText(empty || jogador == null ? "" : jogador.getUsername());
            }
        });
        return list;
    }

    private Button criarBtnAdicionarJogador(ComboBox<Jogador> comboJogadores) {
        Button btn = new Button("Adicionar");
        btn.setOnAction(e -> {
            Jogador selecionado = comboJogadores.getValue();
            if (selecionado != null && !jogadoresSelecionados.contains(selecionado)) {
                jogadoresSelecionados.add(selecionado);
                atualizarComboJogadores(comboJogadores);
                comboJogadores.setValue(null);
            }
        });
        return btn;
    }

    private Button criarBtnRemoverJogador(ComboBox<Jogador> comboJogadores, ListView<Jogador> listJogadoresSelecionados) {
        Button btn = new Button("Remover");
        btn.setOnAction(e -> {
            Jogador selecionado = listJogadoresSelecionados.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                jogadoresSelecionados.remove(selecionado);
                atualizarComboJogadores(comboJogadores);
                comboJogadores.setValue(null);
            }
        });
        return btn;
    }

    private void buscarJogadoresDisponiveis(ComboBox<Jogador> comboJogadores, Equipa equipa) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/jogadores"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> {
                    jogadoresDisponiveis.clear();
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        jogadoresDisponiveis.add(new Jogador(
                                obj.getLong("id"),
                                obj.getString("username"),
                                obj.getString("email"),
                                obj.getString("posicao"),
                                obj.optInt("numeroCamisa", 0)
                        ));
                    }
                    jogadoresSelecionados.clear();
                    if (equipa.getJogadores() != null) {
                        for (Jogador jogadorEquipa : equipa.getJogadores()) {
                            jogadoresDisponiveis.stream()
                                .filter(j -> j.getId() == jogadorEquipa.getId())
                                .findFirst()
                                .ifPresent(jogadoresSelecionados::add);
                        }
                    }
                    atualizarComboJogadores(comboJogadores);
                }));
    }

    private void atualizarComboJogadores(ComboBox<Jogador> comboJogadores) {
        comboJogadores.getItems().setAll(
            jogadoresDisponiveis.filtered(j -> !jogadoresSelecionados.contains(j))
        );
    }

    private void atualizarJogadoresDaEquipa(long equipaId, ObservableList<Jogador> jogadores, Stage stage) {
        HttpClient client = HttpClient.newHttpClient();
        JSONArray jogadoresIds = new JSONArray();
        for (Jogador j : jogadores) {
            jogadoresIds.put(j.getId());
        }
        JSONObject json = new JSONObject().put("jogadoresIds", jogadoresIds);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/equipas/" + equipaId + "/jogadores"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenAccept(resp -> Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Equipa atualizada com jogadores!");
                alert.showAndWait();
                new EquipaListView().show(stage);
            }));
    }
}