package org.example.projetoBet.Infra;

public enum Situacao {
    ATIVO("Ativo"), INATIVO("Inativo");

    private final String valor;

    Situacao(String valor){
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }

    public static Situacao fromValor(String valor){
        for (Situacao s : Situacao.values()){
            if (s.getValor().equalsIgnoreCase(valor)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Descrição Inválida: " + valor);
    }
}
