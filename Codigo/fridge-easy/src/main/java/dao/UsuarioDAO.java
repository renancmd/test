package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Usuario;

public class UsuarioDAO extends DAO {
    // Criando um novo usuário
    public void createUser(Usuario user) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, preferencias_alimentares) VALUES (?, ?, ?, ?::preferencias_alimentares)";

        connect();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());

            if (user.getDietaryPreferences() != null) {
                statement.setString(4, user.getDietaryPreferences());
            } else {
                statement.setString(4, "refeicoes");
            }

            statement.executeUpdate();
            System.out.println("Usuário cadastrado com sucesso!");
        } finally {
            disconnect();
        }
    }

    // Loga usuário baseado na existência do email no BD
    public Usuario findByEmail(String email) throws SQLException {
        connect();

        String sql = "SELECT * FROM usuario WHERE email = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Usuario user = new Usuario();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("nome"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("senha"));
                user.setDietaryPreferences(rs.getString("preferencias_alimentares"));
                return user;
            } else {
                return null;
            }
        } finally {
            disconnect();
        }
    }

    // Atualiza dados do usuário
    public boolean updateUser(String emailAntigo, String novoNome, String novoEmail) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE usuario SET ");
        boolean alterarNome = novoNome != null && novoNome.length() >= 3;
        boolean alterarEmail = novoEmail != null && novoEmail.contains("@");
    
        if (!alterarNome && !alterarEmail) {
            return false;
        }
    
        if (alterarNome) sql.append("nome = ?");
        if (alterarEmail) {
            if (alterarNome) sql.append(", ");
            sql.append("email = ?");
        }
        sql.append(" WHERE email = ?");
    
        connect();
    
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (alterarNome) {
                stmt.setString(index++, novoNome);
            }
            if (alterarEmail) {
                stmt.setString(index++, novoEmail);
            }
            stmt.setString(index, emailAntigo);
    
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } finally {
            disconnect();
        }
    }
    
    public boolean deleteUserByEmail(String email) throws SQLException {
        String sql = "DELETE FROM usuario WHERE email = ?";
    
        connect();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } finally {
            disconnect();
        }
    }    

}
