package com.eventsapi.utils;

import net.runelite.api.Item;

public class CommonUtility {

    public static boolean areItemContainerEqual(Item[] items1, Item[] items2){
        if(items1.length != items2.length) {
            return false;
        }

        for(int i = 0; i < items1.length; i++){
            if(items1[i].getId() != items2[i].getId()){
                return false;
            }
            if(items1[i].getQuantity() != items2[i].getQuantity()){
                return false;
            }
        }

        return true;
    }
}
