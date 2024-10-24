package com.around.aroundcore.web.services;

import com.around.aroundcore.web.dtos.user.GameUserLocationDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class GameUserLocationQueueService {
    private final LinkedBlockingQueue<GameUserLocationDTO> locationChangesQueue = new LinkedBlockingQueue<>();
    public void addToQueue(GameUserLocationDTO locationDTO) {
        locationChangesQueue.add(locationDTO);
    }
    public GameUserLocationDTO getFromQueue() {
        return locationChangesQueue.poll();
    }
    public List<GameUserLocationDTO> getAllFromQueue() {
        List<GameUserLocationDTO> locs = new ArrayList<>();
        while (!isEmpty()) {
            GameUserLocationDTO locationFromQueue = getFromQueue();
            if(locs.contains(locationFromQueue)){
                GameUserLocationDTO locsDTOInList = locs.get(locs.indexOf(locationFromQueue));
                locsDTOInList.setLongitude(locationFromQueue.getLongitude());
                locsDTOInList.setLatitude(locationFromQueue.getLatitude());
            }else{
                locs.add(locationFromQueue);
            }
        }
        return locs;
    }
    public boolean isEmpty(){
        return locationChangesQueue.isEmpty();
    }

}
