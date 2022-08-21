package com.EventsAPI;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("EventsAPI")
public interface EventsAPIConfig extends Config
{

	@ConfigItem(
			keyName = "Base Endpoint",
			name = "endpoint",
			description = "Endpoint to send data to: (example: http://localhost:9420/api/)"
	)
	default String apiEndpoint() {return "http://localhost:9420/api/"; }

	@ConfigItem(
			position = 1,
			keyName = "setTickDelay",
			name = "TickDelay",
			description = "Set default tick delay for periodic updates"
	)
	default int tickDelay() {return 10;}

	@ConfigItem(
			position = 2,
			keyName = "emitPlayerInfo",
			name = "Emit Player Info",
			description = "An optional addition of player information to each request"
	)
	default boolean emitAttachPlayerInfo() { return true; }

	@ConfigItem(
			position = 3,
			keyName = "emitPlayerState",
			name = "Periodically Emit Player Status",
			description = "Sends HP, Prayer, and Run Energy information periodically"
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
					name = "Enable Monster Kills",
					description = "If on, will send notifications about monster kills"
			)
	default boolean enableMonsterKill() { return true; }

	@ConfigItem
			(
					position = 6,
					keyName = "enabledLevelChange",
					name = "Enable Level Change Updates",
					description = "If on, will send notifications about level changes"
			)
	default boolean enableLevelChange() { return true; }

	@ConfigItem(
			position = 7,
			keyName = "emitEquippedItems",
			name = "Emit Equipped Items",
			description = "Sends equipped items"
	)
	default boolean emitEquippedItems() { return true; }

	@ConfigItem(
			position = 8,
			keyName = "emitInvoItem",
			name = "Emit Inventory Items",
			description = "Sends inventory layout"
	)
	default boolean emitInventory() { return true; }

	@ConfigItem(
			position = 9,
			keyName = "emitBankItems",
			name = "Emit Bank Items",
			description = "Sends bank items and value"
	)
	default boolean emitBankItems() { return true; }

	@ConfigItem(
			position = 10,
			keyName = "emitQuestInfo",
			name = "Emit Quest Info",
			description = "Sends quest info"
	)
	default boolean emitQuestInfo() { return true; }

	@ConfigItem(
			position = 11,
			keyName = "emitLoginState",
			name = "Emit Login State",
			description = "Sends when there is change in the login state"
	)
	default boolean emitLoginState() { return true; }

	@ConfigItem(
			position = 12,
			keyName = "Bearer Token",
			name = "bearerToken",
			description = "Unique api token to provided"
	)
	default String bearerToken() {return "token";}

}