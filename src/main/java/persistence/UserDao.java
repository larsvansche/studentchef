package persistence;

import model.Recipe;
import model.User;

import java.util.List;

public interface UserDao {
    public User findById(int id);
}
