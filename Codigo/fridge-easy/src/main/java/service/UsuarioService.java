package service;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import dao.UsuarioDAO;
import model.Usuario;

public class UsuarioService {
    private UsuarioDAO userDAO;

    public UsuarioService() {
        this.userDAO = new UsuarioDAO();
    }

    public boolean registerUser(Usuario user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().length() < 3)
            return false;
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isEmpty())
            return false;
        if (user.getPassword() == null || user.getPassword().length() < 8 || user.getPassword().isEmpty())
            return false;

        try {
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashed);

            userDAO.createUser(user);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario getUserByEmail(String email) {
        try {
            return userDAO.findByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUpdateUser(String oldEmail, String newName, String newEmail) {
        try {
            return userDAO.updateUser(oldEmail, newName, newEmail);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String email) {
        try {
            return userDAO.deleteUserByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
