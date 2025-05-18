package com.developer.onlybuns.service.impl;


import com.developer.onlybuns.entity.User;
import com.developer.onlybuns.service.KorisnikService;
import com.developer.onlybuns.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements KorisnikService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public String getKorisnikUloga(String email) {
        User user = findByEmail(email);
        String uloga = user.getUloga().toString();
        return uloga;
    }



}
