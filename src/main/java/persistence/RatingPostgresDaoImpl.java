package persistence;

import model.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipePostgresDaoImpl extends PostgresBaseDao implements RecipeDao {
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
                    "SELECT re.id, re.title, re.preptime, avg(ra.value) AS averagerating, re.description, re.guidejson " +
                    "FROM recipes re " +
                    "LEFT JOIN ratings ra " +
                    "ON re.id = ra.recipe_id " +
                    "WHERE re.id = ? " +
                    "GROUP BY re.id;";
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
        double averageRating = resultSet.getDouble("averagerating");
        String description = resultSet.getString("description");
        String guideJSON = resultSet.getString("guidejson");

        return new Recipe(id, title, preptime, averageRating, description, guideJSON);
    }
}
