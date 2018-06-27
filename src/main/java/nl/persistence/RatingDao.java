package nl.persistence;

import nl.model.Rating;

import java.util.ArrayList;

public interface RatingDao {
    public Rating findByRecipeIdAndUserId(int userId, int recipeId);
    public ArrayList<Rating> findByRecipeId(int recipeId);

    public Rating createRating(Rating rating);
    public Rating updateRating(Rating rating);

    public int deleteRating(int userId, int recipeId);
}
