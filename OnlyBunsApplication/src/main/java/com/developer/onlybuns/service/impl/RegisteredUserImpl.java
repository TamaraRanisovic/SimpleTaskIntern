package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.RegisteredUser;
import com.developer.onlybuns.enums.Role;
import com.developer.onlybuns.repository.RegisteredUserRepository;
import com.developer.onlybuns.service.RegisteredUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RegisteredUserImpl implements RegisteredUserService {

    private final RegisteredUserRepository registeredUserRepository;



    @Autowired
    private JavaMailSender mailSender;

    public RegisteredUserImpl(RegisteredUserRepository registeredUserRepository) {
        this.registeredUserRepository = registeredUserRepository;
    }

    @Override
    public List<RegisteredUser> findAllRegistrovaniKorisnik() {
        return registeredUserRepository.findAll();
    }

    @Override
    public Optional<RegisteredUser> findById(Integer id) {
        return registeredUserRepository.findById(id);
    }

    @Override
    public Optional<RegisteredUser> findByUsername(String username) {
        return registeredUserRepository.findByUsername(username);
    }

    @Transactional
    public void register(RegisteredUser registeredUser, String activationToken) {
        Logger log = LoggerFactory.getLogger(getClass());

        log.info("Starting user registration for email: {}", registeredUser.getEmail());

        RegisteredUser noviKorisnik = new RegisteredUser();
        noviKorisnik.setUsername(registeredUser.getUsername());
        noviKorisnik.setEmail(registeredUser.getEmail());
        noviKorisnik.setPassword(registeredUser.getPassword());
        noviKorisnik.setName(registeredUser.getName());
        noviKorisnik.setSurname(registeredUser.getSurname());
        noviKorisnik.setPhoneNumber(registeredUser.getPhoneNumber());
        noviKorisnik.setUloga(Role.REGISTROVANI_KORISNIK);
        noviKorisnik.setActivationToken(activationToken);
        noviKorisnik.setVerified(false);

        // Save user
        registeredUserRepository.save(noviKorisnik);
        log.info("User successfully registered: {}", noviKorisnik.getEmail());
    }



    public boolean activateAccount(String token) {
        Optional<RegisteredUser> registrovaniKorisnik = registeredUserRepository.findByActivationToken(token);
        if (registrovaniKorisnik != null) {
            registrovaniKorisnik.get().setVerified(true); // Activate account
            registrovaniKorisnik.get().setActivationToken(null); // Clear the token after activation
            registeredUserRepository.save(registrovaniKorisnik.get());
            return true;
        }
        return false;
    }

    @Override
    public RegisteredUser saveRegistrovaniKorisnik(RegisteredUser registeredUserEntity) {
        return registeredUserRepository.save(registeredUserEntity);
    }

    @Override
    public RegisteredUser updateRegistrovaniKorisnik(RegisteredUser registeredUserEntity) {
        return registeredUserRepository.save(registeredUserEntity);
    }

    @Override
    public void deleteRegistrovaniKorisnik(Integer id) {
        registeredUserRepository.deleteById(id);
    }

    @Override
    public List<String> getAllEmails() {
        return registeredUserRepository.findAllEmails();
    }

    @Override
    public List<String> getAllUsernames() {
        return registeredUserRepository.findAllUsernames();
    }

    @Override
    public boolean usernameExists(String username) {
        List<String> allUsernames = registeredUserRepository.findAllUsernames();
        if (allUsernames.contains(username)) {
            return true;
        }
        return false;
    }



    @Override
    public RegisteredUser proveriKorisnika(String email, String password) {
        RegisteredUser korisnik = registeredUserRepository.findByEmailAndPassword(email, password);
        return korisnik;
    }


    private void sendActivationEmail(String email, String token) {
        String activationLink = "http://localhost:8080/registrovaniKorisnik/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ranisovic.in1.2020@uns.ac.rs");
        message.setTo(email);
        message.setSubject("Activate your account");
        message.setText("Click the following link to activate your account: " + activationLink);
        mailSender.send(message);
    }


    public class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

}
