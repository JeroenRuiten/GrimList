!(https://raw.githubusercontent.com/FerusGrim/GrimList/master/grimlistv3.png "GrimList v3!")

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
[config.yml](https://github.com/FerusGrim/GrimList/wiki/Configuration "Configuration Wiki")
[playerdata.yml](https://github.com/FerusGrim/GrimList/wiki/PlayerData "PlayerData Wiki")

### Commands and Permissions:
[Commands](https://github.com/FerusGrim/GrimList/wiki/Commands "Commands Wiki")

Permissions | Functions
:-: | :-:
*Still* | `renders`
grimlist.* | Grants all permissions.
grimlist.add | Allows player to add others to the whitelist.
grimlist.remove | Allows player to remove others from the whitelist.
grimlist.delete | Allows player to completely delete another player record.
grimlist.view | Allows a player to view another player's player data.
grimlist.getid | Allows a player to gather another player's UUID.
grimlist.set | Allows a player to change configuration in-game.
grimlist.help | Allows a player to view the help menu.
grimlist.update | Shows notification of update on join.
grimlist.notify | Notifies player on failed join attempt.

### Other Stuff!:
[Dynamic Effect Whitelist](http://dev.bukkit.org/bukkit-plugins/dynamic-effect-whitelist/ "Dynamic Effect Whitelist")
