package org.cubic.listners;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
@Component
public class JourneyChunkListner implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
       System.out.println("Before Processing the Chunk");
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println("After Processing the Chunk");
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.FAILED);
        System.out.println("After Getting the Chunk Error");
    }
}
