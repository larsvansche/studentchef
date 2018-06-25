package model;

import persistence.RatingPostgresDaoImpl;
import persistence.RecipeDao;
import persistence.RecipePostgresDaoImpl;

import java.util.List;

public class RecipeService {
    private RecipeDao recipeDao;
    private RecipeDao ratingDao;

    public RecipeService() {
        this.recipeDao = new RecipePostgresDaoImpl();
        this.ratingDao = new RatingPostgresDaoImpl();
    }

    public List<Recipe> getAllRecipes() {
        return this.recipeDao.findAll();
    }

    public Recipe getRecipeById(int id) {
        return this.recipeDao.findById(id);
    }


}
