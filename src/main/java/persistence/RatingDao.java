package persistence;

import model.Rating;
import model.Recipe;

import java.util.ArrayList;
import java.util.List;

public interface RatingDao {
    public Rating findByRecipeIdAndUserId(int userId, int recipeId);
    public ArrayList<Rating> findByRecipeId(int recipeId);

    public Rating createRating(Rating rating);
    public Rating updateRating(Rating rating);

    public int deleteRating(int userId, int recipeId);
}
