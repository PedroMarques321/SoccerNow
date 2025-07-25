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
import pt.ul.fc.css.soccernow.javafx.Main;
import pt.ul.fc.css.soccernow.javafx.model.Jogador;
import pt.ul.fc.css.soccernow.javafx.model.Arbitro;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import org.json.*;

public class UserListView {
    private final ObservableList<Jogador> jogadores = FXCollections.observableArrayList();
    private final ObservableList<Arbitro> arbitros = FXCollections.observableArrayList();
    private static final Logger logger = Logger.getLogger(UserListView.class.getName());

    public void show(Stage stage) {
        TableView<Jogador> tableJogadores = new TableView<>(jogadores);

        TableColumn<Jogador, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> data.getValue().usernameProperty());

        TableColumn<Jogador, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<Jogador, String> posicaoCol = new TableColumn<>("Posição");
        posicaoCol.setCellValueFactory(data -> data.getValue().posicaoProperty());

        TableColumn<Jogador, Number> numeroCol = new TableColumn<>("Número Camisa");
        numeroCol.setCellValueFactory(data -> data.getValue().numeroCamisaProperty());

        tableJogadores.getColumns().addAll(usernameCol, emailCol, posicaoCol, numeroCol);

        TableView<Arbitro> tableArbitros = new TableView<>(arbitros);

        TableColumn<Arbitro, String> arbUsernameCol = new TableColumn<>("Username");
        arbUsernameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));

        TableColumn<Arbitro, String> arbEmailCol = new TableColumn<>("Email");
        arbEmailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Arbitro, String> arbCertificadoCol = new TableColumn<>("Certificado");
        arbCertificadoCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().isCertificado() ? "Sim" : "Não"));

        tableArbitros.getColumns().addAll(arbUsernameCol, arbEmailCol, arbCertificadoCol);

        Label lblJogadores = new Label("Jogadores");
        Label lblArbitros = new Label("Árbitros");

        Button btnAtualizarJogador = new Button("Atualizar Jogador");
        Button btnRemoverJogador = new Button("Remover Jogador");
        Button btnAtualizarArbitro = new Button("Atualizar Árbitro");
        Button btnRemoverArbitro = new Button("Remover Árbitro");
        Button btnVoltar = new Button("Voltar");

        btnAtualizarJogador.setOnAction(e -> {
            Jogador selected = tableJogadores.getSelectionModel().getSelectedItem();
            if (selected != null) {
                new UserUpdateView().show(stage, selected);
            }
        });

        btnRemoverJogador.setOnAction(e -> {
            Jogador selected = tableJogadores.getSelectionModel().getSelectedItem();
            if (selected != null) {
                removerJogador(selected.getId(), this::buscarJogadores);
            }
        });

        btnAtualizarArbitro.setOnAction(e -> {
            Arbitro selected = tableArbitros.getSelectionModel().getSelectedItem();
            if (selected != null) {
                new UserUpdateView().show(stage, selected);
            }
        });

        btnRemoverArbitro.setOnAction(e -> {
            Arbitro selected = tableArbitros.getSelectionModel().getSelectedItem();
            if (selected != null) {
                removerArbitro(selected.getId(), this::buscarArbitros);
            }
        });

        btnVoltar.setOnAction(e -> Main.showMenu(stage));

        HBox buttonsJogadores = new HBox(20, btnAtualizarJogador, btnRemoverJogador);
        buttonsJogadores.setPadding(new Insets(10, 0, 10, 0));
        buttonsJogadores.setAlignment(javafx.geometry.Pos.CENTER);

        HBox buttonsArbitros = new HBox(20, btnAtualizarArbitro, btnRemoverArbitro);
        buttonsArbitros.setPadding(new Insets(10, 0, 10, 0));
        buttonsArbitros.setAlignment(javafx.geometry.Pos.CENTER);

        VBox vboxJogadores = new VBox(8, lblJogadores, tableJogadores, buttonsJogadores);
        VBox vboxArbitros = new VBox(8, lblArbitros, tableArbitros, buttonsArbitros);

        VBox root = new VBox(20, vboxJogadores, vboxArbitros, btnVoltar);
        root.setPadding(new Insets(20, 20, 20, 20));

        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("Lista de Utilizadores");
        stage.show();

        buscarJogadores();
        buscarArbitros();
    }

    private void buscarJogadores() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/jogadores"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> {
                    logger.info("RESPOSTA DO BACKEND (Jogadores):");
                    logger.info(json);
                    jogadores.clear();
                    if (!json.trim().startsWith("[")) return;
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        jogadores.add(new Jogador(
                            obj.getLong("id"),
                            obj.getString("username"),
                            obj.getString("email"),
                            obj.getString("posicao"),
                            obj.optInt("numeroCamisa", 0)
                        ));
                    }
                }));
    }

    private void buscarArbitros() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/arbitros"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> {
                    logger.info("RESPOSTA DO BACKEND (Árbitros):");
                    logger.info(json);
                    arbitros.clear();
                    if (!json.trim().startsWith("[")) return;
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        arbitros.add(new Arbitro(
                            obj.getLong("id"),
                            obj.getString("username"),
                            obj.getString("email"),
                            obj.getBoolean("certificado")
                        ));
                    }
                }));
    }

    private void removerJogador(long id, Runnable onSuccess) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/jogadores/" + id))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> Platform.runLater(() -> {
                    logger.info("DELETE Jogador status: " + resp.statusCode());
                    if (resp.statusCode() == 200 || resp.statusCode() == 204) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Jogador removido com sucesso!");
                        alert.showAndWait();
                        onSuccess.run();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao remover: " + resp.statusCode());
                        alert.showAndWait();
                    }
                }));
    }

    private void removerArbitro(long id, Runnable onSuccess) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/arbitros/" + id))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> Platform.runLater(() -> {
                    logger.info("DELETE Árbitro status: " + resp.statusCode());
                    if (resp.statusCode() == 200 || resp.statusCode() == 204) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Árbitro removido com sucesso!");
                        alert.showAndWait();
                        onSuccess.run();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao remover: " + resp.statusCode());
                        alert.showAndWait();
                    }
                }));
    }
}