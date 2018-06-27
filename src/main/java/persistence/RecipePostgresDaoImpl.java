package persistence;

import model.RatingService;
import model.Rating;
import model.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipePostgresDaoImpl extends PostgresBaseDao implements RecipeDao {
    private RatingService ratingService;
    public RecipePostgresDaoImpl() {
        this.ratingService = new RatingService();
    }

    public List<Recipe> findAll() {
        List<Recipe> recipeList = new ArrayList<Recipe>();

        try (Connection conn = super.getConnection()) {

            String query = "" +
                    "SELECT re.id, re.title, re.preptime, avg(ra.value) AS averagerating, re.description, re.guidejson\n" +
                    "FROM recipes re\n" +
                    "LEFT JOIN ratings ra\n" +
                    "ON re.id = ra.recipe_id\n" +
                    "GROUP BY re.id;";
            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                recipeList.add(buildRecipe(resultSet));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return recipeList;
    }

    public Recipe findById(int id) {

        try (Connection conn = super.getConnection()) {

            String query = "" +
                    "SELECT re.id, re.title, re.preptime, re.description, re.guidejson " +
                    "FROM recipes re " +
                    "WHERE re.id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                return buildRecipe(resultSet);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private Recipe buildRecipe (ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        int preptime = resultSet.getInt("preptime");
        String description = resultSet.getString("description");
        String guideJSON = resultSet.getString("guidejson");

        // get all ratings in arraylist from ratingdaoimpl and put them in the arraylist ratings
        ArrayList<Rating> ratings = this.ratingService.getRatingByRecipeId(id);
        System.out.println("Ratings: " + ratings);

        return new Recipe(id, title, preptime, description, guideJSON, ratings);
    }
}
