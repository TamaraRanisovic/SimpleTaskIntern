package com.developer.onlybuns.service.impl;
import com.developer.onlybuns.service.RateLimiterService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterServiceImpl implements RateLimiterService {

    private final ConcurrentHashMap<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
    private final RateLimiterRegistry rateLimiterRegistry;


    public RateLimiterServiceImpl() {
        // Default RateLimiter config: max 5 calls in 3 minutes
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(0)) // No waiting allowed
                .build();

        this.rateLimiterRegistry = RateLimiterRegistry.of(config);
    }

    @Override
    public boolean isRateLimited(String ipAddress) {
        RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(ipAddress, ip ->
                rateLimiterRegistry.rateLimiter(ip)
        );
        return !rateLimiter.acquirePermission();
    }

    public Map<String, RateLimiter> listAllRateLimiters() {
        return Collections.unmodifiableMap(rateLimiterMap);
    }

}
