package pt.ul.fc.css.soccernow.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.javafx.view.EquipaListView;
import pt.ul.fc.css.soccernow.javafx.view.EquipaRegisterView;
import pt.ul.fc.css.soccernow.javafx.view.JogoListView;
import pt.ul.fc.css.soccernow.javafx.view.JogoRegisterView;
import pt.ul.fc.css.soccernow.javafx.view.UserListView;
import pt.ul.fc.css.soccernow.javafx.view.UserRegisterView;

    public class Main extends Application {
        public static void showMenu(Stage stage) {
        Label titulo = new Label("SoccerNow");
        titulo.setFont(new Font("Arial Black", 32));
        titulo.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitulo = new Label("Gestão de Jogos de Futsal");
        subtitulo.setFont(new Font("Arial", 18));
        subtitulo.setStyle("-fx-text-fill: #34495e;");

        VBox vBoxTitulo = new VBox(4, titulo, subtitulo);
        vBoxTitulo.setAlignment(Pos.CENTER);

        Button btnRegistarJogador = new Button("Registar Utilizador");
        Button btnListarJogadores = new Button("Ver Utilizadores");
        Button btnRegistarEquipa = new Button("Registar Equipa");
        Button btnListarEquipas = new Button("Ver Equipas");
        Button btnCriarJogo = new Button("Registar Jogo");
        Button btnListarJogos = new Button("Ver Jogos");
        Button btnCriarCampeonato = new Button("Registar Campeonato");
        Button btnListarCampeonatos = new Button("Ver Campeonatos"); 

        Button btnLogout = new Button("Logout");

        for (Button btn : new Button[]{btnRegistarJogador, btnListarJogadores, btnRegistarEquipa, btnListarEquipas, btnCriarJogo, btnListarJogos, btnCriarCampeonato, btnListarCampeonatos, btnLogout}) {
            btn.setMinWidth(180);
            btn.setFont(Font.font(16));
        }

        btnRegistarJogador.setOnAction(e -> new UserRegisterView().show(stage));
        btnListarJogadores.setOnAction(e -> new UserListView().show(stage));
        btnRegistarEquipa.setOnAction(e -> new EquipaRegisterView().show(stage));
        btnListarEquipas.setOnAction(e -> new EquipaListView().show(stage));
        btnCriarJogo.setOnAction(e -> new JogoRegisterView().show(stage));
        btnListarJogos.setOnAction(e -> new JogoListView().show(stage));
        btnCriarCampeonato.setOnAction(e -> new pt.ul.fc.css.soccernow.javafx.view.CampeonatoRegisterView().show(stage));
        btnListarCampeonatos.setOnAction(e -> new pt.ul.fc.css.soccernow.javafx.view.CampeonatoListView().show(stage));

        btnLogout.setOnAction(e -> showLogin(stage));

        HBox hBoxJogador = new HBox(20, btnRegistarJogador, btnListarJogadores);
        hBoxJogador.setAlignment(Pos.CENTER);

        HBox hBoxEquipa = new HBox(20, btnRegistarEquipa, btnListarEquipas);
        hBoxEquipa.setAlignment(Pos.CENTER);

        HBox hBoxJogo = new HBox(20, btnCriarJogo, btnListarJogos);
        hBoxJogo.setAlignment(Pos.CENTER);

        HBox hBoxCampeonato = new HBox(20, btnCriarCampeonato, btnListarCampeonatos); 
        hBoxCampeonato.setAlignment(Pos.CENTER);

        Region espacoFinal = new Region();
        espacoFinal.setMinHeight(30);

        VBox root = new VBox(30, vBoxTitulo, hBoxJogador, hBoxEquipa, hBoxJogo, hBoxCampeonato, btnLogout, espacoFinal);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e0eafc, #cfdef3);");

        Scene scene = new Scene(root, 520, 500);

        stage.setTitle("SoccerNow - JavaFX");
        stage.setScene(scene);
    }  

public static void showLogin(Stage stage) {
        Label lblTitulo = new Label("Login SoccerNow");
        lblTitulo.setFont(new Font("Arial Black", 26));
        lblTitulo.setStyle("-fx-text-fill: #2c3e50;");

        TextField txtUser = new TextField();
        txtUser.setPromptText("Utilizador");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Palavra-passe");

        Label lblStatus = new Label();

        Button btnLogin = new Button("Entrar");
        btnLogin.setMinWidth(120);
        btnLogin.setFont(Font.font(16));
        btnLogin.setOnAction(e -> {
            String user = txtUser.getText();
            String pass = txtPass.getText();
            if (!user.isEmpty() && !pass.isEmpty()) {
                showMenu(stage);
            } else {
                lblStatus.setText("Preencha ambos os campos.");
            }
        });

        VBox root = new VBox(18, lblTitulo, txtUser, txtPass, btnLogin, lblStatus);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e0eafc, #cfdef3);");

        stage.setScene(new Scene(root, 380, 320));
    }

    @Override
    public void start(Stage primaryStage) {
        showLogin(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}