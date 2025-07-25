package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.ul.fc.css.soccernow.javafx.Main;
import pt.ul.fc.css.soccernow.javafx.model.Jogador;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EquipaRegisterView {
    private final ObservableList<Jogador> jogadoresDisponiveis = FXCollections.observableArrayList();
    private final ObservableList<Jogador> jogadoresSelecionados = FXCollections.observableArrayList();

    private static class FormFields {
        TextField txtNome;
        TextField  txtCidade;
        TextField  txtHistorico;
        TextField  txtConquistas;
        ComboBox<Jogador> comboJogadores;
        Button btnAdicionarJogador;
        Button  btnRemoverJogador;
        Button  btnRegistar;
        Button  btnCancelar;
        ListView<Jogador> listJogadoresSelecionados;
        Label lblStatus;
    }

    public void show(Stage stage) {
        stage.setTitle("Registo de Equipa");

        GridPane grid = criarGrid();

        FormFields fields = new FormFields();
        fields.txtNome = new TextField();
        fields.txtCidade = new TextField();
        fields.txtHistorico = new TextField();
        fields.txtConquistas = new TextField();
        fields.comboJogadores = criarComboJogadores();
        fields.listJogadoresSelecionados = criarListViewJogadores();
        fields.lblStatus = new Label();
        fields.btnAdicionarJogador = criarBtnAdicionarJogador(fields.comboJogadores);
        fields.btnRemoverJogador = criarBtnRemoverJogador(fields.comboJogadores, fields.listJogadoresSelecionados);
        fields.btnRegistar = new Button("Registar");
        fields.btnCancelar = new Button("Cancelar");

        fields.btnRegistar.setOnAction(e -> registarEquipa(
            fields.txtNome, fields.txtCidade, fields.txtHistorico, fields.txtConquistas,
            fields.listJogadoresSelecionados, stage, fields.lblStatus));

        fields.btnCancelar.setOnAction(e -> Main.showMenu(stage));

        adicionarComponentesGrid(grid, fields);

        stage.setScene(new Scene(grid, 650, 420));
        buscarJogadoresDisponiveis(fields.comboJogadores);
    }

    private GridPane criarGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        return grid;
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
            }
        });
        return btn;
    }

    private void adicionarComponentesGrid(GridPane grid, FormFields f) {
        grid.add(new Label("Nome da Equipa:"), 0, 0);
        grid.add(f.txtNome, 1, 0);
        grid.add(new Label("Cidade:"), 0, 1);
        grid.add(f.txtCidade, 1, 1);
        grid.add(new Label("Histórico de Jogos:"), 0, 2);
        grid.add(f.txtHistorico, 1, 2);
        grid.add(new Label("Conquistas:"), 0, 3);
        grid.add(f.txtConquistas, 1, 3);
        grid.add(new Label("Jogadores:"), 0, 4);
        grid.add(f.comboJogadores, 1, 4);
        grid.add(f.btnAdicionarJogador, 2, 4);
        grid.add(new Label("Selecionados:"), 0, 5);
        grid.add(f.listJogadoresSelecionados, 1, 5);
        grid.add(f.btnRemoverJogador, 2, 5);
        HBox hBoxBotoes = new HBox(12, f.btnRegistar, f.btnCancelar);
        hBoxBotoes.setStyle("-fx-alignment: center-left;");
        grid.add(hBoxBotoes, 1, 6);
        grid.add(f.lblStatus, 1, 7);
    }

    private void registarEquipa(TextField txtNome, TextField txtCidade, TextField txtHistorico, TextField txtConquistas,
                            ListView<Jogador> listJogadoresSelecionados, Stage stage, Label lblStatus) {
        String nome = txtNome.getText();
        String cidade = txtCidade.getText();
        String historico = txtHistorico.getText();
        String conquistas = txtConquistas.getText();

        if (nome.isEmpty() || cidade.isEmpty() || historico.isEmpty() || conquistas.isEmpty()) {
            lblStatus.setText("Todos os campos são obrigatórios!");
            return;
        }

        String json = new JSONObject()
            .put("nome", nome)
            .put("cidade", cidade)
            .put("historicoJogos", historico)
            .put("conquistas", conquistas)
            .toString();

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/equipas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(body -> Platform.runLater(() -> {
                try {
                    JSONObject equipaCriada = new JSONObject(body);
                    long equipaId = equipaCriada.getLong("id");
                    ObservableList<Jogador> selecionadosParaAdicionar = listJogadoresSelecionados.getItems();
                    if (!selecionadosParaAdicionar.isEmpty()) {
                        adicionarJogadoresAEquipa(equipaId, selecionadosParaAdicionar, stage);
                    } else {
                        mostrarSucesso(stage, "Equipa registada com sucesso!");
                    }
                } catch (Exception ex) {
                    lblStatus.setText("Erro ao criar equipa: " + ex.getMessage());
                }
            }));
    }

    private void mostrarSucesso(Stage stage, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensagem);
        alert.showAndWait();
        Main.showMenu(stage);
    }

    private void adicionarJogadoresAEquipa(long equipaId, ObservableList<Jogador> jogadores, Stage stage) {
        HttpClient client = HttpClient.newHttpClient();

        for (Jogador jogador : jogadores) {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/equipas/" + equipaId + "/" + jogador.getId()))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                });
        }

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Equipa registada com jogadores!");
            alert.showAndWait();
            Main.showMenu(stage);
        });
    }

    private void buscarJogadoresDisponiveis(ComboBox<Jogador> comboJogadores) {
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
                    atualizarComboJogadores(comboJogadores);
                }));
    }

    private void atualizarComboJogadores(ComboBox<Jogador> comboJogadores) {
        comboJogadores.getItems().setAll(
            jogadoresDisponiveis.filtered(j -> !jogadoresSelecionados.contains(j))
        );
    }
}