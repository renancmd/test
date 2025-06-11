package model;

public class ReceitaIngrediente {
    private int idReceita;
    private int idIngrediente;
    private double quantidade;
    private String medida;

    public ReceitaIngrediente(int idReceita, int idIngrediente, double quantidade, String medida) {
        this.idReceita = idReceita;
        this.idIngrediente = idIngrediente;
        this.quantidade = quantidade;
        this.medida = medida;
    }

    public ReceitaIngrediente() {}

    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    @Override
    public String toString() {
        return "ReceitaIngrediente{" +
                "idReceita=" + idReceita +
                ", idIngrediente=" + idIngrediente +
                ", quantidade=" + quantidade +
                ", medida='" + medida + '\'' +
                '}';
    }
}

