package org.example.projetoBet.layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.projetoBet.Infra.DAO;
import org.example.projetoBet.Infra.Situacao;
import org.example.projetoBet.modelo.Gerente;
import org.example.projetoBet.modelo.Supervisor;

import java.util.ArrayList;
import java.util.List;

public class TelaPesquisar {

    private final List<RadioButton> radioButtons = new ArrayList<>();
    private final ToggleGroup grupoColunas = new ToggleGroup();
    private final ComboBox<Object> comboValores = new ComboBox<>();
    private final Button botaoFiltrar = new Button("Filtrar");
    private final Class<?> classe = LayoutPrincipal.getClasse();
    private final VBox listaColunas = new VBox(5);

    public void exibir(List<String> colunas) {
        Stage janela = new Stage();
        janela.initModality(Modality.APPLICATION_MODAL);
        janela.setTitle("Pesquisar");

        VBox layoutPrincipal = new VBox(10);
        layoutPrincipal.setPadding(new Insets(10));

        listaColunas.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        listaColunas.setPadding(new Insets(10));
        listaColunas.getChildren().add(new Label("Escolha uma coluna para aplicar o filtro"));

        setRadioButtons(colunas);

        HBox areaValor = new HBox(5);
        areaValor.getChildren().addAll(new Label("Valor:"),  comboValores);

        toggleListener();
        setOnActionFiltrar(janela);

        layoutPrincipal.getChildren().addAll(listaColunas, areaValor, botaoFiltrar);
        layoutPrincipal.setAlignment(Pos.CENTER);

        Scene cena = new Scene(layoutPrincipal);
        janela.setScene(cena);
        janela.showAndWait();
    }

    private void setRadioButtons(List<String> colunas){
        final List<String> COLUNAS_VALIDAS = List.of("id", "nome", "cpf", "login", "supervisor", "gerente", "situacao");
        for (String nomeColuna : colunas){
            if (COLUNAS_VALIDAS.contains(nomeColuna.toLowerCase())) {
                RadioButton radio = new RadioButton(nomeColuna);
                radio.setToggleGroup(grupoColunas);
                listaColunas.getChildren().add(radio);
                radioButtons.add(radio);
            }
        }
    }

    private void toggleListener(){
        grupoColunas.selectedToggleProperty().addListener((obs ,antigo, novo) -> {
            if (novo instanceof RadioButton radioSelecionado){
                String coluna = radioSelecionado.getText().toLowerCase();
                comboValores.getItems().clear();

                try {
                    if (coluna.equalsIgnoreCase("situacao")){
                        comboValores.getItems().addAll(Situacao.values());
                    } else if (coluna.equalsIgnoreCase("gerente")) {
                        List<Gerente> gerentes = DAO.getInstance(Gerente.class).findAll();
                        comboValores.getItems().addAll(gerentes);
                    } else if (coluna.equalsIgnoreCase("supervisor")) {
                        List<Supervisor> supervisores = DAO.getInstance(Supervisor.class).findAll();
                        comboValores.getItems().addAll(supervisores);
                    } else {
                        List<Object> valores = DAO.getInstance(classe).findDistinctValues(coluna);
                        comboValores.getItems().addAll(valores);
                    }
                } catch (Exception e) {
                    mostrarErro("Erro ao carregar valores: " + e.getMessage());
                }

            }
        });
    }

    private void setOnActionFiltrar(Stage janela){
        botaoFiltrar.setOnAction(e -> {
            String colunaSelecionada = getColunaSelecionada();
            Object valorSelecionado = comboValores.getValue();

            if(colunaSelecionada == null){
                mostrarAlerta("Selecione uma coluna para aplicar o filtro!");
                return;
            }
            if (valorSelecionado == null){
                mostrarAlerta("Selecione um valor para filtrar");
                return;
            }

            try {
                List<?> dadosFiltrados = DAO.getInstance(classe).findByEquals(colunaSelecionada.toLowerCase(), valorSelecionado);
                LayoutPrincipal.filtrarTabela(classe, dadosFiltrados);
                janela.close();
            } catch (Exception ex) {
                mostrarErro("Erro ao aplicar Filtro: " + ex.getMessage());
            }

        });
    }

    private String getColunaSelecionada(){
        for (RadioButton rb : radioButtons){
            if (rb.isSelected()){
                return rb.getText();
            }
        }
        return null;
    }

    private void mostrarAlerta(String mensagem){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String mensagem){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}