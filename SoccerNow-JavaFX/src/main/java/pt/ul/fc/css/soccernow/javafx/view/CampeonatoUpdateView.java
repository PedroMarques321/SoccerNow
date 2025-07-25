package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CampeonatoUpdateView {

    public void show(Stage stage, CampeonatoListView.CampeonatoResumo campeonato) {
        stage.setTitle("Atualizar Campeonato");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField(campeonato.getNome());

        Label lblDataInicio = new Label("Data Início (yyyy-MM-dd):");
        TextField txtDataInicio = new TextField(campeonato.getDataInicio());

        Label lblDataFim = new Label("Data Fim (yyyy-MM-dd):");
        TextField txtDataFim = new TextField(campeonato.getDataFim());

        Label lblModalidade = new Label("Modalidade:");
        ComboBox<String> comboModalidade = new ComboBox<>();
        comboModalidade.getItems().addAll("PONTOS", "ELIMINATORIA");
        comboModalidade.setValue(campeonato.getModalidade());

        Label lblStatus = new Label();

        Button btnAtualizar = new Button("Atualizar");
        Button btnCancelar = new Button("Cancelar");

        btnAtualizar.setOnAction(e -> {
            String nome = txtNome.getText();
            String dataInicio = txtDataInicio.getText();
            String dataFim = txtDataFim.getText();
            String modalidade = comboModalidade.getValue();

            if (nome.isBlank() || dataInicio.isBlank() || dataFim.isBlank() || modalidade == null) {
                lblStatus.setText("Preencha todos os campos.");
                return;
            }

            String json = String.format(
                "{\"nome\":\"%s\",\"dataInicio\":\"%s\",\"dataFim\":\"%s\",\"modalidade\":\"%s\"}",
                nome, dataInicio, dataFim, modalidade
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/campeonatos/" + campeonato.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(status -> Platform.runLater(() -> {
                    if (status == 200) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Campeonato atualizado com sucesso!");
                        alert.showAndWait();
                        new CampeonatoListView().show(stage);
                    } else {
                        lblStatus.setText("Erro ao atualizar campeonato: " + status);
                    }
                }));
        });

        btnCancelar.setOnAction(e -> new CampeonatoListView().show(stage));

        grid.add(lblNome, 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(lblDataInicio, 0, 1);
        grid.add(txtDataInicio, 1, 1);
        grid.add(lblDataFim, 0, 2);
        grid.add(txtDataFim, 1, 2);
        grid.add(lblModalidade, 0, 3);
        grid.add(comboModalidade, 1, 3);

        HBox hBoxBotoes = new HBox(20, btnAtualizar, btnCancelar);
        hBoxBotoes.setAlignment(Pos.CENTER_RIGHT);
        grid.add(hBoxBotoes, 1, 4);

        grid.add(lblStatus, 1, 5);

        stage.setScene(new Scene(grid, 450, 300));
    }
}