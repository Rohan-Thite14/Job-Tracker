package com.jobtracker.backend.dto.request;

public class MatchRequest {

    private Long applicationId;
    private Long resumeId;
    private String jobDescriptionOverride;

    public Long getApplicationId() { return applicationId; }
    public Long getResumeId() { return resumeId; }
    public String getJobDescriptionOverride() { return jobDescriptionOverride; }

    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }
    public void setJobDescriptionOverride(String jobDescriptionOverride) {
        this.jobDescriptionOverride = jobDescriptionOverride;
    }
}