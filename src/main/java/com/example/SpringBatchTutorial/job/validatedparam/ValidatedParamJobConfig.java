package com.example.SpringBatchTutorial.job.validatedparam;

import com.example.SpringBatchTutorial.job.validatedparam.validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc: 파일 이름 파라미터 전달 그리고 검증
 * run: --spring.batch.job.names=validatedParamJob -fileName=test.csv
 */
@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validatedParamJob(Step validatedParamStep) {
        return jobBuilderFactory.get("validatedParamJob")
                .incrementer(new RunIdIncrementer())
                .validator(new FileParamValidator())
                .start(validatedParamStep)
                .build();
    }

    @JobScope
    @Bean
    public Step validatedParamStep(Tasklet validatedParamTasklet) {
        return stepBuilderFactory.get("validatedParamStep")
                .tasklet(validatedParamTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet validatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println("fileName = " + fileName);
            System.out.println("validated Param Tasklet");
            return RepeatStatus.FINISHED;
        };
    }

}
