package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    void create(User user);

    void delete(long id);

    void update(User user);

    User getUserById(long id);

    List<User> getAllUsers();

    Role getRoleById(Long id);

    List<Role> getAllRoles();


}
