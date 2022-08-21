package com.EventsAPI;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EventsAPIPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EventsAPIPlugin.class);
		RuneLite.main(args);
	}
}