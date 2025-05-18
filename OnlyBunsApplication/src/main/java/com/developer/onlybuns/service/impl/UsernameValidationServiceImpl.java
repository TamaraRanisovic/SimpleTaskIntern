package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.service.RegisteredUserService;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Service;
import com.developer.onlybuns.service.UsernameValidationService;

import java.nio.charset.StandardCharsets;

@Service
public class UsernameValidationServiceImpl implements UsernameValidationService {

    private final BloomFilter<String> bloomFilter;

    private final RegisteredUserService registeredUserService;

    // Initialize Bloom Filter with expected size and false positive probability
    public UsernameValidationServiceImpl(RegisteredUserService registeredUserService) {
        this.bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                15,
                0.01
        );
        this.registeredUserService = registeredUserService;
    }

    // Add a username to the Bloom Filter
    @Override
    public void addUsername(String username) {
        bloomFilter.put(username);
    }

    // Check if a username exists (probabilistic)
    @Override
    public boolean mightContainUsername(String username) {
        return bloomFilter.mightContain(username);
    }

    // Validate username against database if Bloom Filter indicates presence
    @Override
    public boolean isUsernameValid(String username) {
        if (mightContainUsername(username)) {
            // Double-check in the database to avoid false positives
            return registeredUserService.usernameExists(username);
        }
        return false; // Username is definitely not in the Bloom Filter
    }

    @Override
    public void loadUsernamesFromDatabase() {
        for (String username : registeredUserService.getAllUsernames()) {
            addUsername(username);
        }
    }
}
