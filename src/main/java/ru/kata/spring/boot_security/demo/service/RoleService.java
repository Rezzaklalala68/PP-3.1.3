package ru.kata.spring.boot_security.demo.service;

import org.springframework.data.jpa.repository.EntityGraph;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface RoleService {


    Role add(Role role);


    List<Role> findAll();

}

