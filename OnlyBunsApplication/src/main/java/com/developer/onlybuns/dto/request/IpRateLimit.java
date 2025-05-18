package com.developer.onlybuns.dto.request;

public class IpRateLimit {
    private long timestamp;
    private int attempts;

    public IpRateLimit() {
    }
    public IpRateLimit(long timestamp, int attempts) {
        this.timestamp = timestamp;
        this.attempts = attempts;
    }



    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}