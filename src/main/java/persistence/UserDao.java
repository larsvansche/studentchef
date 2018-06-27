package persistence;

import model.User;

public interface UserDao {
    public User findById(int id);
}
