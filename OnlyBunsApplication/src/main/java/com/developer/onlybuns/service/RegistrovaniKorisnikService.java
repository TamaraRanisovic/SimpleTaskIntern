package com.developer.onlybuns.service;
import com.developer.onlybuns.dto.request.ObjavaDTO;
import com.developer.onlybuns.dto.request.RegistrovaniKorisnikDTO;
import com.developer.onlybuns.dto.request.SevenDaysReportDTO;
import com.developer.onlybuns.entity.Pratioci;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistrovaniKorisnikService {
    List<RegistrovaniKorisnik> findAllRegistrovaniKorisnik();
    Optional<RegistrovaniKorisnik> findById(Integer id);

    RegistrovaniKorisnikDTO getKorisnikDTOByUsername(String username);


    Optional<RegistrovaniKorisnik> findByUsername(String username);

    RegistrovaniKorisnik saveRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);
    RegistrovaniKorisnik updateRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);
    void deleteRegistrovaniKorisnik(Integer id);
    List<String> getAllEmails();

    List<String> getAllUsernames();

    List<String> getAllFollowers(String username);

    List<String> getAllFollowing(String username);

    void register(RegistrovaniKorisnik registrovaniKorisnik, String activationToken);

    boolean activateAccount(String token);

    RegistrovaniKorisnik proveriKorisnika(String email, String password);

    public boolean usernameExists(String username);

    public int getNewFollowersCount(String username, LocalDateTime fromDate);

    public SevenDaysReportDTO generateSevenDaysReport(String username, LocalDateTime lastLogin);

    public List<String> findInactiveUsers(LocalDateTime fromDate);

    List<ObjavaDTO> findAllUserFollows(String username);

    void sendNotificationsToInactiveUsers();

    void sendSevenDaysReportEmail(String email, String username, SevenDaysReportDTO sevenDaysReportDTO);
}
