package org.cubic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Main {
    public static void main(String [] args) {
        long startTime = System.currentTimeMillis();
        System.out.print("Start Processing at: "+ startTime);
        SpringApplication.run(Main.class, args);
        long endTime = System.currentTimeMillis();
        System.out.println("Ended Processing at: "+ endTime);
        System.out.println("Total time taken to process is: "+ ((endTime-startTime)/100));
    }
}
