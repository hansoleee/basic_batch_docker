package com.hansoleee.basic_batch_docker.source.domain;

import com.hansoleee.basic_batch_docker.target.domain.TargetBatch;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "source_batch")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class SourceBatch {

    @Id
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public TargetBatch toWriteBatch() {
        return new TargetBatch(dateTime);
    }
}
