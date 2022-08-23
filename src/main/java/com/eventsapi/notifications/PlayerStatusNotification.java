package com.eventsapi.notifications;

import com.eventsapi.interfaces.Sendable;
import com.eventsapi.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.World;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.vars.AccountType;

public class PlayerStatusNotification implements Sendable {
    private static final String API_ENDPOINT = Endpoint.PLAYER_STATUS_ENDPOINT;

    public PlayerStatusNotification(String userName, AccountType accountType, int combatLevel, int currentWorld,
                                    WorldPoint currentWorldPoint, int maxHealth, int currentHealth,
                                    int maxPrayer, int currentPrayer, int currentRun, int currentWeight){
        setUserName(userName);
        setAccountType(accountType);
        setCombatLevel(combatLevel);
        setWorld(currentWorld);
        setWorldPoint(currentWorldPoint);
        setMaxHealth(maxHealth);
        setCurrentHealth(currentHealth);
        setMaxPrayer(maxPrayer);
        setCurrentPrayer(currentPrayer);
        setCurrentRun(currentRun);
        setCurrentWeight(currentWeight);
    }

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private AccountType accountType;

    @Getter
    @Setter
    private int combatLevel;

    @Getter
    @Setter
    private int world;

    @Getter
    @Setter
    private WorldPoint worldPoint;

    @Getter
    @Setter
    private int maxHealth;

    @Getter
    @Setter
    private int currentHealth;

    @Getter
    @Setter
    private int maxPrayer;

    @Getter
    @Setter
    private int currentPrayer;

    @Getter
    @Setter
    private int currentRun;

    @Getter
    @Setter
    private int currentWeight;

    @Override
    public EventWrapper getEventWrapper() {
        return new EventWrapper(this);
    }

    @Override
    public String getApiEndpoint() {
        return API_ENDPOINT;
    }
}
