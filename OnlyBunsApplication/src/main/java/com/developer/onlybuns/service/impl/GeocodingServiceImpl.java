package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.request.LokacijaDTO;
import com.developer.onlybuns.service.GeocodingService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class GeocodingServiceImpl implements GeocodingService {
    private static final String GEOCODING_RESOURCE = "https://geocode.search.hereapi.com/v1/geocode";
    private static final String REVERSE_GEOCODING_RESOURCE = "https://revgeocode.search.hereapi.com/v1/revgeocode";

    private static final Integer RADIUS = 10;

    private static final Integer LIMIT = 3;

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Value("${here.api.key}")
    private String API_KEY;

    @Override
    public double[] getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        // Build the full URL for the API request
        String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_RESOURCE)
                .queryParam("q", address)
                .queryParam("apiKey", API_KEY)
                .toUriString();

        // Send the GET request and capture the response
        String response = restTemplate.getForObject(url, String.class);

        // Parse the response to extract latitude and longitude
        JSONObject jsonResponse = new JSONObject(response);
        double latitude = jsonResponse
                .getJSONArray("items")
                .getJSONObject(0)
                .getJSONObject("position")
                .getDouble("lat");
        double longitude = jsonResponse
                .getJSONArray("items")
                .getJSONObject(0)
                .getJSONObject("position")
                .getDouble("lng");

        return new double[]{latitude, longitude};
    }

    public LokacijaDTO getAddressDetails(String address) {

        String[] addressParts = address.split(",");

        String ulica = addressParts[0].trim(); // Street address
        String grad = addressParts[2].trim();  // City
        String drzava = addressParts[3].trim(); // Country

        return new LokacijaDTO(ulica, grad, drzava);
    }


    @Override
    public LokacijaDTO getAddress(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();

        // Build the full URL for the API request
        String url = UriComponentsBuilder.fromHttpUrl(REVERSE_GEOCODING_RESOURCE)
                .queryParam("at", latitude + "," + longitude)
                .queryParam("lang", "en-US")
                .queryParam("apiKey", API_KEY)
                .toUriString();

        // Send the GET request and capture the response
        String response = restTemplate.getForObject(url, String.class);

        // Parse the response to extract address components
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray items = jsonResponse.optJSONArray("items");

        if (items != null && !items.isEmpty()) {
            JSONObject firstItem = items.getJSONObject(0);
            JSONObject address = firstItem.optJSONObject("address");

            if (address != null) {
                String ulica = address.optString("street", "") + " " + address.optString("houseNumber", "");
                String grad = address.optString("city", "");
                String drzava = address.optString("countryName", "");

                return new LokacijaDTO(ulica, grad, drzava);
            }
        }

        return null;
    }


    @Override
    public String getUsersNearbyPosts(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(REVERSE_GEOCODING_RESOURCE)
                .queryParam("in", "circle:" + latitude + "," + longitude + ";r=" + RADIUS)
                .queryParam("limit", LIMIT)
                .queryParam("apiKey", API_KEY)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }


    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        return EARTH_RADIUS_KM * c;
    }
}

