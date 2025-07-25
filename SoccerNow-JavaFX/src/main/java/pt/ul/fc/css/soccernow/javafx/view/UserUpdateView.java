package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.javafx.model.Jogador;
import pt.ul.fc.css.soccernow.javafx.model.Arbitro;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserUpdateView {
    public void show(Stage stage, Jogador jogador) {
        stage.setTitle("Atualizar Jogador");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField(jogador.getUsername());

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(jogador.getEmail());

        Label lblPosicao = new Label("Posição:");
        TextField txtPosicao = new TextField(jogador.getPosicao());

        Label lblNumero = new Label("Número Camisa:");
        TextField txtNumero = new TextField(String.valueOf(jogador.getNumeroCamisa()));

        Button btnAtualizar = new Button("Atualizar");
        Button btnCancelar = new Button("Voltar");
        Label lblStatus = new Label();

        btnAtualizar.setOnAction(e -> {
            String username = txtUsername.getText();
            String email = txtEmail.getText();
            String posicao = txtPosicao.getText();
            String numeroCamisa = txtNumero.getText();

            if (username.isEmpty() || email.isEmpty() || posicao.isEmpty() || numeroCamisa.isEmpty()) {
                lblStatus.setText("Todos os campos são obrigatórios!");
                return;
            }

            String json = String.format(
                "{\"username\":\"%s\",\"email\":\"%s\",\"posicao\":\"%s\",\"numeroCamisa\":%s}",
                username, email, posicao, numeroCamisa
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/jogadores/" + jogador.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(status -> Platform.runLater(() -> {
                    if (status == 200) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Jogador atualizado com sucesso!");
                        alert.showAndWait();
                        voltarParaLista(stage);
                    } else if (status == 400 || status == 409 || status == 500) {
                        lblStatus.setText("Erro ao atualizar: dados duplicados ou inválidos.");
                    } else {
                        lblStatus.setText("Erro ao atualizar: " + status);
                    }
                }));
        });

        btnCancelar.setOnAction(e -> voltarParaLista(stage));

        grid.add(lblUsername, 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(lblEmail, 0, 1);
        grid.add(txtEmail, 1, 1);
        grid.add(lblPosicao, 0, 2);
        grid.add(txtPosicao, 1, 2);
        grid.add(lblNumero, 0, 3);
        grid.add(txtNumero, 1, 3);

        HBox hBoxButtons = new HBox(20, btnAtualizar, btnCancelar);
        hBoxButtons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(hBoxButtons, 1, 4);

        grid.add(lblStatus, 1, 5);

        BorderPane root = new BorderPane();
        root.setCenter(grid);

        stage.setScene(new Scene(root, 420, 260));
    }

    public void show(Stage stage, Arbitro arbitro) {
        stage.setTitle("Atualizar Árbitro");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField(arbitro.getUsername());

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(arbitro.getEmail());

        Label lblCertificado = new Label("Certificado:");
        CheckBox chkCertificado = new CheckBox();
        chkCertificado.setSelected(arbitro.isCertificado());

        Button btnAtualizar = new Button("Atualizar");
        Button btnCancelar = new Button("Voltar");
        Label lblStatus = new Label();

        btnAtualizar.setOnAction(e -> {
            String username = txtUsername.getText();
            String email = txtEmail.getText();
            boolean certificado = chkCertificado.isSelected();

            if (username.isEmpty() || email.isEmpty()) {
                lblStatus.setText("Os campos username e email são obrigatórios!");
                return;
            }

            String json = String.format(
                "{\"username\":\"%s\",\"email\":\"%s\",\"certificado\":%s}",
                username, email, certificado
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/arbitros/" + arbitro.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(status -> Platform.runLater(() -> {
                    if (status == 200) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Árbitro atualizado com sucesso!");
                        alert.showAndWait();
                        voltarParaLista(stage);
                    } else if (status == 400 || status == 409 || status == 500) {
                        lblStatus.setText("Erro ao atualizar: dados duplicados ou inválidos.");
                    } else {
                        lblStatus.setText("Erro ao atualizar: " + status);
                    }
                }));
        });

        btnCancelar.setOnAction(e -> voltarParaLista(stage));

        grid.add(lblUsername, 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(lblEmail, 0, 1);
        grid.add(txtEmail, 1, 1);
        grid.add(lblCertificado, 0, 2);
        grid.add(chkCertificado, 1, 2);

        HBox hBoxButtons = new HBox(20, btnAtualizar, btnCancelar);
        hBoxButtons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(hBoxButtons, 1, 3);

        grid.add(lblStatus, 1, 4);

        BorderPane root = new BorderPane();
        root.setCenter(grid);

        stage.setScene(new Scene(root, 420, 220));
    }

    private void voltarParaLista(Stage stage) {
        new UserListView().show(stage);
    }
}