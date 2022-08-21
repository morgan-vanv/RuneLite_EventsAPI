package com.EventsAPI;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("EventsAPI")
public interface EventsAPIConfig extends Config
{

	// GENERAL SETTINGS
	@ConfigSection(
			name = "General Settings",
			description = "General Settings",
			position = 0,
			closedByDefault = false
	)
	String generalSettings = "generalSettings";

	@ConfigItem(
			keyName = "Base Endpoint",
			name = "Endpoint",
			description = "Endpoint to send data to: (example: http://localhost:9420/api/)",
			section = "generalSettings"
	)
	default String apiEndpoint() {return "http://localhost:9420/api/"; }

	@ConfigItem(
			position = 1,
			keyName = "setTickDelay",
			name = "Tick Delay",
			description = "Set default tick delay for periodic updates",
			section = "generalSettings"
	)
	default int tickDelay() {return 10;}

	// CUSTOMIZABLE SETTINGS
	@ConfigSection(
			name = "Customize Event Emissions",
			description = "Select which events you wish to receive data about",
			position = 1,
			closedByDefault = false
	)
	String customSettings = "customSettings";

	@ConfigItem(
			position = 2,
			keyName = "emitPlayerInfo",
			name = "Attach Player Info",
			description = "An optional addition of player information to each request",
			section = "customSettings"
	)
	default boolean emitAttachPlayerInfo() { return true; }

	@ConfigItem(
			position = 3,
			keyName = "emitPlayerState",
			name = "Periodically Emit Player Status",
			description = "Sends HP, Prayer, and Run Energy information periodically",
			section = "customSettings"
	)
	default boolean emitPlayerState() { return true; }

	//@ConfigItem(
	//		position = 4,
	//		keyName = "emitAnimationStatus",
	//		name = "TODO: Emit Animation Status Change",
	//		description = "Triggers on animation change (maybe)"
	//)
	//default boolean emitAnimationState() { return false; }

	@ConfigItem
			(
					position = 5,
					keyName = "enableMonsterKills",
					name = "Emit Monster Kills",
					description = "Sends notifications about monster kills",
					section = "customSettings"
			)
	default boolean enableMonsterKill() { return true; }

	@ConfigItem
			(
					position = 6,
					keyName = "enabledLevelChange",
					name = "Emit Level Change Updates",
					description = "Send notifications about level changes",
					section = "customSettings"
			)
	default boolean enableLevelChange() { return true; }

	@ConfigItem(
			position = 7,
			keyName = "emitEquippedItems",
			name = "Emit Equipped Items",
			description = "Sends equipped items",
			section = "customSettings"
	)
	default boolean emitEquippedItems() { return true; }

	@ConfigItem(
			position = 8,
			keyName = "emitInvoItem",
			name = "Emit Inventory Items",
			description = "Sends inventory layout",
			section = "customSettings"
	)
	default boolean emitInventory() { return true; }

	@ConfigItem(
			position = 9,
			keyName = "emitBankItems",
			name = "Emit Bank Items",
			description = "Sends bank items and value",
			section = "customSettings"
	)
	default boolean emitBankItems() { return true; }

	@ConfigItem(
			position = 10,
			keyName = "emitQuestInfo",
			name = "Emit Quest Info",
			description = "Sends quest info",
			section = "customSettings"
	)
	default boolean emitQuestInfo() { return true; }

	@ConfigItem(
			position = 11,
			keyName = "emitLoginState",
			name = "Emit Login State",
			description = "Sends when there is change in the login state",
			section = "customSettings"
	)
	default boolean emitLoginState() { return true; }

	// ADVANCED SETTINGS
	@ConfigSection(
			name = "Advanced Settings & Credits",
			description = "Credit To llamaXc and OSRSEvents Plugin",
			position = 12,
			closedByDefault = true
	)
	String advancedSettings = "advancedSettings";

	@ConfigItem(
			position = 13,
			keyName = "Bearer Token",
			name = "BearerToken",
			description = "API Token to be provided",
			section = "advancedSettings"
	)
	default String bearerToken() {return "Credit To llamaXc and OSRSEvents";}

}