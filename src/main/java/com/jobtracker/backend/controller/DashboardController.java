package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.response.DashboardResponse;
import com.jobtracker.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardResponse> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
    
}
