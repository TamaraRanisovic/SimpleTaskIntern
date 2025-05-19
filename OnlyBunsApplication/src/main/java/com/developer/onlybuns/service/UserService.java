package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.User;

public interface UserService {
    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
    String getUserRole(String email);

}