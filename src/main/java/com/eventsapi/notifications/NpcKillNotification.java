package com.eventsapi.notifications;

import com.eventsapi.interfaces.Sendable;
import com.eventsapi.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Item;

import java.util.List;

public class NpcKillNotification implements Sendable {

    private final static String API_ENDPOINT = Endpoint.NPC_KILL_ENDPOINT;

    public NpcKillNotification(int npcId, List<Item> items, int gePrice){

        this.setNpcId(npcId);
        this.setItems(items);
        this.setGePrice(gePrice);
    }

    public String getApiEndpoint(){
        return API_ENDPOINT;
    }
    public EventWrapper getEventWrapper(){ return new EventWrapper(this); }

    @Getter
    @Setter
    private int gePrice;

    @Getter
    @Setter
    private int npcId;

    @Getter
    @Setter
    private List<Item> items;
}
