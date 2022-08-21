package com.EventsAPI.notifications;

import com.EventsAPI.interfaces.Sendable;
import com.EventsAPI.utils.Endpoint;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class LevelChangeNotification implements Sendable {

    private final static String API_ENDPOINT = Endpoint.LEVEL_CHANGE_ENDPOINT;

    public LevelChangeNotification(String name, Integer level, Map<String, Integer> levelMap){
        setUpdatedSkillName(name);
        setUpdatedSkillLevel(level);
        setLevels(levelMap);

        int totalLevel = 0;
        for(Integer skillLevel : levelMap.values()){
            totalLevel += skillLevel;
        }
        this.setTotalLevel(totalLevel);
    }

    @Getter
    @Setter
    private int totalLevel;

    @Getter
    @Setter
    private String updatedSkillName;

    @Getter
    @Setter
    private Integer updatedSkillLevel;

    @Getter
    @Setter
    private Map<String, Integer> levels;

    @Override
    public EventWrapper getEventWrapper() {
        return new EventWrapper(this);
    }

    @Override
    public String getApiEndpoint() {
        return API_ENDPOINT;
    }
}
