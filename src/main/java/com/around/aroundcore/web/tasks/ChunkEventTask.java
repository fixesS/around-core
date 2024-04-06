package com.around.aroundcore.web.tasks;

import com.around.aroundcore.web.controllers.ws.ChunkWsController;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.services.ChunkQueueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ChunkEventTask implements Runnable{
    private ChunkQueueService queueService;
    private SimpMessagingTemplate messagingTemplate;
    private ObjectMapper objectMapper;

    @Override
    public void run() {
        if(!queueService.isEmpty()){
            List<ChunkDTO> chunks = queueService.getAllFromQueue();
            messagingTemplate.convertAndSend(
                    ChunkWsController.FETCH_CHUNK_CHANGES_EVENT,
                    chunks
            );
        }
    }
}
