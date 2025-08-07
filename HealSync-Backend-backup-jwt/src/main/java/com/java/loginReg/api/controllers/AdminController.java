package com.java.loginReg.api.controllers;

import com.java.loginReg.business.abstracts.UserService;
import com.java.loginReg.dataAccess.UserRepository;
import com.java.loginReg.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // ✅ Restrict all endpoints to ADMIN
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get all users (patients, doctors, admins)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ✅ Delete user by ID
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUserById(id);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

    // ✅ Promote user to a new role (e.g., from PATIENT → DOCTOR)
    @PutMapping("/user/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long id,
            @RequestParam String newRole) {

        boolean updated = userService.updateUserRole(id, newRole.toUpperCase());
        if (updated) {
            return ResponseEntity.ok("User role updated to " + newRole.toUpperCase());
        } else {
            return ResponseEntity.badRequest().body("User or role invalid.");
        }
    }
}
