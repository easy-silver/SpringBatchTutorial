package com.example.SpringBatchTutorial.job.conditionalstep;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc: step 결과에 따른 다음 step 분기 처리
 * run: --spring.batch.job.names=conditionalStepJob
 */
@Configuration
@RequiredArgsConstructor
public class ConditionalStepJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job conditionalStepJob(
            Step conditionalStartStep,
            Step conditionalFailedStep,
            Step conditionalAllStep,
            Step conditionalCompletedStep
    ) {
        return jobBuilderFactory.get("conditionalStepJob")
                .incrementer(new RunIdIncrementer())
                .start(conditionalStartStep)
                .on("FAILED").to(conditionalFailedStep)
                .from(conditionalStartStep)
                .on("COMPLETED").to(conditionalCompletedStep)
                .from(conditionalStartStep)
                .on("*").to(conditionalAllStep)
                .end()
                .build();
    }

    @JobScope
    @Bean
    public Step conditionalStartStep() {
        return stepBuilderFactory.get("conditionalStartStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("conditional Start Step");
                    return RepeatStatus.FINISHED;
                    //throw new Exception("Exception!!!");
                })
                .build();
    }

    @JobScope
    @Bean
    public Step conditionalAllStep() {
        return stepBuilderFactory.get("conditionalAllStep")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("conditional All Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @JobScope
    @Bean
    public Step conditionalFailedStep() {
        return stepBuilderFactory.get("conditionalFailedStep")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("conditional Fail Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @JobScope
    @Bean
    public Step conditionalCompletedStep() {
        return stepBuilderFactory.get("conditionalCompletedStep")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("conditional Completed Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
