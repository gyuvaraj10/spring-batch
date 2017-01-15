package org.cubic.jobs;

import org.cubic.conditions.ExecutionDecider;
import org.cubic.listners.CustomStepListener;
import org.cubic.listners.JourneyChunkListner;
import org.cubic.models.OtoDJourney;
import org.cubic.processors.JourneyItemProcess;
import org.cubic.processors.WebserviceCallProcess;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourcesItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private CustomStepListener customStepListener;


    @Bean
    @StepScope
    public Resource destFile(@Value("#{jobParameters[dest]}") String dest) {
        return new FileSystemResource(dest);
    }

    @Bean
    public FlatFileItemReader<OtoDJourney> readOtoDJourney(){

        FlatFileItemReader<OtoDJourney> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("fares.csv"));
//        itemReader.setResource(destFile());
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(new DefaultLineMapper<OtoDJourney>(){{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"origin", "destination", "minPrice", "maxPrice"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<OtoDJourney>() {{
                setTargetType(OtoDJourney.class);
            }});
        }});
        return itemReader;
    }

    @Bean
    public ItemWriter<OtoDJourney> otoDJourneyItemWriter(){
        System.out.println("Chunk is Completed");
        return null;
    }

    @Bean
    public JourneyItemProcess processor() {
        return new JourneyItemProcess();
    }

    @Bean
    public WebserviceCallProcess processorWeb() {
        return new WebserviceCallProcess();
    }

    @Bean
    public Job importJourneyJob(){
        return jobBuilderFactory.get("importJourneyJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .next(new ExecutionDecider())
                .on("*").to(step2())
                .on("*").end()
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<OtoDJourney, OtoDJourney>chunk(5)
                .reader(readOtoDJourney())
                .processor(processor())
                .listener(getJourneyChunkListner())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<OtoDJourney, OtoDJourney>chunk(5)
                .reader(readOtoDJourney())
                .processor(processorWeb())
                .listener(getJourneyChunkListner())
                .build();
    }


    @Bean
    public CustomStepListener getCustomStepListener(){
        return new CustomStepListener();
    }

    @Bean
    public JourneyChunkListner getJourneyChunkListner(){
        return new JourneyChunkListner();
    }

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }
}