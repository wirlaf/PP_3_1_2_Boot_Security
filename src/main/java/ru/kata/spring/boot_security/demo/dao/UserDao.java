package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {

    void create(User user);

    User read(long id);

    void update(long id, User user);

    void delete(long id);

    List<User> listUsers();
}
