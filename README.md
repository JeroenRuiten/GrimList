![Logo](https://raw.githubusercontent.com/FerusGrim/GrimList/master/grimlistv3.png "GrimList v3!")

# GrimList

### A better approach to whitelisting!

__For now, the only valid focus is saving to file. MySQL, SQLite, and URL support will be added back when I've done further bug-testing.__

GrimList is a whitelisting program for Bukkit, designed with ease of use in mind. It allows you to manage your server
in a variety of ways, ranging from a remote file to a sqlite database. Once you configure (or dont!) management of
your server's whitelist becomes so simple, you almost forget it's a complicated plugin, under the hood. Choosing to
add or remove players from the whitelist is just scratching the surface!

Used to refreshing your whitelist, after you add or remove a player? Don't worry about it! GrimList handles all of
this automatically (and immediately)!

Worried about the upcoming UUID changes? Don't! GrimList is already ahead of the curve, and fully compatible with UUIDs.

Have you heard horror stories about a UUID lookup freezing the server? Ha! GrimList handles any UUID lookup on an
alternate thread, leaving your server unaffected!

In short, GrimList is the premier whitelisting plugin. I challenge you to find an easier to use, or better designed solution.

### About the YMLs:
[config.yml](https://github.com/FerusGrim/GrimList/wiki/Configuration "Configuration Wiki") - _The config.yml is intended to be streamlined for use!_

[playerdata.yml](https://github.com/FerusGrim/GrimList/wiki/PlayerData "PlayerData Wiki") - _Be careful if you want to edit this! I've warned you!_

### Commands and Permissions:
[Commands](https://github.com/FerusGrim/GrimList/wiki/Commands "Commands Wiki") - _There are more commands than you might think. Take a look!_

Permissions | Functions
:-: | :--
grimlist.* | _Grants all permissions._
grimlist.add | _Allows player to add others to the whitelist._
grimlist.remove | _Allows player to remove others from the whitelist._
grimlist.delete | _Allows player to completely delete another player record._
grimlist.view | _Allows a player to view another player's player data._
grimlist.getid | _Allows a player to gather another player's UUID._
grimlist.set | _Allows a player to change configuration in-game._
grimlist.help | _Allows a player to view the help menu._
grimlist.update | _Shows notification of update on join._
grimlist.notify | _Notifies player on failed join attempt._

### Other Stuff!:
[Dynamic Effect Whitelist](http://dev.bukkit.org/bukkit-plugins/dynamic-effect-whitelist/ "Dynamic Effect Whitelist") - _DEW inspired GrimList. No code from DEW is used in GrimList, anymore._
