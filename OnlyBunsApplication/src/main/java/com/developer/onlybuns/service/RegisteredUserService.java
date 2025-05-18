package com.developer.onlybuns.service;
import com.developer.onlybuns.entity.RegisteredUser;

import java.util.List;
import java.util.Optional;

public interface RegisteredUserService {
    List<RegisteredUser> findAllRegistrovaniKorisnik();
    Optional<RegisteredUser> findById(Integer id);
    Optional<RegisteredUser> findByUsername(String username);
    RegisteredUser saveRegistrovaniKorisnik(RegisteredUser registeredUser);
    RegisteredUser updateRegistrovaniKorisnik(RegisteredUser registeredUser);
    void deleteRegistrovaniKorisnik(Integer id);
    List<String> getAllEmails();

    List<String> getAllUsernames();

    void register(RegisteredUser registeredUser, String activationToken);

    boolean activateAccount(String token);

    RegisteredUser proveriKorisnika(String email, String password);

    public boolean usernameExists(String username);

}
