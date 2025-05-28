package org.example.projetoBet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projetoBet.Infra.DAO;
import org.example.projetoBet.layout.LayoutPrincipal;

import java.util.Objects;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        LayoutPrincipal tela = new LayoutPrincipal();
        Scene scene = new Scene(tela.getRoot(), 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

        stage.setTitle("Sistema de Cadastro");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        DAO.closeEntityManager();
    }
}
