# **EventsAPI Plugin for RuneLite** 
### _by Morgan Van V._

**NOTE: This plugin was heavily inspired by the OSRSEvents Plugin by llamaXc [(REPO)](targetURL="https://github.com/llamaXc/osrs-events")**
    
    https://github.com/llamaXc/osrs-events

EventsAPI simply builds off and adapts their foundation for my uses, so credit to llamaXc as well as all of their contributors

## How to use?
- Install via Plugin Hub in RuneLite
- Navigate to General Settings for `EventsAPI`
- Enter a valid `Endpoint` which can receive these POST HTTP Requests
- Select a `Tick Delay` to determine how often periodic emissions occur (currently only used for PlayerStatus)
- Navigate to the `Customize Event Emissions` settings category, and select which emissions you wish to occur
- Optionally: Navigate to `Advanced Settings` and enter a Bearer Token, which is used to identify the sender of data without using sensitive information

## Security & Privacy
This plugin is customizable by the user. You choose where your data goes, and have varying levels of configuration.
#### Configuration Options & Outline of Data being emitted to API
- Attach Player Info: `UserName, CombatLevel, MapPosition` (Attached to other requests)
- Periodically Emit Player Status: `currentHealth, currentPrayer, currentRunEnergy, currentWeight`
- Emit Monster Kills: `npcInfo, lootInfo`
- Emit Level Change Updates: `allLevels`
- Emit Equipped Items: `equippedItemsList`
- Emit Inventory Items: `inventoryItemsList`
- Emit Bank Items: `bankItemsList`
- Emit Quest Info: `questPoints, questStates`
- Emit Login State: `loginStatus`

# Use Cases / Goals
The simple goal of this plugin is to be able to have a customizable amount of information that is POSTed from the
RuneLite client to a custom API endpoint for the purpose of social progress tracking, efficiency analysis,
general logging, as potentially even tournament purposes.

More information will come on the README in the future as this project becomes more defined in scope and purpose

### TODO:

- make it even more betterer
- do work on the logging / information representation side to identify potential future data category additions
- explore if there is a way to generate this type of stuff dynamically, give user full control on what triggers their
    requests and what data they contain
- potentially offer a premade Python/Flask script for handling/storing these requests for less technical users

### Future Features / Potential Use Cases

- Personal Blog style webpage that shows a live status of your character (location, skills, etc.)
- Tracking progress similar to CrystalMathLabs, but done locally for the enterprising developer / data scientist (me)
- Efficiency Analysis of training methods through logged events
- Generating datasets for analysis