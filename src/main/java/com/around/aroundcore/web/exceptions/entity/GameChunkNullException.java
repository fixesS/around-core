package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class GameChunkNullException extends EntityNullException{
    public GameChunkNullException(){
        super("GameChunk is null", ApiResponse.CHUNK_DOES_NOT_EXIST);
    }

    public GameChunkNullException(String message){
        super(message);
    }
}
