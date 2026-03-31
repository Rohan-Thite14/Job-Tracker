package com.jobtracker.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.backend.dto.request.MatchRequest;
import com.jobtracker.backend.dto.response.MatchResponse;
import com.jobtracker.backend.entity.JobApplication;
import com.jobtracker.backend.entity.MatchResult;
import com.jobtracker.backend.entity.Resume;
import com.jobtracker.backend.entity.User;
import com.jobtracker.backend.repository.ApplicationRepository;
import com.jobtracker.backend.repository.MatchResultRepository;
import com.jobtracker.backend.repository.ResumeRepository;
import com.jobtracker.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiMatchService {

    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final MatchResultRepository matchResultRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${app.gemini.api-key}")
    private String geminiApiKey;

    @Value("${app.gemini.url}")
    private String geminiUrl;

    public AiMatchService(ApplicationRepository applicationRepository,
                          ResumeRepository resumeRepository,
                          MatchResultRepository matchResultRepository,
                          UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.resumeRepository = resumeRepository;
        this.matchResultRepository = matchResultRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public MatchResponse score(MatchRequest request) throws Exception {
        User user = getCurrentUser();

        JobApplication application = applicationRepository
                .findByIdAndUser(request.getApplicationId(), user)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Resume resume = resumeRepository
                .findByIdAndUser(request.getResumeId(), user)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        String jd = (request.getJobDescriptionOverride() != null
                && !request.getJobDescriptionOverride().isBlank())
                ? request.getJobDescriptionOverride()
                : application.getJobDescription();

        if (jd == null || jd.isBlank()) {
            throw new RuntimeException("No job description available to match against");
        }

        String prompt = buildPrompt(resume.getExtractedText(), jd);
        String geminiResponse = callGemini(prompt);
        return parseAndSave(geminiResponse, application, resume);
    }

    private String buildPrompt(String resumeText, String jdText) {
        return """
                You are an expert resume-job description matcher.
                Analyze the resume and job description below and respond ONLY with a valid JSON object.
                Do not include any explanation, markdown, or code blocks. Just raw JSON.
                
                JSON format:
                {
                  "score": <integer 0-100>,
                  "matched_keywords": [<list of matching skills/keywords>],
                  "missing_keywords": [<list of important missing skills>],
                  "suggestions": [<list of exactly 3 specific improvement tips>]
                }
                
                RESUME:
                %s
                
                JOB DESCRIPTION:
                %s
                """.formatted(
                resumeText.substring(0, Math.min(resumeText.length(), 3000)),
                jdText.substring(0, Math.min(jdText.length(), 2000))
        );
    }

    private String callGemini(String prompt) throws Exception {
        String requestBody = """
                {
                  "contents": [{
                    "parts": [{"text": %s}]
                  }]
                }
                """.formatted(objectMapper.writeValueAsString(prompt));

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(geminiUrl + "?key=" + geminiApiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(
                httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API error: " + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());
        return root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();
    }

    private MatchResponse parseAndSave(String rawJson, JobApplication application,
                                        Resume resume) throws Exception {
        String cleaned = rawJson.trim()
                .replaceAll("^```json\\s*", "")
                .replaceAll("^```\\s*", "")
                .replaceAll("```$", "")
                .trim();

        JsonNode json = objectMapper.readTree(cleaned);

        int score = json.path("score").asInt();

        String matched = streamToString(json.path("matched_keywords"));
        String missing = streamToString(json.path("missing_keywords"));
        String suggestions = streamToString(json.path("suggestions"));

        MatchResult result = new MatchResult();
        result.setApplication(application);
        result.setResume(resume);
        result.setMatchScore(score);
        result.setMatchedKeywords(matched);
        result.setMissingKeywords(missing);
        result.setAiSuggestions(suggestions);

        return new MatchResponse(matchResultRepository.save(result));
    }

    private String streamToString(JsonNode arrayNode) {
        if (!arrayNode.isArray()) return "";
        StringBuilder sb = new StringBuilder();
        arrayNode.forEach(node -> {
            if (sb.length() > 0) sb.append("|");
            sb.append(node.asText());
        });
        return sb.toString();
    }

    public List<MatchResponse> getHistory(Long applicationId) {
        User user = getCurrentUser();
        JobApplication application = applicationRepository
                .findByIdAndUser(applicationId, user)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        return matchResultRepository
                .findByApplicationOrderByCreatedAtDesc(application)
                .stream()
                .map(MatchResponse::new)
                .collect(Collectors.toList());
    }
}