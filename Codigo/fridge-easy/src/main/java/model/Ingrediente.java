package model;

public class Ingrediente {

    private String name;
    private String category;
    private String nutritional_value;

    public Ingrediente(String name, String category, String nutritional_value) {
        this.name = name;
        this.category = category;
        this.nutritional_value = nutritional_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNutritionalValue() {
        return nutritional_value;
    }

    public void setNutritionalValue(String nutritional_value) {
        this.nutritional_value = nutritional_value;
    }
}
