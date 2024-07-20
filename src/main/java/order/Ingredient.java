package order;

import java.util.List;

// Определение класса Ingredient
public class Ingredient {

    public List<String> ingredients;

    // Конструктор класса
    public Ingredient(List<String> ingredients) {
        // Присваиваем список ингредиентов
        this.ingredients = ingredients;
    }
}