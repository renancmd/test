package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Ingrediente;

public class IngredienteDAO extends DAO {

    public boolean insert(Ingrediente ingrediente) {
        try {
            connect();
            String sql = "INSERT INTO ingrediente (nome, categoria, valor_nutricional) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, ingrediente.getName());
            stmt.setString(2, ingrediente.getCategory());
            stmt.setString(3, ingrediente.getNutritionalValue());

            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            disconnect();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Ingrediente> getAll() {
        List<Ingrediente> ingredientes = new ArrayList<>();

        try {
            connect();
            String sql = "SELECT * FROM ingrediente";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("nome");
                String category = rs.getString("categoria");
                String nutritionalValue = rs.getString("valor_nutricional");

                Ingrediente ingrediente = new Ingrediente(name, category, nutritionalValue);
                ingredientes.add(ingrediente);
            }

            rs.close();
            stmt.close();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredientes;
    }

    public boolean update(String oldName, Ingrediente ingrediente) {
        try {
            connect();
            String sql = "UPDATE ingrediente SET nome = ?, categoria = ?, valor_nutricional = ? WHERE nome = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, ingrediente.getName());
            stmt.setString(2, ingrediente.getCategory());
            stmt.setString(3, ingrediente.getNutritionalValue());
            stmt.setString(4, oldName);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            disconnect();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String name) {
        try {
            connect();
            String sql = "DELETE FROM ingrediente WHERE nome = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            disconnect();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
