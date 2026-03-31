package com.jobtracker.backend.repository;

import com.jobtracker.backend.entity.JobApplication;
import com.jobtracker.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByUser(User user, Pageable pageable);

    Page<JobApplication> findByUserAndStatus(
            User user, JobApplication.Status status, Pageable pageable);

    Optional<JobApplication> findByIdAndUser(Long id, User user);

    long countByUserAndStatus(User user, JobApplication.Status status);

    List<JobApplication> findByUser(User user);

    @Query("SELECT a FROM JobApplication a WHERE a.deadline = :tomorrow")
    List<JobApplication> findAllByDeadline(@Param("tomorrow") LocalDate tomorrow);

    @Query(value = """
            SELECT YEARWEEK(applied_date, 1) as week,
                   COUNT(*) as count
            FROM job_applications
            WHERE user_id = :userId
              AND applied_date IS NOT NULL
            GROUP BY YEARWEEK(applied_date, 1)
            ORDER BY week DESC
            LIMIT 8
            """, nativeQuery = true)
    List<Object[]> findWeeklyApplicationCounts(@Param("userId") Long userId);
}