package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.javafx.Main;

public class UserRegisterView {
    public void show(Stage stage) {
        stage.setTitle("Registo de Utilizador");

        // Formulário Jogador
        GridPane gridJogador = new GridPane();
        gridJogador.setPadding(new Insets(10));
        gridJogador.setVgap(8);
        gridJogador.setHgap(10);

        Label lblUsernameJog = new Label("Username:");
        TextField txtUsernameJog = new TextField();

        Label lblEmailJog = new Label("Email:");
        TextField txtEmailJog = new TextField();

        Label lblPasswordJog = new Label("Password:");
        PasswordField txtPasswordJog = new PasswordField();

        Label lblPosicao = new Label("Posição:");
        TextField txtPosicao = new TextField();

        Label lblNumero = new Label("Número Camisa:");
        TextField txtNumero = new TextField();

        Button btnRegistarJogador = new Button("Registar Jogador");
        Label lblStatusJog = new Label();

        Label lblTituloJogador = new Label("Jogador");
        lblTituloJogador.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        lblTituloJogador.setMaxWidth(Double.MAX_VALUE);
        lblTituloJogador.setAlignment(Pos.CENTER);

        gridJogador.add(lblTituloJogador, 0, 0, 2, 1);
        GridPane.setHalignment(lblTituloJogador, javafx.geometry.HPos.CENTER);

        
        gridJogador.add(lblUsernameJog, 0, 1);
        gridJogador.add(txtUsernameJog, 1, 1);
        gridJogador.add(lblEmailJog, 0, 2);
        gridJogador.add(txtEmailJog, 1, 2);
        gridJogador.add(lblPasswordJog, 0, 3);
        gridJogador.add(txtPasswordJog, 1, 3);
        gridJogador.add(lblPosicao, 0, 4);
        gridJogador.add(txtPosicao, 1, 4);
        gridJogador.add(lblNumero, 0, 5);
        gridJogador.add(txtNumero, 1, 5);
        gridJogador.add(btnRegistarJogador, 1, 6);
        gridJogador.add(lblStatusJog, 1, 7);

        // Formulário Árbitro
        GridPane gridArbitro = new GridPane();
        gridArbitro.setPadding(new Insets(10));
        gridArbitro.setVgap(8);
        gridArbitro.setHgap(10);

        Label lblUsernameArb = new Label("Username:");
        TextField txtUsernameArb = new TextField();

        Label lblEmailArb = new Label("Email:");
        TextField txtEmailArb = new TextField();

        Label lblPasswordArb = new Label("Password:");
        PasswordField txtPasswordArb = new PasswordField();

        CheckBox chkCertificado = new CheckBox("Certificado");

        Button btnRegistarArbitro = new Button("Registar Árbitro");
        Label lblStatusArb = new Label();

        Label lblTituloArbitro = new Label("Árbitro");
        lblTituloArbitro.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        gridArbitro.add(lblTituloArbitro, 0, 0, 2, 1);
        GridPane.setHalignment(lblTituloArbitro, javafx.geometry.HPos.CENTER);
        lblTituloArbitro.setMaxWidth(Double.MAX_VALUE);
        lblTituloArbitro.setAlignment(Pos.CENTER);
        gridArbitro.add(lblUsernameArb, 0, 1);
        gridArbitro.add(txtUsernameArb, 1, 1);
        gridArbitro.add(lblEmailArb, 0, 2);
        gridArbitro.add(txtEmailArb, 1, 2);
        gridArbitro.add(lblPasswordArb, 0, 3);
        gridArbitro.add(txtPasswordArb, 1, 3);
        gridArbitro.add(chkCertificado, 1, 4);
        gridArbitro.add(btnRegistarArbitro, 1, 5);
        gridArbitro.add(lblStatusArb, 1, 6);

        HBox hBox = new HBox(40, gridJogador, gridArbitro);
        hBox.setAlignment(Pos.CENTER);

        Button btnCancelar = new Button("Cancelar");
        HBox hBoxCancelar = new HBox(btnCancelar);
        hBoxCancelar.setAlignment(Pos.CENTER);
        hBoxCancelar.setPadding(new Insets(20, 0, 0, 0));

        VBox root = new VBox(20, hBox, hBoxCancelar);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        btnRegistarJogador.setOnAction(e -> {
            String username = txtUsernameJog.getText();
            String email = txtEmailJog.getText();
            String password = txtPasswordJog.getText();
            String posicao = txtPosicao.getText();
            String numeroCamisa = txtNumero.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || posicao.isEmpty() || numeroCamisa.isEmpty()) {
                lblStatusJog.setText("Todos os campos são obrigatórios!");
                return;
            }

            String json = String.format(
                "{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"posicao\":\"%s\",\"numeroCamisa\":%s}",
                username, email, password, posicao, numeroCamisa.isEmpty() ? "null" : numeroCamisa
            );

            var client = java.net.http.HttpClient.newHttpClient();
            var request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://localhost:8080/api/jogadores"))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                .thenApply(java.net.http.HttpResponse::statusCode)
                .thenAccept(status ->
                    Platform.runLater(() -> {
                        if (status == 200 || status == 201) {
                            Main.showMenu(stage);
                        } else {
                            lblStatusJog.setText("Erro ao registar: " + status);
                        }
                    })
                );
        });

        btnRegistarArbitro.setOnAction(e -> {
            String username = txtUsernameArb.getText();
            String email = txtEmailArb.getText();
            String password = txtPasswordArb.getText();
            boolean certificado = chkCertificado.isSelected();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                lblStatusArb.setText("Todos os campos são obrigatórios!");
                return;
            }

            String json = String.format(
                "{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"certificado\":%s}",
                username, email, password, certificado
            );

            var client = java.net.http.HttpClient.newHttpClient();
            var request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://localhost:8080/api/arbitros"))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                .thenApply(java.net.http.HttpResponse::statusCode)
                .thenAccept(status ->
                    Platform.runLater(() -> {
                        if (status == 200 || status == 201) {
                            Main.showMenu(stage);
                        } else {
                            lblStatusArb.setText("Erro ao registar: " + status);
                        }
                    })
                );
        });

        btnCancelar.setOnAction(e -> Main.showMenu(stage));

        stage.setScene(new Scene(root, 900, 400));
    }
}