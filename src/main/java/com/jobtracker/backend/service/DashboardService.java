package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.response.DashboardResponse;
import com.jobtracker.backend.entity.JobApplication;
import com.jobtracker.backend.entity.User;
import com.jobtracker.backend.repository.ApplicationRepository;
import com.jobtracker.backend.repository.MatchResultRepository;
import com.jobtracker.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final ApplicationRepository applicationRepository;
    private final MatchResultRepository matchResultRepository;
    private final UserRepository userRepository;

    public DashboardService(ApplicationRepository applicationRepository,
                            MatchResultRepository matchResultRepository,
                            UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.matchResultRepository = matchResultRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public DashboardResponse getStats() {
        User user = getCurrentUser();

        long saved = applicationRepository.countByUserAndStatus(user, JobApplication.Status.SAVED);
        long applied = applicationRepository.countByUserAndStatus(user, JobApplication.Status.APPLIED);
        long interview = applicationRepository.countByUserAndStatus(user, JobApplication.Status.INTERVIEW);
        long offer = applicationRepository.countByUserAndStatus(user, JobApplication.Status.OFFER);
        long rejected = applicationRepository.countByUserAndStatus(user, JobApplication.Status.REJECTED);
        long total = saved + applied + interview + offer + rejected;

        double interviewRate = applied > 0
                ? Math.round((double) interview / applied * 100 * 10.0) / 10.0
                : 0.0;

        Double avgScore = matchResultRepository.findAverageScoreByUser(user);
        double averageMatchScore = avgScore != null
                ? Math.round(avgScore * 10.0) / 10.0
                : 0.0;

        List<Object[]> weeklyRaw = applicationRepository
                .findWeeklyApplicationCounts(user.getId());
        List<Map<String, Object>> weeklyTimeline = new ArrayList<>();
        for (Object[] row : weeklyRaw) {
            Map<String, Object> point = new HashMap<>();
            point.put("week", row[0]);
            point.put("count", row[1]);
            weeklyTimeline.add(point);
        }

        DashboardResponse response = new DashboardResponse();
        response.setTotalApplications(total);
        response.setSavedCount(saved);
        response.setAppliedCount(applied);
        response.setInterviewCount(interview);
        response.setOfferCount(offer);
        response.setRejectedCount(rejected);
        response.setInterviewRate(interviewRate);
        response.setAverageMatchScore(averageMatchScore);
        response.setWeeklyTimeline(weeklyTimeline);

        return response;
    }
}