package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Receita;

public class ReceitaDAO extends DAO {
    public boolean inserir(Receita receita) {
        try {
            connect();
            String sql = "INSERT INTO receita (nome, descricao, favorito, imagem, tempo_preparo, nivel_dificuldade, filtro) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, receita.getNome());
            stmt.setString(2, receita.getDescricao());
            stmt.setBoolean(3, receita.isFavorito());
            stmt.setString(4, receita.getImagem());
            stmt.setInt(5, receita.getTempoPreparo());
            stmt.setString(6, receita.getNivelDificuldade());
            stmt.setString(7, receita.getFiltro());
            int rows = stmt.executeUpdate();
            stmt.close();
            disconnect();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Receita> listar() {
        List<Receita> receitas = new ArrayList<>();
        try {
            connect();
            String sql = "SELECT * FROM receita";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Receita r = new Receita(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getBoolean("favorito"),
                    rs.getString("imagem"),
                    rs.getInt("tempo_preparo"),
                    rs.getString("nivel_dificuldade"),
                    rs.getString("filtro")
                );
                receitas.add(r);
            }
            rs.close();
            stmt.close();
            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receitas;
    }

    public Receita buscarPorId(int id) {
        Receita receita = null;
        try {
            connect();
            String sql = "SELECT * FROM receita WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                receita = new Receita(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getBoolean("favorito"),
                    rs.getString("imagem"),
                    rs.getInt("tempo_preparo"),
                    rs.getString("nivel_dificuldade"),
                    rs.getString("filtro")
                );
            }
            rs.close();
            stmt.close();
            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receita;
    }

    public boolean atualizar(Receita receita) {
        try {
            connect();
            String sql = "UPDATE receita SET nome=?, descricao=?, favorito=?, imagem=?, tempo_preparo=?, nivel_dificuldade=?, filtro=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, receita.getNome());
            stmt.setString(2, receita.getDescricao());
            stmt.setBoolean(3, receita.isFavorito());
            stmt.setString(4, receita.getImagem());
            stmt.setInt(5, receita.getTempoPreparo());
            stmt.setString(6, receita.getNivelDificuldade());
            stmt.setString(7, receita.getFiltro());
            stmt.setInt(8, receita.getId());
            int rows = stmt.executeUpdate();
            stmt.close();
            disconnect();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        try {
            connect();
            String sql = "DELETE FROM receita WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            stmt.close();
            disconnect();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
