package com.developer.onlybuns.service;

import io.github.resilience4j.ratelimiter.RateLimiter;

import java.util.Map;

public interface RateLimiterService {
    public boolean isRateLimited(String ipAddress);
    public Map<String, RateLimiter> listAllRateLimiters();

}
