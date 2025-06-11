package service;

import dao.ReceitaDAO;
import model.Receita;
import java.util.List;

public class ReceitaService {
    private ReceitaDAO receitaDAO = new ReceitaDAO();

    public boolean criarReceita(Receita receita) {
        return receitaDAO.inserir(receita);
    }

    public List<Receita> listarReceitas() {
        return receitaDAO.listar();
    }

    public Receita buscarReceitaPorId(int id) {
        return receitaDAO.buscarPorId(id);
    }

    public boolean atualizarReceita(Receita receita) {
        return receitaDAO.atualizar(receita);
    }

    public boolean deletarReceita(int id) {
        return receitaDAO.deletar(id);
    }
}
