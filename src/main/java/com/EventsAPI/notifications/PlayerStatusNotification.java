package com.EventsAPI.notifications;

import com.EventsAPI.interfaces.Sendable;
import com.EventsAPI.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;

public class PlayerStatusNotification implements Sendable {
    private static final String API_ENDPOINT = Endpoint.PLAYER_STATUS_ENDPOINT;

    public PlayerStatusNotification(int currentWorld, String currentHealth, String currentPrayer,
                                    int currentRun, int currentWeight){
        setCurrentWorld(currentWorld);
        setCurrentHealth(currentHealth);
        setCurrentPrayer(currentPrayer);
        setCurrentRun(currentRun);
        setCurrentWeight(currentWeight);
    }

    @Getter
    @Setter
    private int currentWorld;

    @Getter
    @Setter
    private String currentHealth;

    @Getter
    @Setter
    private String currentPrayer;

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
