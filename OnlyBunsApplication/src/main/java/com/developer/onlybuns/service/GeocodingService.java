package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.request.LokacijaDTO;

public interface GeocodingService {

    double[] getCoordinates(String address);

    LokacijaDTO getAddress(double latitude, double longitude) throws Exception;

    String getUsersNearbyPosts(double latitude, double longitude);

    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}
