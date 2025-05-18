package com.developer.onlybuns.service;

public interface UsernameValidationService {

    public void addUsername(String username);

    public boolean mightContainUsername(String username);

    public boolean isUsernameValid(String username);

    public void loadUsernamesFromDatabase();
}
