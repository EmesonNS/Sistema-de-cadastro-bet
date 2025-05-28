package org.example.projetoBet.layout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.projetoBet.Infra.DAO;
import org.example.projetoBet.modelo.Gerente;
import org.example.projetoBet.modelo.Supervisor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaAddFactory {

    public static <E> void criarTelaAdd(Class<E> classe) {
        Stage stage = new Stage();
        VBox layoutPrincipal = new VBox(5);
        Label titulo = new Label("Preencha o formulário");
        titulo.setStyle("-fx-font-size: 16px;");

        GridPane formulario = new GridPane();
        formulario.setPadding(new Insets(20));
        formulario.setHgap(10);
        formulario.setVgap(10);

        Map<String, Control> campos = new HashMap<>();

        int linha = 0;
        int coluna = 0;

        for (Field field : classe.getDeclaredFields()) {
            field.setAccessible(true);

                if (field.getName().equals("id") || field.getType() == List.class) continue;

                Label rotulo = new Label(field.getName());
                Control campoInput = criarCampoInput(field);

                if (linha > 5){
                    linha = 0;
                    coluna += 2;
                }

                campos.put(field.getName(), campoInput);
                formulario.add(rotulo, coluna, linha);
                formulario.add(campoInput,coluna + 1, linha);
                linha++;
        }

        Button botaoSalvar = criarBotaoSalvar(classe, campos);

        layoutPrincipal.getChildren().addAll(titulo, formulario, botaoSalvar);
        layoutPrincipal.setAlignment(Pos.CENTER);
        layoutPrincipal.setPadding(new Insets(10));
        stage.setScene(new Scene(layoutPrincipal));
        stage.setTitle("Adicionar " + classe.getSimpleName());
        stage.showAndWait();
    }

    private static Control criarCampoInput(Field field) {
        Class<?> tipo = field.getType();

        if (tipo.isEnum()){
            ComboBox<Object> comboEnum = new ComboBox<>();
            comboEnum.getItems().addAll(tipo.getEnumConstants());
            comboEnum.setPromptText("Selecione a Situação");
            return comboEnum;
        }

        if (tipo == Supervisor.class){
            ObservableList<Supervisor> supervisores = FXCollections.observableArrayList(
                    DAO.getInstance(Supervisor.class).findAll()
            );
            ComboBox<Supervisor> combo = new ComboBox<>(supervisores);
            combo.setPromptText("Selecione o Supervisor");
            return combo;
        }

        if (tipo == Gerente.class){
            ObservableList<Gerente> gerentes = FXCollections.observableArrayList(
                    DAO.getInstance(Gerente.class).findAll()
            );
            ComboBox<Gerente> combo = new ComboBox<>(gerentes);
            combo.setPromptText("Selecione o Gerente");
            return combo;
        }

        return new TextField();
    }

    private static <E> Button criarBotaoSalvar(Class<E> classe, Map<String, Control> campos) {
        Button botaoSalvar = new Button("Salvar");

        botaoSalvar.setOnAction(e->{
            if (!validarCampos(campos)) return;

            try {
                E instancia = classe.getDeclaredConstructor().newInstance();

                for (Map.Entry<String, Control> entrada : campos.entrySet()){
                    Field field = classe.getDeclaredField(entrada.getKey());
                    field.setAccessible(true);

                    Control controle = entrada.getValue();

                    if (controle instanceof TextField tf) {
                        if (field.getType() == int.class) {
                            field.set(instancia, Integer.parseInt(tf.getText()));
                        } else {
                            field.set(instancia, tf.getText());
                        }
                    } else if (controle instanceof ComboBox<?> cb) {
                        field.set(instancia, cb.getValue());
                    }
                }

                DAO.getInstance(classe).persist(instancia);
                LayoutPrincipal.criarTabela(LayoutPrincipal.getClasse());
                mostrarConfirmacao();
                limparCampos(campos);
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarErro("Erro ao salvar: " + ex.getMessage());
            }
        });

        return botaoSalvar;
    }

    private static void limparCampos(Map<String, Control> campos) {
        for (Control controle : campos.values()){
            controle.setStyle("");

            if (controle instanceof TextField tf){
                tf.clear();
            } else if (controle instanceof ComboBox<?> cb) {
                cb.setValue(null);
            }
        }
    }

    private static boolean validarCampos(Map<String, Control> campos) {
        boolean valido = true;

        for (Map.Entry<String, Control> entrada : campos.entrySet()){
            String nomeCampo = entrada.getKey();
            Control controle = entrada.getValue();
            controle.setStyle("");

            if (controle instanceof TextField tf){
                if (tf.getText().isEmpty() && !nomeCampo.equalsIgnoreCase("observacao")){
                    tf.setStyle("-fx-border-color: red;");
                    valido = false;
                }
            } else if (controle instanceof ComboBox<?> cb) {
                if (cb.getValue() == null){
                    cb.setStyle("-fx-border-color: red;");
                    valido = false;
                }
            }
        }
        if (!valido){
            mostrarAlerta();
        }
        return valido;
    }

    private static void mostrarAlerta(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText("Preencha todos os campos obrigatórios");
        alert.showAndWait();
    }

    private static void mostrarConfirmacao(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Registro Salvo com Sucesso!");
        alert.showAndWait();
    }

    private static void mostrarErro(String mensagem){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Falha ao salvar");
        alert.setContentText("Erro: " + mensagem);
        alert.showAndWait();
    }
}

