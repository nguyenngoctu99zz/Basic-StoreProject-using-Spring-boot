package com.boostmytool.beststore.dao;

import com.boostmytool.beststore.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {
}
