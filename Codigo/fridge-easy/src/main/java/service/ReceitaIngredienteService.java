package service;

import dao.ReceitaIngredienteDAO;
import model.ReceitaIngrediente;
import java.util.List;

public class ReceitaIngredienteService {
    private ReceitaIngredienteDAO dao = new ReceitaIngredienteDAO();

    public boolean criar(ReceitaIngrediente ri) {
        return dao.inserir(ri);
    }

    public List<ReceitaIngrediente> listarTodos() {
        return dao.listarTodos();
    }

    public ReceitaIngrediente buscarPorId(int idReceita, int idIngrediente) {
        return dao.buscarPorId(idReceita, idIngrediente);
    }

    public boolean atualizar(ReceitaIngrediente ri) {
        return dao.atualizar(ri);
    }

    public boolean deletar(int idReceita, int idIngrediente) {
        return dao.deletar(idReceita, idIngrediente);
    }
}
