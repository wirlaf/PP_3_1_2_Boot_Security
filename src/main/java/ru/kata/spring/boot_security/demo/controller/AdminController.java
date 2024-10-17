package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncode, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncode;
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user";
    }

    @GetMapping("/user/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "show";
    }

    @GetMapping("/user/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles()); // Передаем все роли в модель
        return "new";
    }

    @PostMapping
    public String create(Model model,
                         @ModelAttribute("user") @Valid User user,
                         @RequestParam List<String> listRoleId,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.getAllRoles());
            return "/new";
        }
        Set<Role> userRole = new HashSet<>();
        for (String roleId : listRoleId) {
            Role role = roleService.getRoleById(Long.parseLong(roleId));
            userRole.add(role);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRole);
        userService.create(user);
        return "redirect:/admin/user/" + user.getId();
    }

//    @PostMapping
//    public String create(@ModelAttribute("user") User user, @RequestParam List<String> listRoleId) {
//        Set<Role> userRole = new HashSet<>();
//        for (String roleId : listRoleId) {
//            Role role = roleService.getRoleById(Long.parseLong(roleId));
//            userRole.add(role);
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            user.setRoles(userRole);
//        }
//
//        userService.create(user);
//        return "redirect:/admin";
//    }
    @GetMapping("/user/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", userService.getAllRoles());
        return "/edit";
    }

    @PostMapping("/user/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") long id,
                         @RequestParam("roles") List<String> roleIds,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.getAllRoles());
            return "/edit";
        }
        User existingUser = userService.getUserById(id);
        if (!user.getPassword().equals(existingUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        Set<Role> userRoles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = userService.getRoleById(Long.parseLong(roleId));
            userRoles.add(role);
        }
        user.setRoles(userRoles);
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/user/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
