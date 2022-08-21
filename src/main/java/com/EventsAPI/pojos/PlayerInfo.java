package com.EventsAPI.pojos;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.coords.WorldPoint;

public class PlayerInfo {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private int combatLevel;

    @Getter
    @Setter
    private WorldPoint position;

    public PlayerInfo(String username, int combatLevel, WorldPoint pos){
        this.username = username;
        this.combatLevel = combatLevel;
        this.position = pos;
    }

}
