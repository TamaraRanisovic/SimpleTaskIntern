package com.developer.onlybuns.dto.request;

public class SevenDaysReportDTO {
    private Integer newFollowersCount;

    private Integer newCommentsCount;

    private Integer newLikesCount;

    public SevenDaysReportDTO() {
    }

    public SevenDaysReportDTO(Integer newFollowersCount, Integer newCommentsCount, Integer newLikesCount) {
        this.newFollowersCount = newFollowersCount;
        this.newCommentsCount = newCommentsCount;
        this.newLikesCount = newLikesCount;
    }

    public Integer getNewFollowersCount() {
        return newFollowersCount;
    }

    public void setNewFollowersCount(Integer newFollowersCount) {
        this.newFollowersCount = newFollowersCount;
    }

    public Integer getNewCommentsCount() {
        return newCommentsCount;
    }

    public void setNewCommentsCount(Integer newCommentsCount) {
        this.newCommentsCount = newCommentsCount;
    }

    public Integer getNewLikesCount() {
        return newLikesCount;
    }

    public void setNewLikesCount(Integer newLikesCount) {
        this.newLikesCount = newLikesCount;
    }
}
