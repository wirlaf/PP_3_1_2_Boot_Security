package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUsers(ModelMap model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("user", userService.findByUsername(userDetails.getUsername()));
        model.addAttribute("allusers", userService.findAllWithRoles());
        model.addAttribute("allroles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin-view";
    }

    @PostMapping(value = "/createUser")
    public String create(@ModelAttribute("user") User user, @RequestParam(name = "roles", required = false) List<Integer> roleId) {
        setUserRoles(user, roleId);
        userService.create(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/updateUser")
    public String update(@ModelAttribute("editedUser") User user, @RequestParam(name = "roles", required = false) List<Integer> roleId) {
        setUserRoles(user, roleId);
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String delete(@RequestParam("userId") Integer userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin";
    }

    private void setUserRoles(User user, List<Integer> roleId) {
        Set<Role> roleSet = new HashSet<>();
        if (roleId != null) {
            roleId.forEach(id -> roleSet.add(roleService.findRoleById(id)));
        }
        user.setUserRoles(roleSet);
    }
}
