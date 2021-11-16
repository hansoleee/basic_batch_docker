package com.hansoleee.basic_batch_docker.source.repository;

import com.hansoleee.basic_batch_docker.source.domain.SourceBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

public interface SourceBatchRepository extends JpaRepository<SourceBatch, LocalDateTime> {
}
