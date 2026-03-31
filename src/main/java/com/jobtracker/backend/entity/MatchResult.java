package com.jobtracker.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_results")
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "match_score")
    private int matchScore;

    @Column(name = "matched_keywords", columnDefinition = "TEXT")
    private String matchedKeywords;

    @Column(name = "missing_keywords", columnDefinition = "TEXT")
    private String missingKeywords;

    @Column(name = "ai_suggestions", columnDefinition = "TEXT")
    private String aiSuggestions;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public MatchResult() {}

    public Long getId() { return id; }
    public JobApplication getApplication() { return application; }
    public Resume getResume() { return resume; }
    public int getMatchScore() { return matchScore; }
    public String getMatchedKeywords() { return matchedKeywords; }
    public String getMissingKeywords() { return missingKeywords; }
    public String getAiSuggestions() { return aiSuggestions; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setApplication(JobApplication application) { this.application = application; }
    public void setResume(Resume resume) { this.resume = resume; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
    public void setMatchedKeywords(String matchedKeywords) { this.matchedKeywords = matchedKeywords; }
    public void setMissingKeywords(String missingKeywords) { this.missingKeywords = missingKeywords; }
    public void setAiSuggestions(String aiSuggestions) { this.aiSuggestions = aiSuggestions; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}