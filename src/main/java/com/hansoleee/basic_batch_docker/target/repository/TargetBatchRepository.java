package com.hansoleee.basic_batch_docker.target.repository;

import com.hansoleee.basic_batch_docker.target.domain.TargetBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

public interface TargetBatchRepository extends JpaRepository<TargetBatch, LocalDateTime> {
}
