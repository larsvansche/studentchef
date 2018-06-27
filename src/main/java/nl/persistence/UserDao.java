package nl.persistence;

import nl.model.User;

public interface UserDao {
    public User findById(int id);
}
