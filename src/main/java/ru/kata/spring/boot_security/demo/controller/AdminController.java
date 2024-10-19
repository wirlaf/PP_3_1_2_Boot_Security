package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user";
    }

    @GetMapping("/user")
    public String show(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "show";
    }

    @GetMapping("/user/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "new";
    }

    @PostMapping("/user/new")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "roleIds", required = false) List<String> roleIds,
                         Model model) {

        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email is already in use");
            model.addAttribute("allRoles", userService.getAllRoles());
            return "new";
        }
        user.setRoles(getRolesFromIds(roleIds));
        userService.create(user);
        return "redirect:/admin";
    }

    @GetMapping("/user/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", userService.getAllRoles());
        return "/edit";
    }

    @PostMapping("/user/{id}")
    public String update(Model model,
                         @ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") long id,
                         @RequestParam("roles") List<String> roleIds) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.getAllRoles());
            return "/edit";
        }

        user.setRoles(getRolesFromIds(roleIds));
        userService.update(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/user/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    // Вынесенный метод для получения ролей по ID
    private Set<Role> getRolesFromIds(List<String> roleIds) {
        Set<Role> userRoles = new HashSet<>();
        if (roleIds != null) {
            for (String roleId : roleIds) {
                Role role = userService.getRoleById(Long.parseLong(roleId));
                userRoles.add(role);
            }
        }
        return userRoles;
    }
}
