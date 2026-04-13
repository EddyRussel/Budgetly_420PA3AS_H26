package com.example.budgetly_v1_avecsb.service;

import com.example.budgetly_v1_avecsb.model.User;
import com.example.budgetly_v1_avecsb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + email));
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new RuntimeException("Un compte avec cet email existe déjà");
        return userRepository.save(user);
    }

    public User updateUser(int id, User updated) {
        User existing = getUserById(id);
        existing.setEmail(updated.getEmail());
        return userRepository.save(existing);
    }

    public void deleteUser(int id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    public boolean login(String email, String password) {
        return userRepository.findByEmail(email)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }
}