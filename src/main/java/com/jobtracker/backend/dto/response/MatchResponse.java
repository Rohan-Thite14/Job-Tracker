package com.jobtracker.backend.dto.response;

import com.jobtracker.backend.entity.MatchResult;
import java.time.LocalDateTime;
import java.util.List;

public class MatchResponse {

    private Long id;
    private int matchScore;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private List<String> aiSuggestions;
    private LocalDateTime createdAt;

    public MatchResponse() {}

    public MatchResponse(MatchResult result) {
        this.id = result.getId();
        this.matchScore = result.getMatchScore();
        this.matchedKeywords = parseList(result.getMatchedKeywords());
        this.missingKeywords = parseList(result.getMissingKeywords());
        this.aiSuggestions = parseList(result.getAiSuggestions());
        this.createdAt = result.getCreatedAt();
    }

    private List<String> parseList(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return List.of(csv.split("\\|"));
    }

    public Long getId() { return id; }
    public int getMatchScore() { return matchScore; }
    public List<String> getMatchedKeywords() { return matchedKeywords; }
    public List<String> getMissingKeywords() { return missingKeywords; }
    public List<String> getAiSuggestions() { return aiSuggestions; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
    public void setMatchedKeywords(List<String> matchedKeywords) { this.matchedKeywords = matchedKeywords; }
    public void setMissingKeywords(List<String> missingKeywords) { this.missingKeywords = missingKeywords; }
    public void setAiSuggestions(List<String> aiSuggestions) { this.aiSuggestions = aiSuggestions; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}