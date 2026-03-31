package com.jobtracker.backend.dto.response;

import com.jobtracker.backend.entity.Resume;
import java.time.LocalDateTime;

public class ResumeResponse {

    private Long id;
    private String fileName;
    private String extractedText;
    private boolean active;
    private LocalDateTime uploadedAt;

    public ResumeResponse() {}

    public ResumeResponse(Resume resume) {
        this.id = resume.getId();
        this.fileName = resume.getFileName();
        this.extractedText = resume.getExtractedText();
        this.active = resume.isActive();
        this.uploadedAt = resume.getUploadedAt();
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getExtractedText() { return extractedText; }
    public boolean isActive() { return active; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
    public void setActive(boolean active) { this.active = active; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}