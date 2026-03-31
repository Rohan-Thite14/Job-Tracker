package com.jobtracker.backend.service;

import com.jobtracker.backend.entity.JobApplication;
import com.jobtracker.backend.repository.ApplicationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final ApplicationRepository applicationRepository;
    private final JavaMailSender mailSender;

    public NotificationService(ApplicationRepository applicationRepository,
                               JavaMailSender mailSender) {
        this.applicationRepository = applicationRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDeadlineAlerts() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<JobApplication> upcoming = applicationRepository
                .findAllByDeadline(tomorrow);

        for (JobApplication app : upcoming) {
            sendAlert(app);
        }
    }

    private void sendAlert(JobApplication app) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(app.getUser().getEmail());
            message.setSubject("Deadline tomorrow: " + app.getRoleTitle()
                    + " at " + app.getCompanyName());
            message.setText(
                "Hi " + app.getUser().getFullName() + ",\n\n" +
                "This is a reminder that your application deadline is tomorrow!\n\n" +
                "Role: " + app.getRoleTitle() + "\n" +
                "Company: " + app.getCompanyName() + "\n" +
                "Deadline: " + app.getDeadline() + "\n\n" +
                "Good luck!\n" +
                "Job Tracker"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send alert for application "
                    + app.getId() + ": " + e.getMessage());
        }
    }
}