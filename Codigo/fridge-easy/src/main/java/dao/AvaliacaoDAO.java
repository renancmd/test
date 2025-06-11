package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Avaliacao;

public class AvaliacaoDAO extends DAO {
    public boolean create(Avaliacao avaliacao) {
        try {
            connect();
            String sql = "INSERT INTO avaliacao (nota, comentario, id_receita, id_usuario) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, avaliacao.getNota());
            ps.setString(2, avaliacao.getComentario());
            ps.setInt(3, avaliacao.getIdReceita());
            ps.setInt(4, avaliacao.getIdUsuario());
            int rows = ps.executeUpdate();
            ps.close();
            disconnect();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Avaliacao> readAll() {
        List<Avaliacao> avaliacoes = new ArrayList<>();
        try {
            connect();
            String sql = "SELECT * FROM avaliacao";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Avaliacao a = new Avaliacao(
                    rs.getInt("id"),
                    rs.getInt("nota"),
                    rs.getString("comentario"),
                    rs.getInt("id_receita"),
                    rs.getInt("id_usuario")
                );
                avaliacoes.add(a);
            }
            rs.close();
            st.close();
            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avaliacoes;
    }

    public Avaliacao readById(int id) {
        try {
            connect();
            String sql = "SELECT * FROM avaliacao WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Avaliacao a = new Avaliacao(
                    rs.getInt("id"),
                    rs.getInt("nota"),
                    rs.getString("comentario"),
                    rs.getInt("id_receita"),
                    rs.getInt("id_usuario")
                );
                rs.close();
                ps.close();
                disconnect();
                return a;
            }
            rs.close();
            ps.close();
            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(Avaliacao avaliacao) {
        try {
            connect();
            String sql = "UPDATE avaliacao SET nota=?, comentario=?, id_receita=?, id_usuario=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, avaliacao.getNota());
            ps.setString(2, avaliacao.getComentario());
            ps.setInt(3, avaliacao.getIdReceita());
            ps.setInt(4, avaliacao.getIdUsuario());
            ps.setInt(5, avaliacao.getId());
            int rows = ps.executeUpdate();
            ps.close();
            disconnect();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            connect();
            String sql = "DELETE FROM avaliacao WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            ps.close();
            disconnect();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
