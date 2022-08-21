package com.EventsAPI;

import com.EventsAPI.interfaces.Sendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageChannel {

    private boolean canSend = false;
    private boolean hasSent = false;
    private Sendable latestMessage;
    private int gameTicksBetweenFrequentActions = 0;
    private int gameTicksSinceSending = 0;
    private static final Logger logger = LoggerFactory.getLogger(MessageChannel.class);

    public MessageChannel(int gameTicksBetweenFrequentActions){
        this.gameTicksBetweenFrequentActions = gameTicksBetweenFrequentActions;
        this.canSend = true;
    }

    public void processGameTick(){
        //Update send clock if this queue is only tracking the latest queue message
        if(gameTicksSinceSending < gameTicksBetweenFrequentActions){
            gameTicksSinceSending++;
        }

        //Update send clock if this queue is only tracking the latest queue message
        if(canSend == false && gameTicksSinceSending >= gameTicksBetweenFrequentActions){
            this.canSend = true;
            gameTicksSinceSending = 0;
        }
    }

    public void updateLatest(Sendable message){
        latestMessage = message;
        this.hasSent = false;
    }

    public Sendable getMessageToSend(){
        if(hasSent == false && canSend){
            this.hasSent = true;
            this.gameTicksSinceSending = 0;
            this.canSend = false;
            return latestMessage;
        }

        return null;
    }
}
