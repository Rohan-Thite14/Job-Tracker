package com.jobtracker.backend.dto.request;

import com.jobtracker.backend.entity.JobApplication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class ApplicationRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Role title is required")
    @Size(max = 200, message = "Role title must be under 200 characters")
    private String roleTitle;

    @Size(max = 10000, message = "Job description too long")
    private String jobDescription;

    private JobApplication.Status status;
    private LocalDate appliedDate;
    private LocalDate deadline;

    @Size(max = 500, message = "URL too long")
    private String jobUrl;

    @Size(max = 5000, message = "Notes too long")
    private String notes;

    public String getCompanyName() { return companyName; }
    public String getRoleTitle() { return roleTitle; }
    public String getJobDescription() { return jobDescription; }
    public JobApplication.Status getStatus() { return status; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public LocalDate getDeadline() { return deadline; }
    public String getJobUrl() { return jobUrl; }
    public String getNotes() { return notes; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public void setStatus(JobApplication.Status status) { this.status = status; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }
    public void setNotes(String notes) { this.notes = notes; }
}