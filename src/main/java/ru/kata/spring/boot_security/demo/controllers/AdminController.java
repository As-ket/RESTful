package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String show(Principal principal, ModelMap model) {
        model.addAttribute("activeUser", userService.findByName(principal.getName()));
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("user", new User());
        return "all-users";
    }

//    @GetMapping()
//    public String showAllUsers(Model model) {
//        model.addAttribute("allUsers", userService.getAllUsers());
//        model.addAttribute("allRole", roleRepository.findAll());
//        return "all-users";
//    }

//    @GetMapping("/{id}")
//    public String showOneUser(@PathVariable("id") int id, Model model) {
//        model.addAttribute("user", userService.getUserById(id));
//        return "show";
//    }

//    @GetMapping("/newUser")
//    public String newUser(@ModelAttribute("user") User user) {
//        return "all-users";
//    }

//    @GetMapping("/new")
//    public String newUser(Model model) {
//        model.addAttribute("allRole", roleRepository.findAll());
//        return "new";
//    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        Set<Role> sr = new HashSet<>(roleRepository.findAll());
//        user.setRole(sr);
        userService.addUser(user);
        return "redirect:/admin";
    }

//    @GetMapping("/{id}/edit")
//    public String editUser(Model model, @PathVariable("id") int id) {
//        model.addAttribute("user", userService.getUserById(id));
//        return "edit";
//    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
