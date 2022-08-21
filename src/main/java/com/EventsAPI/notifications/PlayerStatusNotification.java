package com.EventsAPI.notifications;

import com.EventsAPI.interfaces.Sendable;
import com.EventsAPI.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;

public class PlayerStatusNotification implements Sendable {
    private static final String API_ENDPOINT = Endpoint.PLAYER_STATUS_ENDPOINT;

    public PlayerStatusNotification(int currentWorld, int maxHealth, int currentHealth, int maxPrayer,
                                    int currentPrayer, int currentRun, int currentWeight){
        setCurrentWorld(currentWorld);
        setMaxHealth(maxHealth);
        setCurrentHealth(currentHealth);
        setMaxPrayer(maxPrayer);
        setCurrentPrayer(currentPrayer);
        setCurrentRun(currentRun);
        setCurrentWeight(currentWeight);
    }

    @Getter
    @Setter
    private int currentWorld;

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
