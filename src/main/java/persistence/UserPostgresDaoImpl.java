package persistence;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPostgresDaoImpl extends PostgresBaseDao implements UserDao {

    public User findById(int id) {

        try (Connection conn = super.getConnection()) {

            String query = "" +
                    "SELECT id, username, password " +
                    "FROM users " +
                    "WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                return buildUser(resultSet);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private User buildUser (ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");

        return new User(id, username, password);
    }
}
