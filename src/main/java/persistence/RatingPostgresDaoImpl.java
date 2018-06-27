package persistence;

import model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RatingPostgresDaoImpl extends PostgresBaseDao implements RatingDao {
    private UserDao userDaoPostgresImpl;

    public RatingPostgresDaoImpl() {
        this.userDaoPostgresImpl = new UserPostgresDaoImpl();
    }

    public Rating findByRecipeIdAndUserId(int userId, int recipeId) {

        try (Connection conn = super.getConnection()) {

            String query = "" +
                    "SELECT r.user_id, u.username, r.recipe_id, r.value, r.datetime, r.description " +
                    "FROM ratings r " +
                    "LEFT JOIN users u " +
                    "ON u.id = r.user_id " +
                    "WHERE recipe_id = ? " +
                    "AND r.user_id = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, recipeId);
            ps.setInt(2, userId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                return buildRating(resultSet);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public ArrayList<Rating> findByRecipeId(int recipeId) {

        try (Connection conn = super.getConnection()) {
            ArrayList<Rating> returnList = new ArrayList<Rating>();

            String query = "" +
                    "SELECT r.user_id, u.username, r.recipe_id, r.value, r.datetime, r.description " +
                    "FROM ratings r " +
                    "LEFT JOIN users u " +
                    "ON u.id = r.user_id " +
                    "WHERE recipe_id = ? ";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, recipeId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                returnList.add(buildRating(resultSet));
            }

            return returnList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Rating createRating(Rating rating) {

        try (Connection conn = super.getConnection()) {
            String query = "" +
                    "INSERT INTO ratings " +
                    "(user_id, recipe_id, value, description, datetime) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?::timestamp with time zone);";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, rating.getUserId());
            ps.setInt(2, rating.getRecipeId());
            ps.setInt(3, rating.getValue());
            ps.setString(4, rating.getDescription());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
            ps.setString(5, formatter.format(rating.getDatetime()));

            if (ps.executeUpdate() == 1) {
                return rating;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Rating updateRating(Rating rating) {

        try (Connection conn = super.getConnection()) {
            String query = "" +
                    "UPDATE ratings " +
                    "SET value = ?, " +
                    "description = ?, " +
                    "datetime = current_timestamp(0) " +
                    "WHERE user_id = ? " +
                    "AND recipe_id = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, rating.getValue());
            ps.setString(2, rating.getDescription());
            ps.setInt(3, rating.getUserId());
            ps.setInt(4, rating.getRecipeId());

            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rating;
    }

    public int deleteRating(int userId, int recipeId) {

        try (Connection conn = super.getConnection()) {
            String query = "" +
                    "DELETE FROM ratings " +
                    "WHERE user_id = ? " +
                    "AND recipe_id = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, recipeId);

            return ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private Rating buildRating (ResultSet resultSet) throws SQLException, ParseException {
        int userId = resultSet.getInt("user_id");
        String userName = resultSet.getString("username");
        int recipeId = resultSet.getInt("recipe_id");
        int value = resultSet.getInt("value");
        String fetchedDatetime = resultSet.getString("datetime");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
        Date datetime = format.parse(fetchedDatetime);
        System.out.println(datetime);
        String description = resultSet.getString("description");

        return new Rating(recipeId, userId, userName, value, datetime, description);
    }
}
