package com.jobtracker.backend.repository;

import com.jobtracker.backend.entity.JobApplication;
import com.jobtracker.backend.entity.MatchResult;
import com.jobtracker.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    List<MatchResult> findByApplicationOrderByCreatedAtDesc(JobApplication application);

    @Query("SELECT AVG(m.matchScore) FROM MatchResult m WHERE m.application.user = :user")
    Double findAverageScoreByUser(@Param("user") User user);
}