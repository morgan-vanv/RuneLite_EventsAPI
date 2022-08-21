package com.EventsAPI;

// RuneLite & General Imports
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
//import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

// Local Imports
import com.EventsAPI.enums.LOGIN_STATE;
import com.EventsAPI.interfaces.ApiConnectable;
import com.EventsAPI.EventsAPIConfig;
//import com.EventsAPI.interfaces.EventsConfig;
import com.EventsAPI.notifications.*;
import com.EventsAPI.enums.MESSAGE_EVENT;
import com.EventsAPI.pojos.BankItem;
import com.EventsAPI.pojos.QuestInfo;
import com.EventsAPI.utils.CommonUtility;
import com.EventsAPI.MessageHandler;

import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "EventsAPI"
)
public class EventsAPIPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private EventsAPIConfig config;

	@Inject
	private ItemManager itemManager;

	// Variables to store and poll for update checking
	private boolean hasLoggedIn = false;
	private boolean hasTicked;
	private int[] lastSkillLevels;
	private QuestState[] lastQuestStates;
	private Quest[] quests;
	private boolean lastBankOpenStatus ;
	private Item[] lastInventoryState;
	private ItemContainer lastBankContainer;

	// Class to handle messages
	private MessageHandler messageHandler;

	@Override
	protected void startUp() throws Exception
	{
		log.info("EventsAPIPlugin Started!");
		ApiConnectable apiManager = new ApiManager(config, client);
		messageHandler = new MessageHandler(apiManager);

		this.initializeSessionVariables();
	}

	// Functions
	private void initializeSessionVariables(){
		lastSkillLevels = new int[Skill.values().length - 1];
		hasTicked = false;
		lastQuestStates = new QuestState[Quest.values().length];
		lastBankOpenStatus = false;
		quests = Quest.values();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("EventsAPIPlugin stopped!");
	}

	@Provides
	EventsAPIConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EventsAPIConfig.class);
	}
}
