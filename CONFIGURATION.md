Configuration Options | What does it do? | Default
:-: | :-- | :-:
Whitelist: | _Determines whether or not checks are made against the focus, when a player joins the server. If set to false, player data will still be gathered._ | true
Metrics: | _A nifty tool that allows me to keep tabs on how the plugin is used! This way I can see how the plugin is being used, to better develop features in the future._ | true
Updater: | _This utility will, when enabled, inform a player with the appropriate permissions that there is an update available, when they join the server. It will also display a notice on server startup. This does NOT download anything to your server._ | true
DebugMessages: | _Will enable debugging. This will display information in the logs which you or I can use to help narrow down an issue you're experiencing, leading to faster resolution time._ | false
SaveQueries: | _Some queries do not natively save player data, such as removing a player from a whitelist, but they don't have a record. If this is set to true, the username/uuid of the player will be saved, saving time in the future._ | true
KickRemove: | _KickRemove will kick a player from the server if they're connected when they're removed from the whitelist, or have their record deleted._ | true
AlwaysLookup: | _AlwaysLookup will bypass record checking, and go straight to UUIDFetcher. Not using AlwaysLookup does NOT allow players to bypass the whitelist check. | false
Focus: | _The focus is the data source that GrimList pays attention to when writing player data, or checking if a player is whitelisted. Currently, this can be 4 things: 'file', 'mysql', 'sqlite', and 'url'._ | file
Notify.Console: | _Show a message on the console, when a player is denied access to the server. Not recommended for larger servers, to prevent log spam._ | false
Notify.Player: | _Show a message to a player with the appropriate permissions, when a player is denied access to the server._ | true
MySQL.host: | _Set the MySQL host._ | localhost
MySQL.port: | _Set the Port that the MySQL server listens to._ | 3306
MySQL.database: | _Set the MySQL database._ | grimlist
MySQL.username: | _Set the MySQL username._ | root
MySQL.password: | _Set the MySQL password._ | toor
Url.link: | _Link to the remote player data file._ | http://your.pd/playerdata.yml
Url.update-interval: | _Interval between which this link should be checked for / save updates._ | 120