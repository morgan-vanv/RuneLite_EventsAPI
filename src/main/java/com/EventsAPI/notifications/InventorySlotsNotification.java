package com.EventsAPI.notifications;

import com.EventsAPI.interfaces.Sendable;
import com.EventsAPI.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Item;

import java.util.List;

public class InventorySlotsNotification implements Sendable {
    private static final String API_ENDPOINT = Endpoint.INVENTORY_SLOT_ENDPOINT;

    public InventorySlotsNotification(List<Item> items, int gePrice){
        setInventory(items);
        setGePrice(gePrice);
    }

    @Getter
    @Setter
    private int gePrice;

    @Getter
    @Setter
    private List<Item> inventory;
    @Override
    public EventWrapper getEventWrapper() {
        return new EventWrapper(this);
    }

    @Override
    public String getApiEndpoint() {
        return API_ENDPOINT;
    }
}
