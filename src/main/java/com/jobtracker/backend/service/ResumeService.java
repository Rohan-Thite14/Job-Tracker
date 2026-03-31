package com.jobtracker.backend.service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import com.jobtracker.backend.dto.response.ResumeResponse;
import com.jobtracker.backend.entity.Resume;
import com.jobtracker.backend.entity.User;
import com.jobtracker.backend.repository.ResumeRepository;
import com.jobtracker.backend.repository.UserRepository;
import org.apache.tika.Tika;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jobtracker.backend.exception.ResourceNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final Tika tika = new Tika();

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Transactional
    public ResumeResponse upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        String extractedText = tika.parseToString(file.getInputStream());

        User user = getCurrentUser();

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFileName(file.getOriginalFilename());
        resume.setExtractedText(extractedText);
        resume.setActive(false);

        return new ResumeResponse(resumeRepository.save(resume));
    }

    public List<ResumeResponse> getAllForUser() {
        User user = getCurrentUser();
        return resumeRepository.findByUser(user)
                .stream()
                .map(ResumeResponse::new)
                .collect(Collectors.toList());
    }

    public ResumeResponse activate(Long id) {
        User user = getCurrentUser();

        Resume resume = resumeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        resumeRepository.deactivateAllForUser(user);
        resume.setActive(true);
        return new ResumeResponse(resumeRepository.save(resume));
    }

    public void delete(Long id) {
        User user = getCurrentUser();
        Resume resume = resumeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        resumeRepository.delete(resume);
    }

    public Optional<ResumeResponse> getActiveResume() {
        User user = getCurrentUser();
        return resumeRepository.findByUserAndActiveTrue(user)
                .map(ResumeResponse::new);
    }
}