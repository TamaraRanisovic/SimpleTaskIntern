package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.request.LokacijaDTO;
import com.developer.onlybuns.service.GeocodingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/geocode")
public class GeocodingController {
    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @PostMapping("/get-coordinates")
    public ResponseEntity<?> getCoordinates(@RequestBody LokacijaDTO lokacijaDTO) {
        // Combine the fields of LokacijaDTO to form the address
        String address = lokacijaDTO.getUlica() + ", " + lokacijaDTO.getGrad() + ", " + lokacijaDTO.getDrzava();

        // Get the coordinates from the geocoding service
        double[] coordinates = geocodingService.getCoordinates(address);

        // If coordinates are found, return them
        if (coordinates != null) {
            Map<String, Double> response = new HashMap<>();
            response.put("latitude", coordinates[0]);
            response.put("longitude", coordinates[1]);
            return ResponseEntity.ok(response);
        }

        // Return bad request if coordinates are not found
        return ResponseEntity.badRequest().body("Invalid address");
    }

    @GetMapping("/get-address")
    public ResponseEntity<Object> getAddress(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            // Check if latitude and longitude are valid
            if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                return new ResponseEntity<>("Invalid latitude or longitude", HttpStatus.BAD_REQUEST);
            }

            // Call the geocoding service to get the address
            LokacijaDTO lokacijaDTO = geocodingService.getAddress(latitude, longitude);

            // Return the LokacijaDTO as the response body with 200 OK status
            return new ResponseEntity<>(lokacijaDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Handle invalid argument error, if necessary
            return new ResponseEntity<>("Invalid latitude or longitude", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Catch all other exceptions and return a generic error message
            return new ResponseEntity<>("Error occurred while fetching address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/distance")
    public double getDistance(
            @RequestParam double lat1,
            @RequestParam double lon1,
            @RequestParam double lat2,
            @RequestParam double lon2) {
        return geocodingService.calculateDistance(lat1, lon1, lat2, lon2);
    }

}