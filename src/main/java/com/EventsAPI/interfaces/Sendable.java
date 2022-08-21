package com.EventsAPI.interfaces;

import com.EventsAPI.notifications.EventWrapper;

public interface Sendable {
    public abstract EventWrapper getEventWrapper();
    public abstract String getApiEndpoint();
}
