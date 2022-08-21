package com.EventsAPI;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

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

	@Override
	protected void startUp() throws Exception
	{
		log.info("EventsAPIPlugin Started!");
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
