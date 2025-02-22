package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model, @AuthenticationPrincipal User user) {
        List<Role> set= roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", set);
        return "admin";
    }

    @GetMapping("/users/{id}")
    public String findUserById(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        model.addAttribute("allRoles", roleService.findAll());
        return "user";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute User user,  Model model) {
        // Проверяем, используется ли email другим пользователем
        Optional<User> userWithSameEmail = userService.findUserAndFetchRoles(user.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(user.getId())) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("errorMessage", "Email уже используется!");
            model.addAttribute("user", user);
            return "admin";
        }


        userService.add(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userService.removeById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/admin";
    }

    @PutMapping("/users/{id}")
    public String updateUser(@ModelAttribute User user, @PathVariable Long id, Model model) {
            // Проверяем, используется ли email другим пользователем
            Optional<User> userWithSameEmail = userService.findUserAndFetchRoles(user.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                model.addAttribute("users", userService.findAll());
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("errorMessage", "Email уже используется!");
                model.addAttribute("user", user);
                return "admin";
            }


            // Обновляем пароль только если он был передан
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(user.getPassword());
            }

            // Сохраняем обновленного пользователя
            userService.update(user);
            return "redirect:/admin";

    }
}