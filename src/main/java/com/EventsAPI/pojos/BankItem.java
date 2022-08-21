package com.EventsAPI.pojos;

import lombok.Getter;
import lombok.Setter;

public class BankItem {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private int quantity;


    public BankItem(int id, int quantity){
        this.id = id;
        this.quantity = quantity;
    }
}
