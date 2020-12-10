# DeathLog Plugin for Minecraft/Bukkit
A logging plugin that records the deaths and logouts of players for the rescuing of players or player items.

## Screenshots
![DeathLog Command](https://user-images.githubusercontent.com/10486660/101585652-1038cb80-3995-11eb-8b51-a44cf27dac9c.png)
The plugin was renamed since the creation of the screenshot. The "DS" was changed to "DL."

## Features
- Logs a player's coordinates and health when they log out or dies
- Prints log entry into the console when one a player logs out or dies
- Stores death and logout information for individual per-player files (in the plugins folder)
- Logs accessible by commands
    - See other players' logs with certain permissions
        - Uses Fuzzy Matching for player search
- Configure who can see which logs through permissions
- Great for small, lightweight SMP servers 

## Commands
- See my log: /dl
- See other people's log: /dl \<PlayerName\>

## Permissions
- deathlog.*
- deathlog.access
- deathlog.all

## Requirements
- Bukkit/Spigot/PaperSpigot
- Minecraft version 1.13+ (only tested on 1.16+)

## Installation
- Put the JAR file in the plugins folder
- Start the server and report any potential bugs

## How it works
- On death and logout events, a death log entry will be added to the file corresponding to the player's name
- When a command is called, the death log will print out the corresponding file into the chat

## Ideas for Future Updates
- When running the command, it will print every single log file
    - Make only the latest few entries show on command (rather than chat spam)
- When adding an entry to a file, add to the top instead of appending
- Make older entries expire after a certain time or log usage
    - Currently, the log files will become very large after a lot of log-outs
- Check for updates?