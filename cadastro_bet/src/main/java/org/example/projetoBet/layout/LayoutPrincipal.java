package org.example.projetoBet.layout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import org.example.projetoBet.Infra.DAO;
import org.example.projetoBet.modelo.Cambista;
import org.example.projetoBet.modelo.Gerente;
import org.example.projetoBet.modelo.Supervisor;

import java.lang.reflect.Field;
import java.util.List;

public class LayoutPrincipal {

    private VBox root;
    private static TableView<?> tabela;
    private static Class<?> classe;
    private static final PseudoClass SELECIONADO = PseudoClass.getPseudoClass("selecionado");

    public LayoutPrincipal() {
        inicializarLayout();
    }

    private void inicializarLayout() {
        VBox cabecalho = criarCabecalho();
        HBox botoesCadastro = criarBotoesCadastro();
        botoesCadastro.setAlignment(Pos.CENTER);
        VBox tabelaComFiltro = criarAreaTabela();

        root = new VBox(10, cabecalho, botoesCadastro, tabelaComFiltro);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("fundo");
    }

    private VBox criarCabecalho() {
        Label titulo = new Label("Sistema de Cadastro ");
        Label marca = new Label("Betão.Bet");
        titulo.getStyleClass().add("titulo");
        marca.getStyleClass().add("marca");

        HBox cabecalho = new HBox(5, titulo, marca);
        cabecalho.setAlignment(Pos.CENTER);
        return new VBox(cabecalho);
    }

    private HBox criarBotoesCadastro() {
        Button botaoAdicionarSupervisor = criarBotao("Adicionar Supervisor", () -> TelaAddFactory.criarTelaAdd(Supervisor.class));
        Button botaoAdicionarGerente = criarBotao("Adicionar Gerente", () -> TelaAddFactory.criarTelaAdd(Gerente.class));
        Button botaoAdicionarCambista = criarBotao("Adicionar Cambista", () -> TelaAddFactory.criarTelaAdd(Cambista.class));

        return new HBox(20, botaoAdicionarSupervisor, botaoAdicionarGerente, botaoAdicionarCambista);
    }

    private Button criarBotao(String texto, Runnable acao) {
        Button botao = new Button(texto);
        botao.getStyleClass().add("botao");
        botao.setOnAction(e-> acao.run());

        return botao;
    }

    private VBox criarAreaTabela() {
        Button botaoFiltroSupervisor = new Button("Supervisor");
        Button botaoFiltroGerente = new Button("Gerente");
        Button botaoFiltroCambista = new Button("Cambista");

        botaoFiltroCambista.pseudoClassStateChanged(SELECIONADO, true);
        configurarBotoesFiltro(
                new Button[]{botaoFiltroSupervisor, botaoFiltroGerente, botaoFiltroCambista},
                new Runnable[]{
                        () -> criarTabela(Supervisor.class),
                        () -> criarTabela(Gerente.class),
                        () -> criarTabela(Cambista.class)
                }
        );

        HBox filtros = new HBox(20, botaoFiltroSupervisor, botaoFiltroGerente, botaoFiltroCambista);
        filtros.setAlignment(Pos.CENTER);
        filtros.setPadding(new Insets(10));
        filtros.getStyleClass().add("filtros");

        tabela = new TableView<>();
        criarTabela(Cambista.class);

        HBox barraSuperior = new HBox(5, criarBotaoPesquisar(), criarBotaoExcluir());
        barraSuperior.setAlignment(Pos.CENTER_LEFT);

        VBox tabelaComPesquisa = new VBox(barraSuperior, tabela);
        VBox areaTabela = new VBox(filtros, tabelaComPesquisa);
        areaTabela.getStyleClass().add("tabela-area");

        return areaTabela;
    }

    private Button criarBotaoPesquisar() {
        Button botao = new Button("Pesquisar");
        botao.getStyleClass().add("botao-pesquisar");
        botao.setPrefWidth(100);
        botao.setOnAction(e -> {
            TelaPesquisar tela = new TelaPesquisar();
            List<String> colunas = tabela.getColumns().stream().map(TableColumnBase::getText).toList();
            tela.exibir(colunas);
        });
        return botao;
    }

