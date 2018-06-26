package persistence;

import model.Recipe;

import java.util.List;

public interface RecipeDao {

    public List<Recipe> findAll();
    public Recipe findById(int id);
}
