package com.EventsAPI.notifications;

import com.EventsAPI.interfaces.Sendable;
import com.EventsAPI.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;

import java.util.Map;

public class EquipSlotsNotification implements Sendable {
    private final static String API_ENDPOINT = Endpoint.EQUIPPED_ITEMS_ENDPOINT;

    public EquipSlotsNotification(Map<EquipmentInventorySlot, Item> equippedItems){
        setEquippedItems(equippedItems);
    }

    @Setter
    @Getter
    private Map<EquipmentInventorySlot, Item> equippedItems;

    @Override
    public EventWrapper getEventWrapper() {
        return new EventWrapper(this);
    }

    @Override
    public String getApiEndpoint() {
        return API_ENDPOINT;
    }
}
