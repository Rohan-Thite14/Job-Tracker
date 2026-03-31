package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.request.ApplicationRequest;
import com.jobtracker.backend.dto.response.ApplicationResponse;
import com.jobtracker.backend.entity.JobApplication;
import com.jobtracker.backend.entity.User;
import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.repository.ApplicationRepository;
import com.jobtracker.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("rohan@test.com");
        mockUser.setFullName("Rohan Thite");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("rohan@test.com");
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        when(userRepository.findByEmail("rohan@test.com"))
                .thenReturn(Optional.of(mockUser));
    }

    @Test
    void create_Success() {
        ApplicationRequest request = new ApplicationRequest();
        request.setCompanyName("Google");
        request.setRoleTitle("Java Backend Intern");
        request.setStatus(JobApplication.Status.APPLIED);

        JobApplication saved = new JobApplication();
        saved.setId(1L);
        saved.setUser(mockUser);
        saved.setCompanyName("Google");
        saved.setRoleTitle("Java Backend Intern");
        saved.setStatus(JobApplication.Status.APPLIED);

        when(applicationRepository.save(any(JobApplication.class))).thenReturn(saved);

        ApplicationResponse response = applicationService.create(request);

        assertNotNull(response);
        assertEquals("Google", response.getCompanyName());
        assertEquals("Java Backend Intern", response.getRoleTitle());
        assertEquals(JobApplication.Status.APPLIED, response.getStatus());
        verify(applicationRepository).save(any(JobApplication.class));
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(applicationRepository.findByIdAndUser(eq(99L), eq(mockUser)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.getById(99L));
    }

    @Test
    void delete_Success() {
        JobApplication app = new JobApplication();
        app.setId(1L);
        app.setUser(mockUser);

        when(applicationRepository.findByIdAndUser(1L, mockUser))
                .thenReturn(Optional.of(app));

        applicationService.delete(1L);

        verify(applicationRepository).delete(app);
    }

    @Test
    void getAll_WithStatusFilter_ReturnsFiltered() {
        JobApplication app = new JobApplication();
        app.setId(1L);
        app.setUser(mockUser);
        app.setCompanyName("Google");
        app.setRoleTitle("Backend Intern");
        app.setStatus(JobApplication.Status.APPLIED);

        Page<JobApplication> page = new PageImpl<>(List.of(app));

        when(applicationRepository.findByUserAndStatus(
                eq(mockUser), eq(JobApplication.Status.APPLIED), any(Pageable.class)))
                .thenReturn(page);

        Page<ApplicationResponse> result =
                applicationService.getAll(0, 10, "APPLIED");

        assertEquals(1, result.getTotalElements());
        assertEquals("Google", result.getContent().get(0).getCompanyName());
    }
}