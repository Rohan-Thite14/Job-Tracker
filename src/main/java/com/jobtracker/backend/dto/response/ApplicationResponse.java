package com.jobtracker.backend.dto.response;

import com.jobtracker.backend.entity.JobApplication;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private String companyName;
    private String roleTitle;
    private String jobDescription;
    private JobApplication.Status status;
    private LocalDate appliedDate;
    private LocalDate deadline;
    private String jobUrl;
    private String notes;
    private LocalDateTime createdAt;

    public ApplicationResponse() {}

    public ApplicationResponse(JobApplication app) {
        this.id = app.getId();
        this.companyName = app.getCompanyName();
        this.roleTitle = app.getRoleTitle();
        this.jobDescription = app.getJobDescription();
        this.status = app.getStatus();
        this.appliedDate = app.getAppliedDate();
        this.deadline = app.getDeadline();
        this.jobUrl = app.getJobUrl();
        this.notes = app.getNotes();
        this.createdAt = app.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getCompanyName() { return companyName; }
    public String getRoleTitle() { return roleTitle; }
    public String getJobDescription() { return jobDescription; }
    public JobApplication.Status getStatus() { return status; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public LocalDate getDeadline() { return deadline; }
    public String getJobUrl() { return jobUrl; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public void setStatus(JobApplication.Status status) { this.status = status; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}