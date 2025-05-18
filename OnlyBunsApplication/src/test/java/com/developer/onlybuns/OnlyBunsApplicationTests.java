package com.developer.onlybuns;

import com.developer.onlybuns.controller.RegistrovaniKorisnikController;
import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.Pratioci;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.enums.Uloga;
import com.developer.onlybuns.repository.KorisnikRepository;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class OnlyBunsApplicationTests {

	@Autowired
	private RegistrovaniKorisnikService registrovaniKorisnikService;

	@Autowired
	private RegistrovaniKorisnikRepository registrovaniKorisnikRepository;

	@Autowired
	private RegistrovaniKorisnikController registrovaniKorisnikController;

	@Test
	@Transactional
	void testConcurrentRegistration() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		// Define two tasks trying to register the same username
		List<Objava> objave = new ArrayList<Objava>();
		List<Komentar> komentari = new ArrayList<Komentar>();
		List<Pratioci> followers = new ArrayList<Pratioci>();
		List<Pratioci> following = new ArrayList<Pratioci>();

		RegistrovaniKorisnik korisnik1 = new RegistrovaniKorisnik("testUser", "test@gmail.com", "test", "test name", "test lastname", "test street", "test city", "test state", "1234567890", Uloga.REGISTROVANI_KORISNIK, false, objave, komentari, following, followers);
		RegistrovaniKorisnik korisnik2 = new RegistrovaniKorisnik("testUser", "test2@gmail.com", "test2", "test name2", "test lastname2", "test street2", "test city2", "test state2", "1234567800", Uloga.REGISTROVANI_KORISNIK, false, objave, komentari, following, followers);

		Runnable task1 = () -> {
			try {
				registrovaniKorisnikController.register(korisnik1);
				System.out.println("Task 1: User registered successfully.");
			} catch (Exception e) {
				System.out.println("Task 1: " + e.getMessage());
			}
		};

		Runnable task2 = () -> {
			try {
				registrovaniKorisnikController.register(korisnik2);
				System.out.println("Task 2: User registered successfully.");
			} catch (Exception e) {
				System.out.println("Task 2: " + e.getMessage());
			}
		};

		// Run both tasks concurrently
		executor.submit(task1);
		executor.submit(task2);

		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);


		Integer userCount = registrovaniKorisnikRepository.countByKorisnickoIme("testUser");

		System.out.println("User count with username 'testUser': " + userCount);

		// Ensure only one user with the username 'testUser' exists
		assertEquals(1, userCount, "Expected exactly one user to be saved.");
	}

}
