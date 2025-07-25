package pt.ul.fc.css.soccernow.javafx.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.javafx.Main;
import pt.ul.fc.css.soccernow.javafx.model.Arbitro;
import pt.ul.fc.css.soccernow.javafx.model.Campeonato;
import pt.ul.fc.css.soccernow.javafx.model.Equipa;
import pt.ul.fc.css.soccernow.javafx.model.Jogador;
import org.controlsfx.control.CheckComboBox;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class JogoRegisterView {
    private final ObservableList<Equipa> equipas = FXCollections.observableArrayList();
    private final ObservableList<Jogador> jogadoresCasa = FXCollections.observableArrayList();
    private final ObservableList<Jogador> jogadoresVisitante = FXCollections.observableArrayList();
    private final ObservableList<Arbitro> arbitros = FXCollections.observableArrayList();
    private final ObservableList<Campeonato> campeonatos = FXCollections.observableArrayList();

    public void show(Stage stage) {
        stage.setTitle("Criar Jogo");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(16));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField txtDataHora = new TextField();
        txtDataHora.setPromptText("AAAA-MM-DD HH:mm");

        TextField txtLocal = new TextField();

        ComboBox<Equipa> comboCasa = new ComboBox<>(equipas);
        ComboBox<Equipa> comboFora = new ComboBox<>(equipas);

        CheckComboBox<Jogador> checkComboCasa = new CheckComboBox<>(jogadoresCasa);
        CheckComboBox<Jogador> checkComboFora = new CheckComboBox<>(jogadoresVisitante);

        ComboBox<Jogador> comboGuardaRedesCasa = new ComboBox<>();
        comboGuardaRedesCasa.setPromptText("Guarda-redes Casa");

        ComboBox<Jogador> comboGuardaRedesFora = new ComboBox<>();
        comboGuardaRedesFora.setPromptText("Guarda-redes Fora");

        ComboBox<Arbitro> comboArbitroPrincipal = new ComboBox<>(arbitros);
        comboArbitroPrincipal.setPromptText("Árbitro Principal");

        CheckComboBox<Arbitro> checkComboAuxiliares = new CheckComboBox<>(arbitros);

        // Checkbox para jogo amigável
        CheckBox chkAmigavel = new CheckBox("Jogo amigável");
        chkAmigavel.setSelected(true);

        ComboBox<Campeonato> comboCampeonato = new ComboBox<>(campeonatos);
        comboCampeonato.setPromptText("Campeonato");
        comboCampeonato.setDisable(chkAmigavel.isSelected());
        chkAmigavel.selectedProperty().addListener((obs, old, val) -> {
            comboCampeonato.setDisable(val);
        });

        Label lblStatus = new Label();
        Button btnCriar = new Button("Registar Jogo");
        Button btnCancelar = new Button("Cancelar");

        comboCasa.setOnAction(e -> {
            Equipa equipa = comboCasa.getValue();
            jogadoresCasa.clear();
            if (equipa != null) {
                buscarJogadoresDaEquipa(equipa.getId(), jogadoresCasa);
            }
        });
        comboFora.setOnAction(e -> {
            Equipa equipa = comboFora.getValue();
            jogadoresVisitante.clear();
            if (equipa != null) {
                buscarJogadoresDaEquipa(equipa.getId(), jogadoresVisitante);
            }
        });

        checkComboCasa.getCheckModel().getCheckedItems().addListener((ListChangeListener<Jogador>) c -> {
            comboGuardaRedesCasa.getItems().setAll(checkComboCasa.getCheckModel().getCheckedItems());
        });
        checkComboFora.getCheckModel().getCheckedItems().addListener((ListChangeListener<Jogador>) c -> {
            comboGuardaRedesFora.getItems().setAll(checkComboFora.getCheckModel().getCheckedItems());
        });

        grid.add(new Label("Data/Hora (AAAA-MM-DD HH:mm):"), 0, 0);
        grid.add(txtDataHora, 1, 0);
        grid.add(new Label("Local:"), 0, 1);
        grid.add(txtLocal, 1, 1);
        grid.add(new Label("Equipa Casa:"), 0, 2);
        grid.add(comboCasa, 1, 2);
        grid.add(new Label("Jogadores Casa (5):"), 0, 3);
        grid.add(checkComboCasa, 1, 3);
        grid.add(new Label("Guarda-redes Casa:"), 0, 4);
        grid.add(comboGuardaRedesCasa, 1, 4);

        grid.add(new Label("Equipa Fora:"), 0, 5);
        grid.add(comboFora, 1, 5);
        grid.add(new Label("Jogadores Fora (5):"), 0, 6);
        grid.add(checkComboFora, 1, 6);
        grid.add(new Label("Guarda-redes Fora:"), 0, 7);
        grid.add(comboGuardaRedesFora, 1, 7);

        grid.add(new Label("Árbitro Principal (certificado):"), 0, 8);
        grid.add(comboArbitroPrincipal, 1, 8);
        grid.add(new Label("Árbitros Auxiliares:"), 0, 9);
        grid.add(checkComboAuxiliares, 1, 9);

        grid.add(chkAmigavel, 1, 10);
        grid.add(new Label("Campeonato:"), 0, 11);
        grid.add(comboCampeonato, 1, 11);

        HBox hBoxButtons = new HBox(20, btnCriar, btnCancelar);
        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        grid.add(hBoxButtons, 1, 12);
        grid.add(lblStatus, 1, 13);

        btnCancelar.setOnAction(e -> pt.ul.fc.css.soccernow.javafx.Main.showMenu(stage));

        btnCriar.setOnAction(e -> {
            String dataHora = txtDataHora.getText();
            String local = txtLocal.getText();
            Equipa equipaCasa = comboCasa.getValue();
            Equipa equipaFora = comboFora.getValue();
            ObservableList<Jogador> selCasa = checkComboCasa.getCheckModel().getCheckedItems();
            ObservableList<Jogador> selFora = checkComboFora.getCheckModel().getCheckedItems();
            Jogador guardaRedesCasa = comboGuardaRedesCasa.getValue();
            Jogador guardaRedesFora = comboGuardaRedesFora.getValue();
            Arbitro arbitroPrincipal = comboArbitroPrincipal.getValue();
            ObservableList<Arbitro> auxiliares = checkComboAuxiliares.getCheckModel().getCheckedItems();
            boolean principalCertificado = arbitroPrincipal != null && arbitroPrincipal.isCertificado();
            boolean algumAuxiliarCertificado = auxiliares.stream().anyMatch(Arbitro::isCertificado);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            try {
                LocalDateTime.parse(dataHora, formatter);
            } catch (DateTimeParseException ex) {
                lblStatus.setText("Data/hora inválida. Use o formato: AAAA-MM-DD HH:mm");
                return;
            }
            if (dataHora.isEmpty() || local.isEmpty() || equipaCasa == null || equipaFora == null
                    || selCasa.size() != 5 || selFora.size() != 5
                    || guardaRedesCasa == null || guardaRedesFora == null
                    || arbitroPrincipal == null) {
                lblStatus.setText("Preencha todos os campos obrigatórios e selecione 5 jogadores por equipa, um guarda-redes e um árbitro principal.");
                return;
            }
            if (!selCasa.contains(guardaRedesCasa) || !selFora.contains(guardaRedesFora)) {
                lblStatus.setText("O guarda-redes deve estar entre os 5 jogadores selecionados.");
                return;
            }
            if (!chkAmigavel.isSelected() && comboCampeonato.getValue() == null) {
                lblStatus.setText("Selecione um campeonato para jogos não amigáveis.");
                return;
            }
            if (!principalCertificado && !algumAuxiliarCertificado) {
                lblStatus.setText("Pelo menos um árbitro (principal ou auxiliar) deve ser certificado.");
                return;
            }
            if (!algumAuxiliarCertificado && !principalCertificado) {
                lblStatus.setText("O árbitro principal deve ser certificado se nenhum auxiliar for certificado.");
                return;
            }

            List<Long> idsAuxiliares = auxiliares.stream()
                .filter(a -> a.getId() != arbitroPrincipal.getId())
                .map(Arbitro::getId)
                .toList();

            JSONObject json = new JSONObject();
            json.put("dataHora", dataHora);
            json.put("local", local);
            json.put("equipaCasaId", equipaCasa.getId());
            json.put("equipaVisitanteId", equipaFora.getId());
            json.put("jogadoresCasaIds", selCasa.stream().map(Jogador::getId).toArray());
            json.put("goleiroCasaId", guardaRedesCasa.getId());
            json.put("jogadoresVisitanteIds", selFora.stream().map(Jogador::getId).toArray());
            json.put("goleiroVisitanteId", guardaRedesFora.getId());
            json.put("arbitroPrincipalId", arbitroPrincipal.getId());
            json.put("arbitrosAuxiliaresIds", idsAuxiliares);
            json.put("jogoAmigavel", chkAmigavel.isSelected());
            if (!chkAmigavel.isSelected()) {
                Campeonato camp = comboCampeonato.getValue();
                if (camp != null) {
                    json.put("campeonatoId", camp.getId());
                }
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/jogos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .thenAccept(status -> Platform.runLater(() -> {
                        if (status == 201 || status == 200) {
                            lblStatus.setText("Jogo criado com sucesso!");
                            Main.showMenu(stage);
                        } else {
                            lblStatus.setText("Erro ao criar jogo: " + status);
                        }
                    }));
        });

        stage.setScene(new Scene(grid, 700, 800));
        buscarEquipas();
        buscarArbitros();
        buscarCampeonatos();
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
                if (!json.trim().startsWith("[")) {
                    return;
                }
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
            }));
    }

    private void buscarJogadoresDaEquipa(long equipaId, ObservableList<Jogador> lista) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/equipas/" + equipaId + "/jogadores"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> {
                    lista.clear();
                    if (!json.trim().startsWith("[")) {
                        return;
                    }
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        lista.add(new Jogador(
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
                arbitros.clear();
                if (!json.trim().startsWith("[")) {
                    return;
                }
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

    private void buscarCampeonatos() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/campeonatos"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(json -> Platform.runLater(() -> {
                campeonatos.clear();
                if (!json.trim().startsWith("[")) {
                    return;
                }
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    campeonatos.add(new Campeonato(
                        obj.getLong("id"),
                        obj.getString("nome"),
                        obj.isNull("dataInicio") ? null : LocalDate.parse(obj.getString("dataInicio")),
                        obj.isNull("dataFim") ? null : LocalDate.parse(obj.getString("dataFim")),
                        obj.getString("modalidade")
                    ));
                }
            }));
    }
}