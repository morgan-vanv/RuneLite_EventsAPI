package com.eventsapi.interfaces;

import com.eventsapi.notifications.EventWrapper;

public interface Sendable {
    public abstract EventWrapper getEventWrapper();
    public abstract String getApiEndpoint();
}
