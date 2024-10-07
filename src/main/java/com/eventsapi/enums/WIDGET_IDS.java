package com.eventsapi.enums;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.InventoryID;

public enum WIDGET_IDS {
    BARROWS(WidgetID.BARROWS_REWARD_GROUP_ID, "Barrows", InventoryID.BARROWS_REWARD),
    COX(WidgetID.CHAMBERS_OF_XERIC_REWARD_GROUP_ID, "COX", InventoryID.CHAMBERS_OF_XERIC_CHEST),
    TOB(WidgetID.THEATRE_OF_BLOOD_GROUP_ID, "TOB", InventoryID.THEATRE_OF_BLOOD_CHEST),
    TOA(WidgetID.TOA_REWARD_GROUP_ID, "TOA", InventoryID.TOA_REWARD_CHEST),
    FISHING_TRAWLER(WidgetID.FISHING_TRAWLER_REWARD_GROUP_ID, "Fishing Trawler", InventoryID.FISHING_TRAWLER_REWARD),
    WILDERNESS_LOOT(WidgetID.WILDERNESS_LOOT_CHEST, "Wilderness Loot Chest", InventoryID.WILDERNESS_LOOT_CHEST);

    private final int widgetId;
    private final String name;
    private final InventoryID inventoryId;

    WIDGET_IDS(int widgetId, String name, InventoryID inventoryId) {
        this.widgetId = widgetId;
        this.name = name;
        this.inventoryId = inventoryId;
    }



    public int getWidgetId() {
        return widgetId;
    }

    public String getName() {
        return name;
    }

    public InventoryID getInventoryId() {
        return inventoryId;
    }

    public static WIDGET_IDS fromWidgetId(int widgetId) {
        for (WIDGET_IDS widget : values()) {
            if (widget.getWidgetId() == widgetId) {
                return widget;
            }
        }
        return null;
    }
}
