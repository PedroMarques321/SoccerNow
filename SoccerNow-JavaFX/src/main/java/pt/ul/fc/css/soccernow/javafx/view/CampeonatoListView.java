package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class CampeonatoListView {
    private final ObservableList<CampeonatoResumo> campeonatos = FXCollections.observableArrayList();

    public void show(Stage stage) {
        stage.setTitle("Lista de Campeonatos");

        TableView<CampeonatoResumo> table = new TableView<>(campeonatos);

        TableColumn<CampeonatoResumo, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(data -> data.getValue().nomeProperty());

        TableColumn<CampeonatoResumo, String> dataInicioCol = new TableColumn<>("Início");
        dataInicioCol.setCellValueFactory(data -> data.getValue().dataInicioProperty());

        TableColumn<CampeonatoResumo, String> dataFimCol = new TableColumn<>("Fim");
        dataFimCol.setCellValueFactory(data -> data.getValue().dataFimProperty());

        TableColumn<CampeonatoResumo, String> modalidadeCol = new TableColumn<>("Modalidade");
        modalidadeCol.setCellValueFactory(data -> data.getValue().modalidadeProperty());

        table.getColumns().addAll(nomeCol, dataInicioCol, dataFimCol, modalidadeCol);

        Button btnAtualizar = new Button("Atualizar");
        Button btnRemover = new Button("Remover");
        Button btnVoltar = new Button("Voltar");

        btnAtualizar.setMinWidth(120);
        btnRemover.setMinWidth(120);
        btnVoltar.setMinWidth(120);

        btnAtualizar.setFont(javafx.scene.text.Font.font(15));
        btnRemover.setFont(javafx.scene.text.Font.font(15));
        btnVoltar.setFont(javafx.scene.text.Font.font(15));

        btnVoltar.setOnAction(e -> pt.ul.fc.css.soccernow.javafx.Main.showMenu(stage));

        btnAtualizar.setDisable(true);
        btnRemover.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            btnAtualizar.setDisable(selected == null);
            btnRemover.setDisable(selected == null);
        });

        btnAtualizar.setOnAction(e -> {
            CampeonatoResumo c = table.getSelectionModel().getSelectedItem();
            if (c != null) {
                new CampeonatoUpdateView().show(stage, c);
            }
        });

        btnRemover.setOnAction(e -> {
            CampeonatoResumo c = table.getSelectionModel().getSelectedItem();
            if (c != null) {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/campeonatos/" + c.getId()))
                        .DELETE()
                        .build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(resp -> Platform.runLater(() -> {
                            campeonatos.remove(c);
                        }));
            }
        });

        HBox hBoxBotoes = new HBox(20, btnAtualizar, btnRemover, btnVoltar);
        hBoxBotoes.setPadding(new Insets(20, 0, 30, 0));
        hBoxBotoes.setAlignment(javafx.geometry.Pos.CENTER);

        VBox root = new VBox(16, table, hBoxBotoes);
        root.setPadding(new Insets(20, 20, 20, 20));

        stage.setScene(new Scene(root, 600, 400));
        stage.show();

        carregarCampeonatos();
    }

    private void carregarCampeonatos() {
        campeonatos.clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/campeonatos"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> {
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        campeonatos.add(new CampeonatoResumo(
                                obj.getLong("id"),
                                obj.getString("nome"),
                                obj.optString("dataInicio", ""),
                                obj.optString("dataFim", ""),
                                obj.optString("modalidade", "")
                        ));
                    }
                }));
    }

    public static class CampeonatoResumo {
        private final javafx.beans.property.SimpleLongProperty id;
        private final javafx.beans.property.SimpleStringProperty nome;
        private final javafx.beans.property.SimpleStringProperty dataInicio;
        private final javafx.beans.property.SimpleStringProperty dataFim;
        private final javafx.beans.property.SimpleStringProperty modalidade;

        public CampeonatoResumo(long id, String nome, String dataInicio, String dataFim, String modalidade) {
            this.id = new javafx.beans.property.SimpleLongProperty(id);
            this.nome = new javafx.beans.property.SimpleStringProperty(nome);
            this.dataInicio = new javafx.beans.property.SimpleStringProperty(dataInicio);
            this.dataFim = new javafx.beans.property.SimpleStringProperty(dataFim);
            this.modalidade = new javafx.beans.property.SimpleStringProperty(modalidade);
        }

        public long getId() { return id.get(); }
        public javafx.beans.property.SimpleLongProperty idProperty() { return id; }
        public String getNome() { return nome.get(); }
        public javafx.beans.property.SimpleStringProperty nomeProperty() { return nome; }
        public String getDataInicio() { return dataInicio.get(); }
        public javafx.beans.property.SimpleStringProperty dataInicioProperty() { return dataInicio; }
        public String getDataFim() { return dataFim.get(); }
        public javafx.beans.property.SimpleStringProperty dataFimProperty() { return dataFim; }
        public String getModalidade() { return modalidade.get(); }
        public javafx.beans.property.SimpleStringProperty modalidadeProperty() { return modalidade; }
    }
}