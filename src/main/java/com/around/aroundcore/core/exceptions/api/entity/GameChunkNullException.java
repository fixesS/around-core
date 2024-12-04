package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameChunkNullException extends EntityNullException{
    public GameChunkNullException(){
        super("GameChunk is null", ApiResponse.CHUNK_DOES_NOT_EXIST);
    }

    public GameChunkNullException(String message){
        super(message);
    }
}
