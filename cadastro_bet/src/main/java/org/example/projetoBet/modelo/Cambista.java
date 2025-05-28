package org.example.projetoBet.modelo;

import org.example.projetoBet.Infra.Situacao;
import jakarta.persistence.*;

@Entity
@Table(name = "cambistas")
public class Cambista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "gerente_id", referencedColumnName = "id", nullable = false)
    private Gerente gerente;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private Situacao situacao;

    private String telefone;
    private String login;
    private int cep;
    private String endereco;
    private int numero;
    private String observacao;

    public Cambista(){

    }

    public Cambista(String nome,Gerente gerente ,String cpf, Situacao situacao, String telefone, String login, int cep, String endereco, int numero, String observacao) {
        this.nome = nome;
        this.gerente = gerente;
        this.cpf = cpf;
        this.situacao = situacao;
        this.telefone = telefone;
        this.login = login;
        this.cep = cep;
        this.endereco = endereco;
        this.numero = numero;
        this.observacao = observacao;
        gerente.getCambistas().add(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Gerente getGerente() {
        return gerente;
    }

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String toString(){
        return this.nome;
    }
}
