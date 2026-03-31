package com.jobtracker.backend.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private long totalApplications;
    private long savedCount;
    private long appliedCount;
    private long interviewCount;
    private long offerCount;
    private long rejectedCount;
    private double interviewRate;
    private double averageMatchScore;
    private List<Map<String, Object>> weeklyTimeline;

    public DashboardResponse() {}

    public long getTotalApplications() { return totalApplications; }
    public long getSavedCount() { return savedCount; }
    public long getAppliedCount() { return appliedCount; }
    public long getInterviewCount() { return interviewCount; }
    public long getOfferCount() { return offerCount; }
    public long getRejectedCount() { return rejectedCount; }
    public double getInterviewRate() { return interviewRate; }
    public double getAverageMatchScore() { return averageMatchScore; }
    public List<Map<String, Object>> getWeeklyTimeline() { return weeklyTimeline; }

    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
    public void setSavedCount(long savedCount) { this.savedCount = savedCount; }
    public void setAppliedCount(long appliedCount) { this.appliedCount = appliedCount; }
    public void setInterviewCount(long interviewCount) { this.interviewCount = interviewCount; }
    public void setOfferCount(long offerCount) { this.offerCount = offerCount; }
    public void setRejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; }
    public void setInterviewRate(double interviewRate) { this.interviewRate = interviewRate; }
    public void setAverageMatchScore(double averageMatchScore) { this.averageMatchScore = averageMatchScore; }
    public void setWeeklyTimeline(List<Map<String, Object>> weeklyTimeline) { this.weeklyTimeline = weeklyTimeline; }
}