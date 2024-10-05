package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String index(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "/user";
    }

    @GetMapping("/user/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.read(id));
        return "/show";
    }

    @GetMapping("/user/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "/new";
    }

    @PostMapping("/users")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/new";

        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("/user/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.read(id));
        return "/edit";
    }

    @PostMapping("/user/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") long id) {
        if (bindingResult.hasErrors())
            return "/edit";

        userService.update(id, user);
        return "redirect:/users";
    }

    @PostMapping("/user/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
