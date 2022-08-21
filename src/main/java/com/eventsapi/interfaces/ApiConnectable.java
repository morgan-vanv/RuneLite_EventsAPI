package com.eventsapi.interfaces;

import java.util.List;

public interface ApiConnectable {
    void init();
    void send(List<Sendable> messages);
    void send(Sendable message);
}
