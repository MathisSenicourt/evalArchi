package com.example.userservice.service;

import com.example.userservice.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long userId, User user);

    void deleteUser(Long id);

    User createUser(User user);

    void lockUser(Long id);

    void unlockUser(Long id);
}