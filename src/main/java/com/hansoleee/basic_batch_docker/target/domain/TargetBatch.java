package com.hansoleee.basic_batch_docker.target.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "target_batch")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class TargetBatch {

    @Id
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public TargetBatch(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
