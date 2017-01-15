package org.cubic.jobs;

import org.cubic.listners.CustomStepListener;
import org.cubic.listners.JourneyChunkListner;
import org.cubic.models.OtoDJourney;
import org.cubic.processors.JourneyItemProcess;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
    public FlatFileItemReader<OtoDJourney> readOtoDJourney(){
        FlatFileItemReader<OtoDJourney> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("fares.csv"));
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
    public Job importJourneyJob(){
        return jobBuilderFactory.get("importJourneyJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
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
    public Job job(Step step1) throws Exception {
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1)
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
}