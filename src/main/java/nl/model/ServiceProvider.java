package nl.model;

public class ServiceProvider {
    private static RecipeService recipeService = new RecipeService();
    private static RatingService ratingService = new RatingService();

    public static RecipeService getRecipeService() {
        return recipeService;
    }
    public static RatingService getRatingService() {
        return ratingService;
    }
}
