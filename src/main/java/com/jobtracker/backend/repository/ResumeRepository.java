package com.jobtracker.backend.repository;

import com.jobtracker.backend.entity.Resume;
import com.jobtracker.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUser(User user);

    Optional<Resume> findByIdAndUser(Long id, User user);

    Optional<Resume> findByUserAndActiveTrue(User user);

    @Modifying
    @Transactional
    @Query("UPDATE Resume r SET r.active = false WHERE r.user = :user")
    void deactivateAllForUser(User user);
}