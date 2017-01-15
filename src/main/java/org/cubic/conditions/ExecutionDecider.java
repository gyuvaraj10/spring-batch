package org.cubic.conditions;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
@Component
public class ExecutionDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED){
            return new FlowExecutionStatus("FAILED");
        }
        return new FlowExecutionStatus("UNKNOWN");
    }
}
