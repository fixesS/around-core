package com.around.aroundcore.web.services.queues;

import com.around.aroundcore.web.dtos.ChunkDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ChunkQueueService {
    private LinkedBlockingQueue<ChunkDTO> chunkChangesQueue = new LinkedBlockingQueue<>();
    public void addToQueue(ChunkDTO chunkDTO) {
        chunkChangesQueue.add(chunkDTO);
    }
    public void addToQueue(List<ChunkDTO> chunkDTOList){
        chunkDTOList.forEach(this::addToQueue);
    }
    public ChunkDTO getFromQueue() {
        return chunkChangesQueue.poll();
    }
    public List<ChunkDTO> getAllFromQueue() {
        List<ChunkDTO> chunks = new ArrayList<>();
        while (!isEmpty()) {
            ChunkDTO chunkDTOFromQueue = getFromQueue();
            if(chunks.contains(chunkDTOFromQueue)){
                ChunkDTO chunkDTOInList = chunks.get(chunks.indexOf(chunkDTOFromQueue));
                chunkDTOInList.setTeam_id(chunkDTOFromQueue.getTeam_id());
            }else{
                chunks.add(chunkDTOFromQueue);
            }
        }
        return chunks;
    }
    public boolean isEmpty(){
        return chunkChangesQueue.isEmpty();
    }
}
