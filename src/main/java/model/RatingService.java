package model;

import persistence.RatingDao;
import persistence.RatingPostgresDaoImpl;

import java.util.ArrayList;

public class RatingService {
    private RatingDao ratingDao;

    public RatingService() {
        this.ratingDao = new RatingPostgresDaoImpl();
    }

    public Rating getRatingByRecipeIdAndUserId(int userId, int recipeId) {
        return this.ratingDao.findByRecipeIdAndUserId(userId, recipeId);
    }

    public ArrayList<Rating> getRatingByRecipeId(int recipeId) {
        return this.ratingDao.findByRecipeId(recipeId);
    }

    public Rating createRating(Rating rating) {
        return this.ratingDao.createRating(rating);
    }

    public Rating updateRating(Rating rating) {
        return this.ratingDao.updateRating(rating);
    }

    public int deleteRating(int userId, int recipeId) {
        return this.ratingDao.deleteRating(userId, recipeId);
    }
}
