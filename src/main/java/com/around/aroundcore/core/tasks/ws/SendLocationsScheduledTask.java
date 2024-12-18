package com.around.aroundcore.core.tasks.ws;

import com.around.aroundcore.config.WebSocketConfig;
import com.around.aroundcore.web.dtos.user.GameUserLocationDTO;
import com.around.aroundcore.core.services.queues.GameUserLocationQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class SendLocationsScheduledTask {
    private final GameUserLocationQueueService queueService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 100)
    public void sendChunks() {
        if(!queueService.isEmpty()){
            List<GameUserLocationDTO> locations = queueService.getAllFromQueue();
            locations.forEach(location -> messagingTemplate.convertAndSendToUser(location.getFriendId().toString(),
                    WebSocketConfig.QUEUE_LOCATIONS_FOR_USER, location));
            log.debug("Locations have been sent");
        }
    }
}
