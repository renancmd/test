package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ReceitaIngrediente;

public class ReceitaIngredienteDAO {
    private Connection getConnection() throws SQLException {
        // Ajuste conforme sua configuração de conexão
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/fridgeeasy", "postgres", "postgres");
    }

    public boolean inserir(ReceitaIngrediente ri) {
        String sql = "INSERT INTO receita_ingrediente (id_receita, id_ingrediente, quantidade, medida) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ri.getIdReceita());
            stmt.setInt(2, ri.getIdIngrediente());
            stmt.setDouble(3, ri.getQuantidade());
            stmt.setString(4, ri.getMedida());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ReceitaIngrediente> listarTodos() {
        List<ReceitaIngrediente> lista = new ArrayList<>();
        String sql = "SELECT * FROM receita_ingrediente";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ReceitaIngrediente ri = new ReceitaIngrediente(
                    rs.getInt("id_receita"),
                    rs.getInt("id_ingrediente"),
                    rs.getDouble("quantidade"),
                    rs.getString("medida")
                );
                lista.add(ri);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ReceitaIngrediente buscarPorId(int idReceita, int idIngrediente) {
        String sql = "SELECT * FROM receita_ingrediente WHERE id_receita = ? AND id_ingrediente = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceita);
            stmt.setInt(2, idIngrediente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ReceitaIngrediente(
                        rs.getInt("id_receita"),
                        rs.getInt("id_ingrediente"),
                        rs.getDouble("quantidade"),
                        rs.getString("medida")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean atualizar(ReceitaIngrediente ri) {
        String sql = "UPDATE receita_ingrediente SET quantidade = ?, medida = ? WHERE id_receita = ? AND id_ingrediente = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, ri.getQuantidade());
            stmt.setString(2, ri.getMedida());
            stmt.setInt(3, ri.getIdReceita());
            stmt.setInt(4, ri.getIdIngrediente());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int idReceita, int idIngrediente) {
        String sql = "DELETE FROM receita_ingrediente WHERE id_receita = ? AND id_ingrediente = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceita);
            stmt.setInt(2, idIngrediente);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
