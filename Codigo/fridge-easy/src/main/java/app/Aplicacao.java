package app;

import static spark.Spark.*;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import model.Avaliacao;
import model.Ingrediente;
import model.Receita;
import model.ReceitaIngrediente;
import model.Usuario;
import service.AvaliacaoService;
import service.IngredienteService;
import service.ReceitaIngredienteService;
import service.ReceitaService;
import service.UsuarioService;
import util.TokenUtils;

public class Aplicacao {
    public static String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");

        get("/", (req, res) -> {
            res.redirect("/index.html");
            System.out.println("Página inicial acessada");
            return null;
        });

        // CRUD do usuário
        post("/register", (req, res) -> {
            String name = req.queryParams("name");
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            Usuario user = new Usuario(name, email, password);

            UsuarioService userService = new UsuarioService();
            boolean isSuccess = userService.registerUser(user);

            if (isSuccess) {
                res.status(201);
                return "Usuário cadastrado com sucesso!";
            } else {
                res.status(400);
                return "Erro ao cadastrar usuário";
            }

        });

        post("/login", (req, res) -> {
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            UsuarioService userService = new UsuarioService();
            Usuario user = userService.getUserByEmail(email);

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                String token = TokenUtils.generateToken(email);
                res.status(200);
                return token;
            } else {
                res.status(401);
                return "Credenciais inválidas";
            }
        });

        get("/user", (req, res) -> {
            String authHeader = req.headers("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401);
                return "Token ausente ou inválido";
            }

            String token = authHeader.substring(7);

            try {
                String email = TokenUtils.validateToken(token);

                UsuarioService userService = new UsuarioService();
                Usuario user = userService.getUserByEmail(email);

                if (user == null) {
                    res.status(404);
                    return "Usuário não encontrado";
                }

                res.type("application/json");
                String json = String.format(
                        "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"dietaryPreferences\":\"%s\"}",
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getDietaryPreferences() != null ? user.getDietaryPreferences() : "");
                return json;
            } catch (Exception e) {
                res.status(500);
                return "Token inválido ou expirado";
            }
        });

        put("/user", (req, res) -> {
            String authHeader = req.headers("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401);
                return "Token ausente ou inválido";
            }

            String token = authHeader.substring(7);

            try {
                String oldEmail = TokenUtils.validateToken(token);

                String newName = req.queryParams("name");
                String newEmail = req.queryParams("email");

                // Se nenhum campo válido for enviado, retorna erro
                if ((newName == null || newName.length() < 3) && (newEmail == null || !newEmail.contains("@"))) {
                    res.status(400);
                    return "Dados inválidos";
                }

                UsuarioService userService = new UsuarioService();
                boolean isUpdate = userService.isUpdateUser(oldEmail, newName, newEmail);

                if (!isUpdate) {
                    res.status(500);
                    return "Erro ao atualizar usuário";
                }

                return "Usuário atualizado com sucesso!";
            } catch (Exception e) {
                res.status(401);
                return "Token inválido ou expirado";
            }
        });

        delete("/user", (req, res) -> {
            String authHeader = req.headers("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401);
                return "Token ausente ou inválido";
            }

            String token = authHeader.substring(7);
            String email;

            try {
                email = TokenUtils.validateToken(token);
            } catch (Exception e) {
                res.status(403);
                return "Token inválido";
            }

            UsuarioService userService = new UsuarioService();
            boolean deleted = userService.deleteUser(email);

            if (deleted) {
                res.status(200);
                return "Conta excluída com sucesso.";
            } else {
                res.status(500);
                return "Erro ao excluir conta.";
            }
        });

        // CRUD do ingrediente
        post("/create-ingredient", (req, res) -> {
            try {
                String name = req.queryParams("name");
                String category = req.queryParams("category");
                String calories = req.queryParams("calories");
                String weight = req.queryParams("weight");

                System.out.println("Recebido: " + name + ", " + category + ", " + calories + ", " + weight);

                IngredienteService service = new IngredienteService();
                boolean success = service.createIngrediente(name, category, calories, weight);

                if (success) {
                    res.status(201);
                    return "Ingrediente criado com sucesso!";
                } else {
                    res.status(500);
                    return "Erro ao criar ingrediente (insert falhou).";
                }
            } catch (Exception e) {
                e.printStackTrace(); // imprime no console do servidor
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        get("/ingredients", (req, res) -> {
            try {
                IngredienteService service = new IngredienteService();
                List<Ingrediente> lista = service.listarIngredientes();

                if (lista == null || lista.isEmpty()) {
                    res.status(404);
                    return "Nenhum ingrediente encontrado";
                }

                res.type("application/json");

                StringBuilder json = new StringBuilder();
                json.append("[");

                for (int i = 0; i < lista.size(); i++) {
                    Ingrediente ing = lista.get(i);
                    String itemJson = String.format(
                            "{\"name\":\"%s\",\"category\":\"%s\",\"nutritional_value\":\"%s\"}",
                            escape(ing.getName()),
                            escape(ing.getCategory()),
                            escape(ing.getNutritionalValue()));
                    json.append(itemJson);
                    if (i < lista.size() - 1) {
                        json.append(",");
                    }
                }

                json.append("]");
                return json.toString();

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Erro ao listar ingredientes";
            }
        });

        put("/update-ingredient", (req, res) -> {
            try {
                String oldName = req.queryParams("oldName");
                String name = req.queryParams("name");
                String category = req.queryParams("category");
                String calories = req.queryParams("calories");
                String weight = req.queryParams("weight");

                IngredienteService service = new IngredienteService();
                boolean success = service.updateIngrediente(oldName, name, category, calories, weight);

                if (success) {
                    res.status(200);
                    return "Ingrediente atualizado com sucesso!";
                } else {
                    res.status(404);
                    return "Ingrediente não encontrado ou erro ao atualizar.";
                }
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        delete("/delete-ingredient", (req, res) -> {
            try {
                String name = req.queryParams("name");
                IngredienteService service = new IngredienteService();
                boolean success = service.deleteIngrediente(name);
                if (success) {
                    res.status(200);
                    return "Ingrediente removido com sucesso!";
                } else {
                    res.status(404);
                    return "Ingrediente não encontrado ou erro ao remover.";
                }
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        // CRUD de receitas
        post("/receitas", (req, res) -> {
            try {
                String nome = req.queryParams("nome");
                String descricao = req.queryParams("descricao");
                boolean favorito = Boolean.parseBoolean(req.queryParams("favorito"));
                String imagem = req.queryParams("imagem");
                int tempoPreparo = Integer.parseInt(req.queryParams("tempoPreparo"));
                String nivelDificuldade = req.queryParams("nivelDificuldade");
                String filtro = req.queryParams("filtro");

                Receita receita = new Receita(nome, descricao, favorito, imagem, tempoPreparo, nivelDificuldade, filtro);
                ReceitaService service = new ReceitaService();
                boolean sucesso = service.criarReceita(receita);

                if (sucesso) {
                    res.status(201);
                    return "Receita criada com sucesso!";
                } else {
                    res.status(400);
                    return "Erro ao criar receita.";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        get("/receitas", (req, res) -> {
            ReceitaService service = new ReceitaService();
            List<Receita> receitas = service.listarReceitas();
            res.type("application/json");
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < receitas.size(); i++) {
                Receita r = receitas.get(i);
                String itemJson = String.format(
                    "{\"id\":%d,\"nome\":\"%s\",\"descricao\":\"%s\",\"favorito\":%b,\"imagem\":\"%s\",\"tempoPreparo\":%d,\"nivelDificuldade\":\"%s\",\"filtro\":\"%s\"}",
                    r.getId(), escape(r.getNome()), escape(r.getDescricao()), r.isFavorito(), escape(r.getImagem()), r.getTempoPreparo(), escape(r.getNivelDificuldade()), escape(r.getFiltro())
                );
                json.append(itemJson);
                if (i < receitas.size() - 1) json.append(",");
            }
            json.append("]");
            return json.toString();
        });

        put("/receitas/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                String nome = req.queryParams("nome");
                String descricao = req.queryParams("descricao");
                boolean favorito = Boolean.parseBoolean(req.queryParams("favorito"));
                String imagem = req.queryParams("imagem");
                int tempoPreparo = Integer.parseInt(req.queryParams("tempoPreparo"));
                String nivelDificuldade = req.queryParams("nivelDificuldade");
                String filtro = req.queryParams("filtro");

                Receita receita = new Receita(id, nome, descricao, favorito, imagem, tempoPreparo, nivelDificuldade, filtro);
                ReceitaService service = new ReceitaService();
                boolean sucesso = service.atualizarReceita(receita);

                if (sucesso) {
                    res.status(200);
                    return "Receita atualizada com sucesso!";
                } else {
                    res.status(404);
                    return "Receita não encontrada ou erro ao atualizar.";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        delete("/receitas/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                ReceitaService service = new ReceitaService();
                boolean sucesso = service.deletarReceita(id);
                if (sucesso) {
                    res.status(200);
                    return "Receita removida com sucesso!";
                } else {
                    res.status(404);
                    return "Receita não encontrada ou erro ao remover.";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        // CRUD de avaliações
        post("/avaliacoes", (req, res) -> {
            try {
                int nota = Integer.parseInt(req.queryParams("nota"));
                String comentario = req.queryParams("comentario");
                int idUsuario = Integer.parseInt(req.queryParams("id_usuario"));
                int idReceita = Integer.parseInt(req.queryParams("id_receita"));

                Avaliacao avaliacao = new Avaliacao(nota, comentario, idReceita, idUsuario);
                AvaliacaoService service = new AvaliacaoService();
                boolean sucesso = service.registrarAvaliacao(avaliacao);

                if (sucesso) {
                    res.status(201);
                    return "Avaliação registrada com sucesso!";
                } else {
                    res.status(400);
                    return "Erro ao registrar avaliação";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        get("/avaliacoes", (req, res) -> {
            AvaliacaoService service = new AvaliacaoService();
            List<Avaliacao> avaliacoes = service.listarAvaliacoes();
            res.type("application/json");
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < avaliacoes.size(); i++) {
                Avaliacao a = avaliacoes.get(i);
                json.append(String.format("{\"id\":%d,\"nota\":%d,\"comentario\":\"%s\",\"id_receita\":%d,\"id_usuario\":%d}",
                    a.getId(), a.getNota(), escape(a.getComentario()), a.getIdReceita(), a.getIdUsuario()));
                if (i < avaliacoes.size() - 1) json.append(",");
            }
            json.append("]");
            return json.toString();
        });

        get("/avaliacoes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            AvaliacaoService service = new AvaliacaoService();
            Avaliacao a = service.buscarPorId(id);
            if (a == null) {
                res.status(404);
                return "Avaliação não encontrada";
            }
            res.type("application/json");
            return String.format("{\"id\":%d,\"nota\":%d,\"comentario\":\"%s\",\"id_receita\":%d,\"id_usuario\":%d}",
                a.getId(), a.getNota(), escape(a.getComentario()), a.getIdReceita(), a.getIdUsuario());
        });

        put("/avaliacoes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                int nota = Integer.parseInt(req.queryParams("nota"));
                String comentario = req.queryParams("comentario");
                int idReceita = Integer.parseInt(req.queryParams("id_receita"));
                int idUsuario = Integer.parseInt(req.queryParams("id_usuario"));
                Avaliacao avaliacao = new Avaliacao(id, nota, comentario, idReceita, idUsuario);
                AvaliacaoService service = new AvaliacaoService();
                boolean sucesso = service.atualizarAvaliacao(avaliacao);
                if (sucesso) {
                    return "Avaliação atualizada com sucesso!";
                } else {
                    res.status(400);
                    return "Erro ao atualizar avaliação";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        delete("/avaliacoes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                AvaliacaoService service = new AvaliacaoService();
                boolean sucesso = service.removerAvaliacao(id);
                if (sucesso) {
                    return "Avaliação removida com sucesso!";
                } else {
                    res.status(404);
                    return "Avaliação não encontrada";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        // CRUD de receita_ingrediente
        post("/receita-ingrediente", (req, res) -> {
            try {
                int idReceita = Integer.parseInt(req.queryParams("id_receita"));
                int idIngrediente = Integer.parseInt(req.queryParams("id_ingrediente"));
                double quantidade = Double.parseDouble(req.queryParams("quantidade"));
                String medida = req.queryParams("medida");
                ReceitaIngrediente ri = new ReceitaIngrediente(idReceita, idIngrediente, quantidade, medida);
                ReceitaIngredienteService service = new ReceitaIngredienteService();
                boolean ok = service.criar(ri);
                if (ok) {
                    res.status(201);
                    return "ReceitaIngrediente criado com sucesso!";
                } else {
                    res.status(400);
                    return "Erro ao criar ReceitaIngrediente.";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        get("/receita-ingrediente", (req, res) -> {
            ReceitaIngredienteService service = new ReceitaIngredienteService();
            List<ReceitaIngrediente> lista = service.listarTodos();
            res.type("application/json");
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < lista.size(); i++) {
                ReceitaIngrediente ri = lista.get(i);
                json.append(String.format("{\"id_receita\":%d,\"id_ingrediente\":%d,\"quantidade\":%.2f,\"medida\":\"%s\"}",
                    ri.getIdReceita(), ri.getIdIngrediente(), ri.getQuantidade(), escape(ri.getMedida())));
                if (i < lista.size() - 1) json.append(",");
            }
            json.append("]");
            return json.toString();
        });

        get("/receita-ingrediente/:id_receita/:id_ingrediente", (req, res) -> {
            int idReceita = Integer.parseInt(req.params(":id_receita"));
            int idIngrediente = Integer.parseInt(req.params(":id_ingrediente"));
            ReceitaIngredienteService service = new ReceitaIngredienteService();
            ReceitaIngrediente ri = service.buscarPorId(idReceita, idIngrediente);
            if (ri == null) {
                res.status(404);
                return "ReceitaIngrediente não encontrado";
            }
            res.type("application/json");
            return String.format("{\"id_receita\":%d,\"id_ingrediente\":%d,\"quantidade\":%.2f,\"medida\":\"%s\"}",
                ri.getIdReceita(), ri.getIdIngrediente(), ri.getQuantidade(), escape(ri.getMedida()));
        });

        put("/receita-ingrediente/:id_receita/:id_ingrediente", (req, res) -> {
            try {
                int idReceita = Integer.parseInt(req.params(":id_receita"));
                int idIngrediente = Integer.parseInt(req.params(":id_ingrediente"));
                double quantidade = Double.parseDouble(req.queryParams("quantidade"));
                String medida = req.queryParams("medida");
                ReceitaIngrediente ri = new ReceitaIngrediente(idReceita, idIngrediente, quantidade, medida);
                ReceitaIngredienteService service = new ReceitaIngredienteService();
                boolean ok = service.atualizar(ri);
                if (ok) {
                    return "ReceitaIngrediente atualizado com sucesso!";
                } else {
                    res.status(400);
                    return "Erro ao atualizar ReceitaIngrediente.";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

        delete("/receita-ingrediente/:id_receita/:id_ingrediente", (req, res) -> {
            try {
                int idReceita = Integer.parseInt(req.params(":id_receita"));
                int idIngrediente = Integer.parseInt(req.params(":id_ingrediente"));
                ReceitaIngredienteService service = new ReceitaIngredienteService();
                boolean ok = service.deletar(idReceita, idIngrediente);
                if (ok) {
                    return "ReceitaIngrediente removido com sucesso!";
                } else {
                    res.status(400);
                    return "Erro ao remover ReceitaIngrediente.";
                }
            } catch (Exception e) {
                res.status(500);
                return "Erro no servidor: " + e.getMessage();
            }
        });

    }
}