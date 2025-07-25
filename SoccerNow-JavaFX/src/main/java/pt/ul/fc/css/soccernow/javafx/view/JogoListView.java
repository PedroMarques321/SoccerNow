package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.ul.fc.css.soccernow.javafx.model.Equipa;
import pt.ul.fc.css.soccernow.javafx.model.Campeonato;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JogoListView {

    private final ObservableList<JogoResumo> jogos = FXCollections.observableArrayList();
    private final ObservableList<Equipa> equipas = FXCollections.observableArrayList();
    private final ObservableList<Campeonato> campeonatos = FXCollections.observableArrayList();

    public void show(Stage stage) {
        stage.setTitle("Lista de Jogos");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));

        TableView<JogoResumo> table = new TableView<>(jogos);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<JogoResumo, String> colCasa = new TableColumn<>("Equipa Casa");
        colCasa.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().equipaCasa));
        TableColumn<JogoResumo, String> colFora = new TableColumn<>("Equipa Fora");
        colFora.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().equipaFora));
        TableColumn<JogoResumo, String> colLocal = new TableColumn<>("Local");
        colLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().local));
        TableColumn<JogoResumo, String> colData = new TableColumn<>("Data/Hora");
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().dataHora));
        TableColumn<JogoResumo, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().estado));
        TableColumn<JogoResumo, String> colTipo = new TableColumn<>("Tipo de Jogo");
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().tipoJogo));
        TableColumn<JogoResumo, String> colCampeonato = new TableColumn<>("Campeonato");
        colCampeonato.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().campeonato));
        TableColumn<JogoResumo, String> colGolosCasa = new TableColumn<>("Golos Casa");
        colGolosCasa.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().golosCasa != null ? data.getValue().golosCasa.toString() : ""));
        TableColumn<JogoResumo, String> colGolosFora = new TableColumn<>("Golos Fora");
        colGolosFora.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().golosFora != null ? data.getValue().golosFora.toString() : ""));

        table.getColumns().setAll(colCasa, colFora, colLocal, colData, colEstado, colTipo, colCampeonato, colGolosCasa, colGolosFora);

        Button btnResultado = new Button("Registar Resultado");
        Button btnAtualizar = new Button("Atualizar Estado");
        Button btnVoltar = new Button("Voltar");

        btnResultado.setDisable(true);
        btnAtualizar.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                btnResultado.setDisable(!(selected.estado.equals("AGENDADO") || selected.estado.equals("EM_ANDAMENTO")));
                btnAtualizar.setDisable(false);
            } else {
                btnResultado.setDisable(true);
                btnAtualizar.setDisable(true);
            }
        });

        btnResultado.setOnAction(e -> {
            JogoResumo selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                mostrarDialogoResultado(selected.id, selected.equipaCasa, selected.equipaFora);
            }
        });

        btnAtualizar.setOnAction(e -> {
            JogoResumo selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                mostrarDialogoAtualizarEstado(selected.id, selected.estado, selected.dataHora);
            }
        });

        btnVoltar.setOnAction(e -> pt.ul.fc.css.soccernow.javafx.Main.showMenu(stage));

        HBox hBox = new HBox(16, btnResultado, btnAtualizar, btnVoltar);
        hBox.setPadding(new Insets(12, 0, 0, 0));

        root.setCenter(table);
        root.setBottom(hBox);

        stage.setScene(new Scene(root, 900, 500));
        buscarCampeonatos(() -> buscarEquipas(() -> buscarJogos()));
    }

    // Carrega campeonatos antes de equipas e jogos
    private void buscarCampeonatos(Runnable onFinish) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/campeonatos"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> {
                    campeonatos.clear();
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        campeonatos.add(new Campeonato(
                                obj.getLong("id"),
                                obj.getString("nome")
                        ));
                    }
                    onFinish.run();
                }));
    }

    // Carrega as equipas antes de carregar os jogos
    private void buscarEquipas(Runnable onFinish) {
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
                        equipas.add(new Equipa(
                                obj.getLong("id"),
                                obj.getString("nome"),
                                obj.getString("cidade"),
                                obj.optString("historicoJogos", ""),
                                obj.optString("conquistas", "")
                        ));
                    }
                    onFinish.run();
                }));
    }

    private String getNomeEquipaById(long id) {
        for (Equipa e : equipas) {
            if (e.getId() == id) return e.getNome();
        }
        return "";
    }

    private String getNomeCampeonatoById(long id) {
        for (Campeonato c : campeonatos) {
            if (c.getId() == id) return c.getNome();
        }
        return "";
    }

    private void buscarJogos() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/jogos"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> {
                    jogos.clear();
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String equipaCasa = "";
                        String equipaFora = "";
                        if (obj.has("equipaCasa") && !obj.isNull("equipaCasa")) {
                            equipaCasa = obj.getJSONObject("equipaCasa").optString("nome", "");
                        } else if (obj.has("equipaCasaNome")) {
                            equipaCasa = obj.optString("equipaCasaNome", "");
                        } else if (obj.has("equipaCasaId")) {
                            equipaCasa = getNomeEquipaById(obj.getLong("equipaCasaId"));
                        }
                        if (obj.has("equipaVisitante") && !obj.isNull("equipaVisitante")) {
                            equipaFora = obj.getJSONObject("equipaVisitante").optString("nome", "");
                        } else if (obj.has("equipaVisitanteNome")) {
                            equipaFora = obj.optString("equipaVisitanteNome", "");
                        } else if (obj.has("equipaVisitanteId")) {
                            equipaFora = getNomeEquipaById(obj.getLong("equipaVisitanteId"));
                        }
                        Integer golosCasa = obj.has("golosCasa") && !obj.isNull("golosCasa") ? obj.getInt("golosCasa") : null;
                        Integer golosFora = obj.has("golosFora") && !obj.isNull("golosFora") ? obj.getInt("golosFora") : null;

                        // Determinar tipo de jogo e campeonato
                        String tipoJogo = "Campeonato";
                        if (obj.has("jogoAmigavel") && obj.getBoolean("jogoAmigavel")) {
                            tipoJogo = "Amigável";
                        }
                        String campeonato = "";
                        if (obj.has("campeonato") && !obj.isNull("campeonato")) {
                            campeonato = obj.getJSONObject("campeonato").optString("nome", "");
                        } else if (obj.has("campeonatoNome")) {
                            campeonato = obj.optString("campeonatoNome", "");
                        } else if (obj.has("campeonatoId") && !obj.isNull("campeonatoId")) {
                            campeonato = getNomeCampeonatoById(obj.getLong("campeonatoId"));
                        }

                        jogos.add(new JogoResumo(
                                obj.getLong("id"),
                                equipaCasa,
                                equipaFora,
                                obj.optString("local", ""),
                                obj.optString("dataHora", ""),
                                obj.optString("estado", ""),
                                tipoJogo,
                                campeonato,
                                golosCasa,
                                golosFora
                        ));
                    }
                }));
    }

    private void mostrarDialogoResultado(long jogoId, String equipaCasa, String equipaFora) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registar Resultado");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        TextField txtCasa = new TextField();
        TextField txtFora = new TextField();

        grid.add(new Label("Golos " + equipaCasa + ":"), 0, 0);
        grid.add(txtCasa, 1, 0);
        grid.add(new Label("Golos " + equipaFora + ":"), 0, 1);
        grid.add(txtFora, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    int golosCasa = Integer.parseInt(txtCasa.getText());
                    int golosFora = Integer.parseInt(txtFora.getText());
                    registarResultadoBackend(jogoId, golosCasa, golosFora);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Insira números válidos.");
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void mostrarDialogoAtualizarEstado(long jogoId, String estadoAtual, String dataHoraAtual) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Atualizar Estado/Data do Jogo");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        ComboBox<String> comboEstado = new ComboBox<>();
        comboEstado.getItems().setAll("AGENDADO", "EM_ANDAMENTO", "FINALIZADO", "CANCELADO", "ADIADO");
        comboEstado.setValue(estadoAtual);

        TextField txtDataHora = new TextField(dataHoraAtual);

        grid.add(new Label("Novo Estado:"), 0, 0);
        grid.add(comboEstado, 1, 0);
        grid.add(new Label("Nova Data/Hora:"), 0, 1);
        grid.add(txtDataHora, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String novoEstado = comboEstado.getValue();
                String novaDataHora = txtDataHora.getText();
                atualizarEstadoEDataBackend(jogoId, novoEstado, novaDataHora);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void registarResultadoBackend(long jogoId, int golosCasa, int golosFora) {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject json = new JSONObject();
        json.put("golosCasa", golosCasa);
        json.put("golosFora", golosFora);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/jogos/" + jogoId + "/resultado"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> Platform.runLater(() -> {
                    if (resp.statusCode() == 200) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Resultado registado com sucesso!");
                        alert.showAndWait();
                        buscarJogos();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao registar resultado: " + resp.statusCode());
                        alert.showAndWait();
                    }
                }));
    }

    private void atualizarEstadoEDataBackend(long jogoId, String novoEstado, String novaDataHora) {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject json = new JSONObject();
        json.put("estado", novoEstado);
        json.put("dataHora", novaDataHora);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/jogos/" + jogoId + "/estado"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> Platform.runLater(() -> {
                    if (resp.statusCode() == 200) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Estado/data atualizados com sucesso!");
                        alert.showAndWait();
                        buscarJogos();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar: " + resp.statusCode());
                        alert.showAndWait();
                    }
                }));
    }

    private static class JogoResumo {
        final long id;
        final String equipaCasa;
        final String equipaFora;
        final String local;
        final String dataHora;
        final String estado;
        final Integer golosCasa;
        final Integer golosFora;
        final String tipoJogo;
        final String campeonato;

        JogoResumo(long id, String equipaCasa, String equipaFora, String local, String dataHora, String estado, String tipoJogo, String campeonato, Integer golosCasa, Integer golosFora) {
            this.id = id;
            this.equipaCasa = equipaCasa;
            this.equipaFora = equipaFora;
            this.local = local;
            this.dataHora = dataHora;
            this.estado = estado;
            this.golosCasa = golosCasa;
            this.golosFora = golosFora;
            this.tipoJogo = tipoJogo;
            this.campeonato = campeonato;
        }
    }
}