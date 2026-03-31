package com.jobtracker.backend.service;
import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.dto.request.ApplicationRequest;
import com.jobtracker.backend.dto.response.ApplicationResponse;
import com.jobtracker.backend.entity.JobApplication;
import com.jobtracker.backend.entity.User;
import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.repository.ApplicationRepository;
import com.jobtracker.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                               UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Page<ApplicationResponse> getAll(int page, int size, String status) {
        User user = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        if (status != null && !status.isBlank()) {
            JobApplication.Status s = JobApplication.Status.valueOf(status.toUpperCase());
            return applicationRepository
                    .findByUserAndStatus(user, s, pageable)
                    .map(ApplicationResponse::new);
        }
        return applicationRepository
                .findByUser(user, pageable)
                .map(ApplicationResponse::new);
    }

    public ApplicationResponse getById(Long id) {
        User user = getCurrentUser();
        JobApplication app = applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        return new ApplicationResponse(app);
    }

    public ApplicationResponse create(ApplicationRequest request) {
        User user = getCurrentUser();

        JobApplication app = new JobApplication();
        app.setUser(user);
        app.setCompanyName(request.getCompanyName());
        app.setRoleTitle(request.getRoleTitle());
        app.setJobDescription(request.getJobDescription());
        app.setStatus(request.getStatus() != null
                ? request.getStatus() : JobApplication.Status.SAVED);
        app.setAppliedDate(request.getAppliedDate());
        app.setDeadline(request.getDeadline());
        app.setJobUrl(request.getJobUrl());
        app.setNotes(request.getNotes());

        return new ApplicationResponse(applicationRepository.save(app));
    }

    public ApplicationResponse update(Long id, ApplicationRequest request) {
        User user = getCurrentUser();
        JobApplication app = applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (request.getCompanyName() != null) app.setCompanyName(request.getCompanyName());
        if (request.getRoleTitle() != null) app.setRoleTitle(request.getRoleTitle());
        if (request.getJobDescription() != null) app.setJobDescription(request.getJobDescription());
        if (request.getStatus() != null) app.setStatus(request.getStatus());
        if (request.getAppliedDate() != null) app.setAppliedDate(request.getAppliedDate());
        if (request.getDeadline() != null) app.setDeadline(request.getDeadline());
        if (request.getJobUrl() != null) app.setJobUrl(request.getJobUrl());
        if (request.getNotes() != null) app.setNotes(request.getNotes());

        return new ApplicationResponse(applicationRepository.save(app));
    }

    public void delete(Long id) {
        User user = getCurrentUser();
        JobApplication app = applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        applicationRepository.delete(app);
    }
}