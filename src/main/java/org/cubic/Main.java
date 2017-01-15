package org.cubic;

import org.cubic.jobs.BatchConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Main {
    public static void main(String [] args) throws Exception{
        long startTime = System.currentTimeMillis();
        System.out.print("Start Processing at: "+ startTime);
        JobParameters jobParameters = new JobParametersBuilder().addString("dest", args[0]).toJobParameters();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(BatchConfiguration.class);
        context.refresh();
        JobLauncher launcher = context.getBean(JobLauncher.class);
        Job job = context.getBean(Job.class);
        launcher.run(job,new JobParametersBuilder()
                .addString("inputFile", "file:./products.txt")
                .addDate("date", new Date())
                .toJobParameters());
//        SpringApplication.run(Main.class, args);
        long endTime = System.currentTimeMillis();
        System.out.println("Ended Processing at: "+ endTime);
        System.out.println("Total time taken to process is: "+ ((endTime-startTime)/100));
    }
}
