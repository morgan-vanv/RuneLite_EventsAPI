package com.EventsAPI.notifications;

import com.EventsAPI.interfaces.Sendable;
import com.EventsAPI.pojos.BankItem;
import com.EventsAPI.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BankNotification implements Sendable {

    private static final String API_ENDPOINT = Endpoint.BANK_ENDPOINT;

    public BankNotification(List<BankItem> items, int totalPrice){
        setItems(items);
        setValue(totalPrice);
    }

    @Getter
    @Setter
    private int value;

    @Getter
    @Setter
    private List<BankItem> items;

    @Override
    public EventWrapper getEventWrapper() {
        return new EventWrapper(this);
    }

    @Override
    public String getApiEndpoint() {
        return API_ENDPOINT;
    }
}
