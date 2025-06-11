package service;

import java.util.List;

import dao.IngredienteDAO;
import model.Ingrediente;

public class IngredienteService {

    public boolean createIngrediente(String name, String category, String calories, String weight) {
        String nutritionalValue = calories + ", " + weight;
        Ingrediente ingrediente = new Ingrediente(name, category, nutritionalValue);
        IngredienteDAO dao = new IngredienteDAO();
        return dao.insert(ingrediente);
    }

    public boolean updateIngrediente(String oldName, String name, String category, String calories, String weight) {
        String nutritionalValue = calories + ", " + weight;
        Ingrediente ingrediente = new Ingrediente(name, category, nutritionalValue);
        IngredienteDAO dao = new IngredienteDAO();
        return dao.update(oldName, ingrediente);
    }

    public boolean deleteIngrediente(String name) {
        IngredienteDAO dao = new IngredienteDAO();
        return dao.delete(name);
    }

    public List<Ingrediente> listarIngredientes() {
        IngredienteDAO dao = new IngredienteDAO();
        return dao.getAll();
    }
}
