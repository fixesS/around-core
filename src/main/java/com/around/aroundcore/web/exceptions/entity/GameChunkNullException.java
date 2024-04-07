package com.around.aroundcore.web.exceptions.entity;

public class GameChunkNullException extends EntityNullException{
    public GameChunkNullException(){
        super("GameChunk is null");
    }

    public GameChunkNullException(String message){
        super(message);
    }
}
