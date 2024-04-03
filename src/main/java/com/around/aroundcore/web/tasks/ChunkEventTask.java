package com.around.aroundcore.web.tasks;

import com.around.aroundcore.web.controllers.ws.ChunkWsController;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.gson.GsonParser;
import com.around.aroundcore.web.services.ChunkQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public class ChunkEventTask implements Runnable{
    private ChunkQueueService queueService;
    private SimpMessagingTemplate messagingTemplate;
    private GsonParser gsonParser;

    @Override
    public void run() {
        if(!queueService.isEmpty()){
            List<ChunkDTO> chunks = queueService.getAllFromQueue();
            String json = gsonParser.toJson(chunks);
            messagingTemplate.convertAndSend(
                    ChunkWsController.FETCH_CHUNK_CHANGES_EVENT,
                    json
            );
        }
    }
}
