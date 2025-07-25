package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.javafx.Main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CampeonatoRegisterView {

    public void show(Stage stage) {
        stage.setTitle("Criar Campeonato");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();

        Label lblDataInicio = new Label("Data Início (yyyy-MM-dd):");
        TextField txtDataInicio = new TextField();

        Label lblDataFim = new Label("Data Fim (yyyy-MM-dd):");
        TextField txtDataFim = new TextField();

        Label lblModalidade = new Label("Modalidade:");
        ComboBox<String> comboModalidade = new ComboBox<>();
        comboModalidade.getItems().addAll("PONTOS", "ELIMINATORIA");
        comboModalidade.setPromptText("Escolha...");

        Label lblStatus = new Label();

        Button btnCriar = new Button("Registar");
        Button btnCancelar = new Button("Cancelar");

        btnCriar.setOnAction(e -> {
            String nome = txtNome.getText();
            String dataInicio = txtDataInicio.getText();
            String dataFim = txtDataFim.getText();
            String modalidade = comboModalidade.getValue();

            // Validação dos campos obrigatórios
            if (nome.isBlank() || dataInicio.isBlank() || dataFim.isBlank() || modalidade == null) {
                lblStatus.setText("Preencha todos os campos.");
                return;
            }

            // Validação do formato das datas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                formatter.parse(dataInicio);
                formatter.parse(dataFim);
            } catch (DateTimeParseException ex) {
                lblStatus.setText("Datas no formato inválido (use yyyy-MM-dd).");
                return;
            }

            String json = String.format(
                "{\"nome\":\"%s\",\"dataInicio\":\"%s\",\"dataFim\":\"%s\",\"modalidade\":\"%s\"}",
                nome, dataInicio, dataFim, modalidade
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/campeonatos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(status -> Platform.runLater(() -> {
                    if (status == 201) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Campeonato criado com sucesso!");
                        alert.showAndWait();
                        Main.showMenu(stage);
                    } else {
                        lblStatus.setText("Erro ao criar campeonato: " + status);
                    }
                }));
        });

        btnCancelar.setOnAction(e -> Main.showMenu(stage));

        grid.add(lblNome, 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(lblDataInicio, 0, 1);
        grid.add(txtDataInicio, 1, 1);
        grid.add(lblDataFim, 0, 2);
        grid.add(txtDataFim, 1, 2);
        grid.add(lblModalidade, 0, 3);
        grid.add(comboModalidade, 1, 3);

        HBox hBoxBotoes = new HBox(20, btnCriar, btnCancelar);
        hBoxBotoes.setAlignment(Pos.CENTER_LEFT);
        hBoxBotoes.setPadding(new Insets(10, 0, 0, 0));
        grid.add(hBoxBotoes, 1, 4);

        grid.add(lblStatus, 1, 5);

        stage.setScene(new Scene(grid, 450, 300));
    }
}