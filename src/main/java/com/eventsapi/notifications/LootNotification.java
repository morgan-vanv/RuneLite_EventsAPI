package com.eventsapi.notifications;

import com.eventsapi.interfaces.Sendable;
import com.eventsapi.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Item;

import java.util.List;

public class LootNotification implements Sendable {

    private final static String API_ENDPOINT = Endpoint.LOOT_ENDPOINT;

    public LootNotification(int widgetId, List<Item> items, int gePrice, String userName){

        this.setWidgetId(widgetId);
        this.setItems(items);
        this.setGePrice(gePrice);
        this.setUserName(userName);
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
    private int widgetId;

    @Getter
    @Setter
    private List<Item> items;

    @Getter
    @Setter
    private String userName;

}
