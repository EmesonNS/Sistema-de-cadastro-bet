package org.example.projetoBet.modelo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gerentes")
public class Gerente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "supervisor_id", referencedColumnName = "id", nullable = false)
    private Supervisor supervisor;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String telefone;
    private String login;
    private int cep;
    private String endereco;
    private int numero;

    @OneToMany(mappedBy = "gerente", cascade = CascadeType.ALL)
    private List<Cambista> cambistas = new ArrayList<>();

    public Gerente(){

    }

    public Gerente(String nome, Supervisor supervisor ,String cpf, String telefone, String login, int cep, String endereco, int numero) {
        this.nome = nome;
        this.supervisor = supervisor;
        this.cpf = cpf;
        this.telefone = telefone;
        this.login = login;
        this.cep = cep;
        this.endereco = endereco;
        this.numero = numero;
        supervisor.getGerentes().add(this);
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

    public Supervisor getSupervisor() {
        return supervisor;
    }
    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public List<Cambista> getCambistas() {
        return cambistas;
    }
    public void setCambistas(List<Cambista> cambistas) {
        this.cambistas = cambistas;
    }

    public String toString(){
        return this.nome;
    }
}
