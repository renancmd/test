package model;

public class Usuario {
    private Integer id;  // Pode ser null antes do insert
    private String name;
    private String email;
    private String password;
    private String dietaryPreferences;

    public Usuario() {}

    // Construtor para cadastro (sem id nem preferências)
    public Usuario(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dietaryPreferences = null;  // o banco vai usar o valor default
    }

    // Getters e Setters
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getDietaryPreferences() { return dietaryPreferences; }

    public void setDietaryPreferences(String dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dietaryPreferences='" + dietaryPreferences + '\'' +
                '}';
    }
}
