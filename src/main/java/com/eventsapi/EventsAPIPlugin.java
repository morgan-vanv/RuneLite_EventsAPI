package com.eventsapi;

// RuneLite & General Imports
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

// Local Imports
import com.eventsapi.enums.WIDGET_IDS;
import com.eventsapi.enums.LOGIN_STATE;
import com.eventsapi.interfaces.ApiConnectable;
import com.eventsapi.notifications.*;
import com.eventsapi.enums.MESSAGE_EVENT;
import com.eventsapi.pojos.BankItem;
import com.eventsapi.pojos.QuestInfo;
import com.eventsapi.utils.CommonUtility;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private int tickIterator = 0;
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

	private static final Logger logger = LoggerFactory.getLogger(EventsAPIPlugin.class);

	@Override
	protected void startUp() throws Exception
	{
		log.info("EventsAPIPlugin Started!");
		ApiConnectable apiManager = new ApiManager(config, client);
		messageHandler = new MessageHandler(apiManager);

		this.initializeSessionVariables();
	}

	// Initial Setup Functions
	@Subscribe
	public void onGameTick(final GameTick event) {
		tickIterator += 1;
		if(!hasTicked){
			logger.debug("First game tick detected, handle initial events");
			hasTicked = true;
			this.populateCurrentQuests();
			this.sendInitialLoginEvents();
		}

		this.detectQuestEvents();
		this.detectBankWindowClosing();

		if (tickIterator >= this.config.tickDelay()) {
			this.createAndSendPlayerStatusNotification();
			tickIterator = 0;
		}

		this.messageHandler.processGameTicks();
	}

	private void initializeSessionVariables(){
		lastSkillLevels = new int[Skill.values().length - 1];
		hasTicked = false;
		lastQuestStates = new QuestState[Quest.values().length];
		lastBankOpenStatus = false;
		quests = Quest.values();
	}

	// Subscribers or Event Listener Functions (maybe split & organize the message creator and sender functions)
	@Subscribe
	public void onGameStateChanged(GameStateChanged state){
		if(state.getGameState() == GameState.LOGIN_SCREEN){
			if(hasLoggedIn == true){
				LoginNotification loggedOut = new LoginNotification(LOGIN_STATE.LOGGED_OUT);
				messageHandler.sendEventNow(MESSAGE_EVENT.LOGIN, loggedOut);
				this.hasLoggedIn = false;
			}

			logger.debug("Player is on login screen, setting up session variables");
			this.initializeSessionVariables();
		}

		if(state.getGameState() == GameState.LOGGED_IN){
			this.hasLoggedIn = true;
			LoginNotification loggedIn = new LoginNotification((LOGIN_STATE.LOGGED_IN));
			messageHandler.sendEventNow(MESSAGE_EVENT.LOGIN, loggedIn);
		}
	}

	private void sendInitialLoginEvents(){
		logger.debug("Sending initial level and quest notifications");
		this.createAndSendLevelNotification(null, null);
		this.createAndSendQuestNotification(null, null);
	}

	private void populateCurrentQuests(){
		logger.debug("Populating quest states with latest player states");
		for (int i = 0; i < quests.length; i++){
			QuestState currentQuestState = quests[i].getState(client);
			lastQuestStates[i] = currentQuestState;
		}
	}

	private void detectQuestEvents(){
		boolean questChangeFound = false;
		for (int i = 0; i < quests.length; i++){
			if(lastQuestStates[i] != quests[i].getState(client)){
				String questName =quests[i].getName();
				QuestState currentQuestState = quests[i].getState(client);
				lastQuestStates[i] = currentQuestState;
				if(questChangeFound == false) {
					logger.debug("Detected new quest state: " + questName + " is now: " + currentQuestState.toString());
					this.createAndSendQuestNotification(questName, currentQuestState);
					questChangeFound = true;
				}
			}
		}
	}

	private void detectBankWindowClosing(){
		Widget con = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
		if(con != null) {
			lastBankOpenStatus = true;
			lastBankContainer = client.getItemContainer(InventoryID.BANK);
		}else if(lastBankOpenStatus){
			lastBankOpenStatus = false;
			logger.debug("Detected closing of bank window. Preparing bank notification");
			this.createAndQueueBankNotification();
		}
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		if (event.getItemContainer() == client.getItemContainer(InventoryID.EQUIPMENT)) {
			logger.debug("Detected ItemContainer change for EQUIPMENT, preparing to queue event");
			this.createAndQueueEquipmentNotification(event.getItemContainer().getItems());
		}

		if(event.getItemContainer() == client.getItemContainer(InventoryID.INVENTORY)){

			//When first logging in, set the inventory container and fire off an event to load first inventory
			if(lastInventoryState == null){
				logger.debug("First time seeing inventory event, prepare to create first Inventory Notification");
				lastInventoryState = event.getItemContainer().getItems();
				this.createAndQueueInvoNotification(lastInventoryState);
				return;
			}

			ItemContainer currentContainer = event.getItemContainer();
			boolean containersEqual = CommonUtility.areItemContainerEqual(lastInventoryState, currentContainer.getItems());
			if(containersEqual == false) {
				logger.debug("Detected changed inventory, prepare to create latest Inventory Notification");
				this.createAndQueueInvoNotification(event.getItemContainer().getItems());
				lastInventoryState = currentContainer.getItems();
			}
		}
	}

	@Subscribe
	public void onNpcLootReceived(final NpcLootReceived npcLootReceived) {
		logger.debug("Detected npcLootReceived from npc with id: " + npcLootReceived.getNpc().getId() +" , preparing to send event");
		this.createAndSendLootNotification(npcLootReceived);
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged) {
		Skill updatedSkill = statChanged.getSkill();
		int skillIdx = updatedSkill.ordinal();
		int lastLevel = lastSkillLevels[skillIdx];
		int currentLevel = client.getRealSkillLevel(updatedSkill);

		lastSkillLevels[skillIdx] = currentLevel;
		boolean didLevel = lastLevel != 0 && currentLevel > lastLevel;

		//Only send level stat changes after initial events have sent
		if(didLevel && hasTicked){
			logger.debug("Level up detected, preparing to queue event: " + updatedSkill.getName() + " lvl: " +  currentLevel);
			this.createAndSendLevelNotification(updatedSkill.getName(), currentLevel);
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded) {

		int groupId = widgetLoaded.getGroupId();
		logger.debug("Widget loaded: {}", groupId);
		if (!IsLoggedIn() || !IsSupportedWidget(groupId)) {
			return;
		}
		logger.debug("Supported widget: {}", groupId);
		this.createAndSendWidgetNotification(widgetLoaded);
	}

	private boolean IsLoggedIn() {
		return client.getGameState() == GameState.LOGGED_IN;
	}

	private boolean IsSupportedWidget(int WidgetId) {
		logger.debug("WidgetID: " + WidgetId);
		WIDGET_IDS widget = WIDGET_IDS.fromWidgetId(WidgetId); // This will return the corresponding WIDGET_IDS or null
		return widget != null; // Return true if the widget is found, false otherwise
	}




	private void createAndSendLootNotification(NpcLootReceived lootReceived) {
		// Log that loot notification is being prepared
		logger.debug("Preparing loot notification for NPC: {}", lootReceived.getNpc().getName());

		Player localPlayer = client.getLocalPlayer();
		String userName = localPlayer.getName();
		int geTotalPrice = 0;
		int npcId = lootReceived.getNpc().getId();
		List<Item> items = new ArrayList<>();

		// Loop through each item in the loot
		for (ItemStack itemStack : lootReceived.getItems()) {
			int itemId = itemStack.getId();
			int itemQuantity = itemStack.getQuantity();
			int itemPrice = 0;
			int totalItemPrice = 0;
			String itemName = itemManager.getItemComposition(itemStack.getId()).getName();

			// Log each item's ID, quantity, and total price
			logger.debug("Item ID: {}, Quantity: {}, Item Price: {}, Total Item Price: {}",
					itemId, itemQuantity, itemPrice, totalItemPrice);

			items.add(new Item(itemId, itemQuantity));
			geTotalPrice += itemStack.getQuantity() * itemManager.getItemComposition(itemStack.getId()).getPrice();
		}

		// Log the total price of the received loot
		logger.debug("Total GE Price of Loot: {}", geTotalPrice);

		// Create and send the loot notification
		NpcKillNotification notification = new NpcKillNotification(npcId, items, geTotalPrice, userName);

		// Log before sending the notification
		logger.debug("Sending loot notification for NPC ID: {}, Player: {}", npcId, userName);

		messageHandler.sendEventNow(MESSAGE_EVENT.LOOT, notification);

		// Log that the notification has been sent
		logger.debug("Loot notification sent successfully for NPC ID: {}, Player: {}", npcId, userName);
	}

	private void createAndSendWidgetNotification(WidgetLoaded widgetLoaded) {
		// Log that widget notification is being prepared
		logger.info("Preparing widget notification for Widget ID: {}", widgetLoaded.getGroupId());

		// Get the local player and check if they are valid
		Player localPlayer = client.getLocalPlayer();
		if (localPlayer == null) {
			logger.error("Local player not found. Unable to send widget notification.");
			return;
		}
		String userName = localPlayer.getName();

		// Get the widget ID from the loaded widget
		int widgetId = widgetLoaded.getGroupId();

		// Find the corresponding WIDGET_IDS enum based on the widget ID
		WIDGET_IDS widget = WIDGET_IDS.fromWidgetId(widgetId);
		if (widget == null) {
			logger.error("Widget not supported: {}", widgetId);
			return;
		}

		ItemContainer container = client.getItemContainer(widget.getInventoryId());
		if (container == null) {
			return ;  // Return an empty list if no items found
		}


		List<Item> items = new ArrayList<>();
		int totalItemPrice = 0;
		// Loop through each item in the loot
		for (Item item : container.getItems()) {
			int itemId = item.getId();
			int itemQuantity = item.getQuantity();

			// Initialize the item price and total price variables
			int itemPrice = itemManager.getItemPrice(itemId);
			totalItemPrice = itemPrice * itemQuantity;
			String itemName = itemManager.getItemComposition(itemId).getName();

			// Log each item's ID, quantity, and total price
			logger.debug("Item ID: {}, Name: {}, Quantity: {}, Item Price: {}, Total Item Price: {}",
					itemId, itemName, itemQuantity, itemPrice, totalItemPrice);

			// Add the item to the list of items
			items.add(new Item(itemId, itemQuantity));

		}


		// Log the supported widget
		logger.debug("Found supported widget: {}", widget.getName());

		// Create the widget notification
		LootNotification notification = new LootNotification(widgetId, items, totalItemPrice, userName);

		// Log the sending of the notification
		logger.debug("Sending widget notification for Widget ID: {}, Player: {}", widgetId, userName);

		// Send the notification
		messageHandler.sendEventNow(MESSAGE_EVENT.LOOT, notification);

		// Log that the notification was sent successfully
		logger.debug("Widget notification sent successfully for Widget ID: {}, Player: {}", widgetId, userName);
	}




	private void createAndQueueBankNotification(){
		//Ensure bankContainer is valid
		if(lastBankContainer == null){
			return;
		}

		Item[] bankItems = lastBankContainer.getItems();
		List<BankItem> items = new ArrayList<>();
		int totalPrice = 0;

		for(Item bankItem : bankItems){
			int id = bankItem.getId();

			// Skip invalid item ids
			if (id <= -1){
				continue;
			}

			int quantity = bankItem.getQuantity();
			ItemComposition itemComp = itemManager.getItemComposition(id);

			// Handle placeholder quantity
			boolean isPlaceholder = itemComp.getPlaceholderTemplateId() == 14401;
			if (isPlaceholder) {
				quantity = 0;
			}

			int gePrice = quantity * itemComp.getPrice();
			totalPrice += gePrice;

			BankItem bankItemToAdd = new BankItem(id, quantity);
			items.add(bankItemToAdd);
		}

		logger.debug("Preparing to send bank notification to message handler");
		BankNotification bankNotification = new BankNotification(items, totalPrice);
		messageHandler.updateLatestEvent(MESSAGE_EVENT.BANK, bankNotification);
	}

	private void createAndSendLevelNotification(String name, Integer level){
		logger.debug("Preparing to send level notification to message handler");
		Map<String, Integer> levelMap = this.getLevelMap();
		LevelChangeNotification levelEvent = new LevelChangeNotification(name, level, levelMap);
		messageHandler.sendEventNow(MESSAGE_EVENT.LEVEL, levelEvent);
	}

	private void createAndSendQuestNotification(String quest, QuestState state){
		logger.debug("Preparing to send quest status to message handler");
		List<QuestInfo> quests = this.getQuestInfoList();
		QuestChangeNotification questEvent = new QuestChangeNotification(quest, state, quests, client.getVar(VarPlayer.QUEST_POINTS));
		messageHandler.sendEventNow(MESSAGE_EVENT.QUEST, questEvent);
	}

	private void createAndQueueInvoNotification(Item[] invoItems){
		logger.debug("Preparing to send inventory notification to message handler");
		List<Item> inventoryItems = new ArrayList<>();
		final int MAX_INVO_SLOTS = 28;
		int geTotalPrice = 0;
		for (int i = 0; i < MAX_INVO_SLOTS; i++){
			if(i > invoItems.length - 1){
				inventoryItems.add(new Item(0,0));
			}else if(invoItems[i].getId() > 0 && invoItems[i].getQuantity() > 0) {
				geTotalPrice += invoItems[i].getQuantity() * itemManager.getItemComposition(invoItems[i].getId()).getPrice();
				inventoryItems.add(invoItems[i]);
			}else{
				inventoryItems.add(new Item(0, 0));
			}
		}

		InventorySlotsNotification invoEvent = new InventorySlotsNotification(inventoryItems, geTotalPrice);
		messageHandler.updateLatestEvent(MESSAGE_EVENT.INVO, invoEvent);
	}

	private void createAndQueueEquipmentNotification(Item[] equippedItems){
		logger.debug("Preparing to send equipment notification to message handler");
		EquipmentInventorySlot[] slots = EquipmentInventorySlot.values();
		Map<EquipmentInventorySlot, Item> equipped = new HashMap<>();

		//Extract each slot and item being worn in the slot
		for(int i = 0; i < slots.length ; i++){
			if(slots[i].getSlotIdx() >= equippedItems.length){
				continue;
			}

			Item item = equippedItems[slots[i].getSlotIdx()];

			//Ensure there is a valid item being worn, else don't attach it
			if(item.getId() > 0 && item.getQuantity() > 0) {
				equipped.put(slots[i], item);
			}
		}

		EquipSlotsNotification equipEvent = new EquipSlotsNotification(equipped);
		messageHandler.updateLatestEvent(MESSAGE_EVENT.EQUIPMENT, equipEvent);
	}

	private Map<String, Integer> getLevelMap(){
		Map<String, Integer> levelMap = new HashMap<>();
		Skill[] skills = Skill.values();
		for (Skill iskill : skills){
			levelMap.put(iskill.getName(), client.getRealSkillLevel(iskill));
		}
		return levelMap;
	}

	private List<QuestInfo> getQuestInfoList(){
		ArrayList<QuestInfo> questInfo = new ArrayList<>();
		for (Quest quest : quests){
			questInfo.add(new QuestInfo(quest.getName(), quest.getId(), quest.getState(client)));
		}
		return questInfo;
	}

	//@Subscribe
	//public void onPlayerStatusChanged(GameTick event){
	//	this.createAndSendPlayerStatusNotification();
	//}

	private void createAndSendPlayerStatusNotification(){
		Player temp_player = client.getLocalPlayer();

		String userName = temp_player.getName();
		AccountType accountType = client.getAccountType();
		int combatLevel = temp_player.getCombatLevel();
		int currentWorld = client.getWorld();
		WorldPoint currentWorldPoint = temp_player.getWorldLocation();
		int currentHealth = client.getBoostedSkillLevel(Skill.HITPOINTS);
		int maxHealth = client.getRealSkillLevel(Skill.HITPOINTS);
		int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
		int maxPrayer = client.getRealSkillLevel(Skill.PRAYER);
		int currentRun = client.getEnergy();
		int currentWeight = client.getWeight();

		PlayerStatusNotification notification = new PlayerStatusNotification(userName, accountType, combatLevel, currentWorld,
				currentWorldPoint, maxHealth, currentHealth, maxPrayer, currentPrayer, currentRun, currentWeight);
		messageHandler.sendEventNow(MESSAGE_EVENT.PLAYERSTATUS, notification);
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
