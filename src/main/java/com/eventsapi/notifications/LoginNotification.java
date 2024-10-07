package com.eventsapi.notifications;

import com.eventsapi.enums.LOGIN_STATE;
import com.eventsapi.interfaces.Sendable;
import com.eventsapi.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;

public class LoginNotification implements Sendable {
    private static final String API_ENDPOINT = Endpoint.LOGIN_NOTIFICATION;

    public LoginNotification(String username, LOGIN_STATE state){
        this.setUsername(username);
        this.setState(state);
    }

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private LOGIN_STATE state;

    @Override
    public EventWrapper getEventWrapper() {
        return new EventWrapper(this);
    }

    @Override
    public String getApiEndpoint() {
        return API_ENDPOINT;
    }
}
