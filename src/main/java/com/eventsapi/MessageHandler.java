package com.eventsapi;

import com.eventsapi.enums.MESSAGE_EVENT;
import com.eventsapi.interfaces.ApiConnectable;
import com.eventsapi.interfaces.Sendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageHandler{
    private Map<MESSAGE_EVENT, com.eventsapi.MessageChannel> eventChannels;
    private ApiConnectable apiConnection;
    private static final double GAME_TICKS_PER_SECOND = 0.6;
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler(ApiConnectable apiConnection){
        this.eventChannels = new HashMap<>();
        this.apiConnection = apiConnection;

        //Events which are 'latest only' are sent every X game ticks when often updates are occuring
        //Other events use the sendEventNow function if they are time important, such as Level Ups, Quest Completes, etc
        eventChannels.put(MESSAGE_EVENT.BANK, new com.eventsapi.MessageChannel(secondsToGameTicks(10)));
        eventChannels.put(MESSAGE_EVENT.INVO, new com.eventsapi.MessageChannel(secondsToGameTicks(2)));
        eventChannels.put(MESSAGE_EVENT.EQUIPMENT, new com.eventsapi.MessageChannel(secondsToGameTicks(2)));
    }

    private int secondsToGameTicks(int seconds){
        return (int)((double)(seconds) / GAME_TICKS_PER_SECOND);
    }

    //Gather all messages from the queues into once list
    private List<Sendable> getMessagesFromQueues(){
        List<Sendable> messages = new ArrayList<>();
        for(com.eventsapi.MessageChannel channel : eventChannels.values()){
            Sendable message = channel.getMessageToSend();
            if(message != null){
                messages.add(message);
            }
        }
        return messages;
    }

    //Used to send an event right now, no waiting around
    public void sendEventNow(MESSAGE_EVENT eventType, Sendable message){
        logger.debug("Sending event now: " + eventType.toString());
        apiConnection.send(message);
    }

    //Update the messages and determine when to send, prevents spamming (Shift dropping, gear swapping, etc)
    public void processGameTicks(){
        for(com.eventsapi.MessageChannel channel : eventChannels.values()){
            channel.processGameTick();
        }

        List<Sendable> messagesToSend = getMessagesFromQueues();
        if(messagesToSend.size() > 0){
            logger.debug("Sending message list to ApiConnectable to be sent: " + messagesToSend.size() + " messages");
            apiConnection.send(messagesToSend);
        }
    }

    //Update the latest event for a given message
    public void updateLatestEvent(MESSAGE_EVENT eventType, Sendable event){
        logger.debug("Attempting to insert event of type: " + eventType.toString());
        try {
           eventChannels.get(eventType).updateLatest(event);
           logger.debug("Queued " + eventType.toString() + " message to be sent");
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }
}
