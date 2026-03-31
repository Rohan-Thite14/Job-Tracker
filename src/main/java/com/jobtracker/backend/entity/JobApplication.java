package com.jobtracker.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    public enum Status {
        SAVED, APPLIED, INTERVIEW, OFFER, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "role_title", nullable = false)
    private String roleTitle;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SAVED;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    private LocalDate deadline;

    @Column(name = "job_url")
    private String jobUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public JobApplication() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getCompanyName() { return companyName; }
    public String getRoleTitle() { return roleTitle; }
    public String getJobDescription() { return jobDescription; }
    public Status getStatus() { return status; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public LocalDate getDeadline() { return deadline; }
    public String getJobUrl() { return jobUrl; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public void setStatus(Status status) { this.status = status; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}