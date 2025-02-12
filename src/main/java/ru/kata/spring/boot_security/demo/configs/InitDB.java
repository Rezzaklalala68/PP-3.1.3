package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class InitDB {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public InitDB(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    private void fillDb() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        roleService.add(roleAdmin);
        roleService.add(roleUser);

        User user = new User("user@example.ru", "John", "Doe", 35, "123");
        user.addRole(roleUser);
        userService.add(user);

        User admin = new User("admin@example.ru", "Jane", "Smith", 28, "123");
        admin.addRole(roleAdmin);
        userService.add(admin);
    }
}
