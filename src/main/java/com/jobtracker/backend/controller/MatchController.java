package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.request.MatchRequest;
import com.jobtracker.backend.dto.response.MatchResponse;
import com.jobtracker.backend.service.AiMatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@CrossOrigin(origins = "http://localhost:5173")
public class MatchController {

    private final AiMatchService aiMatchService;

    public MatchController(AiMatchService aiMatchService) {
        this.aiMatchService = aiMatchService;
    }

    @PostMapping("/score")
    public ResponseEntity<MatchResponse> score(
            @RequestBody MatchRequest request) throws Exception {
        return ResponseEntity.ok(aiMatchService.score(request));
    }

    @GetMapping("/history/{applicationId}")
    public ResponseEntity<List<MatchResponse>> history(
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(aiMatchService.getHistory(applicationId));
    }
}