    private Button criarBotaoExcluir() {
        Button botao = new Button("Excluir");
        botao.getStyleClass().add("botao-excluir");
        botao.setPrefWidth(100);
        botao.setOnAction(e -> {
            Object selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado == null){
                mostrarAlerta("Selecione uma linha para excluir!");
                return;
            }
            if (mostrarConfirmacao()){
                DAO.getInstance(classe).delete(selecionado);
                criarTabela(classe);
            }
        });
        return botao;
    }

    static <E> void criarTabela(Class<E> classe){
        LayoutPrincipal.classe = classe;
        configurarColunas(classe);
        preencherTabela(classe);
        tabela.setEditable(true);
    }

    private static  <E> void configurarColunas(Class<E> classe) {
        tabela.getColumns().clear();

        for (Field campo: classe.getDeclaredFields()) {
            String nomeDoCampo = campo.getName();
            Class<?> tipoCampo = campo.getType();
            TableColumn<E, Object> coluna = new TableColumn<>(capitalize(nomeDoCampo));
            coluna.setCellValueFactory(new PropertyValueFactory<>(nomeDoCampo));
            coluna.setPrefWidth(100);

            if (tipoCampo == Supervisor.class || tipoCampo == Gerente.class || tipoCampo == Cambista.class || tipoCampo.isEnum()) {
                configurarCellFactoryRelacionamento(coluna, tipoCampo);
            } else if (tipoCampo == String.class) {
                configurarCellFactoryString(coluna);
            } else if (tipoCampo == Integer.class || tipoCampo == int.class) {
                configurarCellFactoryInteger(coluna);
            }

            coluna.setOnEditCommit(e -> {
                E entidade = e.getRowValue();

                try {
                    Field field = entidade.getClass().getDeclaredField(nomeDoCampo);
                    field.setAccessible(true);
                    field.set(entidade, e.getNewValue());

                    DAO.getInstance(classe).merge(entidade);
                    preencherTabela(classe);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarAlerta("Erro ao atualizar: " + ex.getMessage());
                }
            });

            ((TableView<E>) tabela).getColumns().add(coluna);
        }
    }

    private static <E> void preencherTabela(Class<E> classe){
        ObservableList<E> dados = FXCollections.observableArrayList(DAO.getInstance(classe).findAll());
        ((TableView<E>) tabela).setItems(dados);
    }

    static <E> void filtrarTabela(Class<?> classe, List<E> valoresFiltrados){
        LayoutPrincipal.classe = classe;
        configurarColunas(classe);
        ((TableView<E>) tabela).setItems(FXCollections.observableArrayList(valoresFiltrados));
    }

    private void configurarBotoesFiltro(Button[] botoes, Runnable[] acoes) {
        for (int i = 0; i < botoes.length; i++) {
            final int index = i;
            Button botao = botoes[i];

            botao.getStyleClass().add("botao");

            botao.setOnAction(e -> {
                for (Button b : botoes) b.pseudoClassStateChanged(SELECIONADO, false);
                botao.pseudoClassStateChanged(SELECIONADO, true);
                acoes[index].run();
            });
        }
    }

    private static <E> void configurarCellFactoryString(TableColumn<E, Object> coluna) {
        coluna.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
                @Override
                public String toString(Object object) {
                    return object != null ? object.toString() : "";
                }
                @Override
                public Object fromString(String string) {
                    return string;
                }
            }));
    }

    private static <E> void configurarCellFactoryInteger(TableColumn<E, Object> coluna){
        coluna.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>(){
                @Override
                public String toString(Object o) {
                    return o != null ? o.toString() : "";
                }
                @Override
                public Object fromString(String s) {
                    try {
                        return Integer.parseInt(s);
                    } catch (Exception ex) {
                        return 0;
                    }
                }
            }));
    }

    private static <E> void configurarCellFactoryRelacionamento(TableColumn<E, Object> coluna, Class<?> tipoRelacionamento) {
        coluna.setCellFactory(col -> new TableCell<>() {
            final ComboBox<Object> comboBox = new ComboBox<>();
                {
                    if (tipoRelacionamento.isEnum()) {
                        comboBox.getItems().addAll(tipoRelacionamento.getEnumConstants());
                    } else {
                        comboBox.getItems().setAll(DAO.getInstance(tipoRelacionamento).findAll());
                    }
                    comboBox.setOnAction(e -> commitEdit(comboBox.getValue()));
                }

                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.toString());
                    setGraphic(null);
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    comboBox.setValue(getItem());
                    setGraphic(comboBox);
                    setText(null);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setText(getItem() == null ? null : getItem().toString());
                    setGraphic(null);
                }
        });
    }

    private static void mostrarAlerta(String mensagem){
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }

    private boolean mostrarConfirmacao(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir este registro");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public VBox getRoot() {
        return root;
    }

    public static Class<?> getClasse() {
        return classe;
    }

    private static String capitalize(String texto){
        return (Character.toUpperCase(texto.charAt(0)) + texto.substring(1));
    }
}
