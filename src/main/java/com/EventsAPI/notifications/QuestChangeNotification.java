package com.EventsAPI.notifications;

import com.EventsAPI.interfaces.Sendable;
import com.EventsAPI.pojos.QuestInfo;
import com.EventsAPI.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.QuestState;

import java.util.List;

public class QuestChangeNotification implements Sendable {
    private final static String API_ENDPOINT = Endpoint.QUEST_POINT_ENDPOINT;

    public QuestChangeNotification(String quest, QuestState state, List<QuestInfo> quests, int qp){
        setQp(qp);
        setQuests(quests);
        setQuest(quest);
        setState(state);
    }

    @Getter
    @Setter
    private int qp;

    @Getter
    @Setter
    private String quest;

    @Getter
    @Setter
    private QuestState state;

    @Getter
    @Setter
    private List<QuestInfo> quests;

    @Override
    public EventWrapper getEventWrapper() {
        return new EventWrapper(this);
    }

    @Override
    public String getApiEndpoint() {
        return API_ENDPOINT;
    }
}
