package service;

import java.util.List;
import model.Avaliacao;
import dao.AvaliacaoDAO;

public class AvaliacaoService {
    private AvaliacaoDAO avaliacaoDAO;

    public AvaliacaoService() {
        this.avaliacaoDAO = new AvaliacaoDAO();
    }

    public boolean registrarAvaliacao(Avaliacao avaliacao) {
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) return false;
        return avaliacaoDAO.create(avaliacao);
    }

    public List<Avaliacao> listarAvaliacoes() {
        return avaliacaoDAO.readAll();
    }

    public Avaliacao buscarPorId(int id) {
        return avaliacaoDAO.readById(id);
    }

    public boolean atualizarAvaliacao(Avaliacao avaliacao) {
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) return false;
        return avaliacaoDAO.update(avaliacao);
    }

    public boolean removerAvaliacao(int id) {
        return avaliacaoDAO.delete(id);
    }
}
