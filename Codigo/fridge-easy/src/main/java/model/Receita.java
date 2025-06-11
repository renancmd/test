package model;

public class Receita {
    private int id;
    private String nome;
    private String descricao;
    private boolean favorito;
    private String imagem;
    private int tempoPreparo;
    private String nivelDificuldade;
    private String filtro;

    public Receita() {}

    public Receita(int id, String nome, String descricao, boolean favorito, String imagem, int tempoPreparo, String nivelDificuldade, String filtro) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.favorito = favorito;
        this.imagem = imagem;
        this.tempoPreparo = tempoPreparo;
        this.nivelDificuldade = nivelDificuldade;
        this.filtro = filtro;
    }

    public Receita(String nome, String descricao, boolean favorito, String imagem, int tempoPreparo, String nivelDificuldade, String filtro) {
        this.nome = nome;
        this.descricao = descricao;
        this.favorito = favorito;
        this.imagem = imagem;
        this.tempoPreparo = tempoPreparo;
        this.nivelDificuldade = nivelDificuldade;
        this.filtro = filtro;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }
    public int getTempoPreparo() { return tempoPreparo; }
    public void setTempoPreparo(int tempoPreparo) { this.tempoPreparo = tempoPreparo; }
    public String getNivelDificuldade() { return nivelDificuldade; }
    public void setNivelDificuldade(String nivelDificuldade) { this.nivelDificuldade = nivelDificuldade; }
    public String getFiltro() { return filtro; }
    public void setFiltro(String filtro) { this.filtro = filtro; }
}
