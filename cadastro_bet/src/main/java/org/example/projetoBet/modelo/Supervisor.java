package org.example.projetoBet.modelo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "supervisores")
public class Supervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String telefone;
    private String login;

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL)
    private List<Gerente> gerentes = new ArrayList<>();

    public Supervisor(){

    }

    public Supervisor(String nome, String cpf, String telefone, String login) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.login = login;
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

    public List<Gerente> getGerentes() {
        return gerentes;
    }
    public void setGerentes(List<Gerente> gerentes) {
        this.gerentes = gerentes;
    }

    public String toString(){
        return this.nome;
    }
}
