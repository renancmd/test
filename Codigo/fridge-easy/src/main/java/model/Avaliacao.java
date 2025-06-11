package model;

public class Avaliacao {
    private int id;
    private int nota;
    private String comentario;
    private int idReceita;
    private int idUsuario;

    public Avaliacao(int id, int nota, String comentario, int idReceita, int idUsuario) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.idReceita = idReceita;
        this.idUsuario = idUsuario;
    }

    public Avaliacao(int nota, String comentario, int idReceita, int idUsuario) {
        this.nota = nota;
        this.comentario = comentario;
        this.idReceita = idReceita;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
