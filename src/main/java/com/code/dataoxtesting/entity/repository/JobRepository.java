package com.code.dataoxtesting.entity.repository;

import com.code.dataoxtesting.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO for {@link Job}.
 */
public interface JobRepository extends JpaRepository<Job, Long> {

  boolean existsByUrl(String url);
}
