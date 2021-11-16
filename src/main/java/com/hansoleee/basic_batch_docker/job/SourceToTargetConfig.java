package com.hansoleee.basic_batch_docker.job;

import com.hansoleee.basic_batch_docker.source.domain.SourceBatch;
import com.hansoleee.basic_batch_docker.target.domain.TargetBatch;
import com.hansoleee.basic_batch_docker.target.repository.TargetBatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class SourceToTargetConfig {

    public static final String JOB_NAME = "sourceToTarget";
    public static final String PREFIX_JOB = JOB_NAME + "_";
    private final static int chunkSize = 1000;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory sourceEntityManagerFactory;

    private final TargetBatchRepository targetBatchRepository;

    public SourceToTargetConfig(
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
            TargetBatchRepository targetBatchRepository
    ) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.sourceEntityManagerFactory = sourceEntityManagerFactory;
        this.targetBatchRepository = targetBatchRepository;
    }

    @Bean(name = JOB_NAME)
    public Job sourceToTargetJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean(name = PREFIX_JOB + "step")
    public Step step() {
        return stepBuilderFactory.get(PREFIX_JOB + "step")
                .<SourceBatch, TargetBatch>chunk(chunkSize)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(name = PREFIX_JOB + "reader")
    @StepScope
    public JpaPagingItemReader<SourceBatch> reader(@Value("#{jobParameters[requestDate]}") String requestDate) {
        final Map<String, Object> parameters = new HashMap<>() {{
            put("dateTime", LocalDate.parse(requestDate, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay());
        }};

        return new JpaPagingItemReaderBuilder<SourceBatch>()
                .name(PREFIX_JOB + "reader")
                .pageSize(chunkSize)
                .entityManagerFactory(sourceEntityManagerFactory)
                .queryString(
                        "SELECT sb " +
                                "FROM SourceBatch sb " +
                                "WHERE sb.dateTime = :dateTime")
                .parameterValues(parameters)
                .build();
    }

    @Bean(name = PREFIX_JOB + "processor")
    public ItemProcessor<SourceBatch, TargetBatch> processor() {
        return SourceBatch::toWriteBatch;
    }

    @Bean(name = PREFIX_JOB + "writer")
    public ItemWriter<TargetBatch> writer() {
        return items -> {
            log.info("items: \n{}", items);
            targetBatchRepository.saveAll(items);
        };
    }
}
