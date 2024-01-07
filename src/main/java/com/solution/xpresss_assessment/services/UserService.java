package com.solution.xpresss_assessment.services;

import com.solution.xpresss_assessment.data.models.User;

import java.util.List;

public interface UserService {
    User findUserByEmail(String email);
    void saveUser(User user);
    void  deleteUser(User user);
    List<User> getAllUsers();
    User findUserById(Long userId);
}


