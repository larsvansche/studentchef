package model;

import persistence.RecipeDao;
import persistence.RecipePostgresDaoImpl;

import java.util.List;

public class RecipeService {
    private RecipeDao recipeDao;

    public RecipeService() {
        this.recipeDao = new RecipePostgresDaoImpl();
    }

    public List<Recipe> getAllRecipes() {
        return this.recipeDao.findAll();
    }

    public Recipe getRecipeById(int id) {
        return this.recipeDao.findById(id);
    }
}
