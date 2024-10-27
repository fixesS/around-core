package com.around.aroundcore.web.tasks;

import com.around.aroundcore.web.controllers.ws.ChunkWsController;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.services.queues.ChunkQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class SendChunksTask {
    private final ChunkQueueService queueService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 100)
    public void sendChunks() {
        if(!queueService.isEmpty()){
            List<ChunkDTO> chunks = queueService.getAllFromQueue();
            messagingTemplate.convertAndSend(
                    ChunkWsController.CHUNK_CHANGES_EVENT,
                    chunks
            );
            log.debug("Chunks have been sent");
        }
    }
}
