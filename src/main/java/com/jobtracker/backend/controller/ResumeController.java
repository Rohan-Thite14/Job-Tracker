package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.response.ResumeResponse;
import com.jobtracker.backend.service.ResumeService;

import org.apache.tika.exception.TikaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponse> upload(
            @RequestParam("file") MultipartFile file) throws IOException, TikaException {
        return ResponseEntity.ok(resumeService.upload(file));
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getAll() {
        return ResponseEntity.ok(resumeService.getAllForUser());
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ResumeResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.activate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resumeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<ResumeResponse> getActive() {
        return resumeService.getActiveResume()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}