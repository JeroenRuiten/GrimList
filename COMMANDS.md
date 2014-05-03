Command | Function | Aliases
:-: | :-- | :-:
/whitelist | _GrimList's main command. Opens help menu._ | wl, grimlist, gl
/whitelist add [player] | _Adds player to the whitelist._ | -a
/whitelist remove [player] | _Removes player from the whitelist._ | -r
/whitelist delete [player] | _Entirely deletes the record from player data._ | -d
/whitelist view [player] | _View recorded information for the player._ | -v
/whitelist getid [player] | _Gets UUID of a player._ | -g
/whitelist help [topic] | _Displays useful help pages._ | -h
/whitelist set [setting] | _Allows you to modify the settings in-game._ | -s
/whitelist import [focus] | _Allows you to import from a focus._ | -i
/whitelist export [focus] | _Allows you to export to a focus._ | -e

__Valid Example Commands:__

/gl -v ferusgrim - _(Would show information on the player 'ferusgrim')_

/wl -s notify console true - _(Would set the Notify.Console boolean to true)_

/whitelist getid ferusgrim - _(Would return the UUID for the player 'ferusgrim')_

/grimlist -a ferusgrim - _(Would add the player 'ferusgrim' to the whitelist)_

### Notes
__Adding a player:__
_Adding a player is self-explanatory. You change a player's "isWhitelisted" setting to true. That's it. If a player log doesn't exist, one will be made._

__Removing a player:__
_Removing a player is very similar to adding a player. If you have "SaveQueries" set to true in the configuration, attempting to remove a player who isn't in record will add that player to the record (and set "isWhitelisted" to false)._

__Deleting a player record:__
_Extreme method of removal. Not recommended. You cannot retrieve a player record that has been deleted._

__Viewing a player:__
_Simply outputs any player data on record. If a record for the player doesn't exist, and "SaveQueries" is set to true in the configuration, one will be created and output._

__Getting a UUID:__
_Will output the UUID of a given player. If this player exists in record, it will not do a UUID lookup, but rather grab the information from there. If the player record doesn't exist, and "SaveQueries" is set to true in the configuration, one will be created and the UUID output._

__Getting help:__
_Using this command without an argument (as well as the main command) will output the help menu. Arguments are simply the command names themselves. Inputting them will output that command's help page._

__Setting Configuration:__
_This isn't... really recommended. It definitely works, but editing a file by hand is just so much easier. However, not everyone wants to reload the server every time a config file changes... so, that's where this comes in. Right now, you can only edit the main booleans._

__Import and Export:__
_Converting between sources can be difficult, sometimes. But it doesn't have to be! GrimList makes it as simple as possible. If you wish to put the player data from the focus you're currently using into another one, you're exporting. Importing is the opposite. To prevent issues, GrimList will detect if a UUID is currently already set in the focus that is being edited, and skip over it to the next one. For a COMPLETE import/export, the focus that's being edited must be empty of any entries._