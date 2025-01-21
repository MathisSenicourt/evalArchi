package com.example.userservice.service.impl;

import com.example.userservice.entity.MembershipType;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        // Définir le nombre maximum d'emprunts en fonction du type d'adhésion
        user.setNombreMaxEmprunt(user.getMembershipType() == MembershipType.PREMIUM ? 7 : 5);
        user.setLocked(false); // Débloqué par défaut
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setMembershipType(updatedUser.getMembershipType());
        existingUser.setNombreMaxEmprunt(updatedUser.getMembershipType() == MembershipType.PREMIUM ? 7 : 5);
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public void lockUser(Long id) {
        User user = getUserById(id);
        user.setLocked(true);
        userRepository.save(user);
    }

    @Override
    public void unlockUser(Long id) {
        User user = getUserById(id);
        user.setLocked(false);
        userRepository.save(user);
    }
}
