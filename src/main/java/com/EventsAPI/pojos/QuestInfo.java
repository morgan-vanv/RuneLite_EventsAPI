package com.EventsAPI.pojos;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.QuestState;

public class QuestInfo {
    public QuestInfo(String name, int id, QuestState state){
        setId(id);
        setName(name);
        setState(state);
    }

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private QuestState state;
}
