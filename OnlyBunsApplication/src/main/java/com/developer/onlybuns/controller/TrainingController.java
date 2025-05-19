package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.request.TrainingDTO;
import com.developer.onlybuns.entity.Training;
import com.developer.onlybuns.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/trainings")
public class TrainingController {
    private final TrainingService trainingService;


    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public List<Training> findAll() {
        return trainingService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Training> findById(@PathVariable("id") Integer id) {
        return trainingService.findById(id);
    }

    @PostMapping("/save")
    public Training saveTraining(@RequestBody Training training) {
        return trainingService.saveTraining(training);
    }

    @GetMapping("/day")
    public List<TrainingDTO> getTrainingsForDay(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return trainingService.getTrainingsForDay(date);
    }

    @GetMapping("/week")
    public List<TrainingDTO> getTrainingsForWeek(
            @RequestParam("startOfWeek") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startOfWeek) {
        return trainingService.getTrainingsForWeek(startOfWeek);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookTraining(@RequestParam Integer trainingId,
                                               @RequestParam String username) {
        try {
            trainingService.bookTraining(trainingId, username);
            return ResponseEntity.ok("Training booked successfully!");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Booking failed: " + ex.getMessage());
        }
    }


    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestParam Integer trainingId,
                                                @RequestParam String username) {
        try {
            trainingService.cancelBooking(trainingId, username);
            return ResponseEntity.ok("Training booking cancelled successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/booked")
    public ResponseEntity<List<TrainingDTO>> getBookedTrainings(@RequestParam String username) {
        try {
            List<TrainingDTO> bookedTrainings = trainingService.getBookedTrainingsForUser(username);
            return ResponseEntity.ok(bookedTrainings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
