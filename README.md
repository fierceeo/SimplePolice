Thanks for checking out my police plugin. Here is some information on how to use this.

**Usage**

To add a police do /police admin police add (username)

To jail a player, make sure you are a police and attack a player with a blaze rod

**Features**

Here is a list of things this plugin can do:
1. Jail Players
2. Frisk Players
3. Police Chat
4. 911 System
5. Police Teleport
6. Api for hooking into

Jailing System:

Attack a player with the jail item (default is a blaze rod)

Frisking system:

A frisking system allows you to scan players for contraband items.
Things like the precent chance of an item being detected and the material for the frisk stick can be configured in the config file.
The frisking system supports custom items, and has support for quality armory guns.


911 System:

Players can dial 911 using /911 (message) or just simply /911 which notifies all the police on the server


Police Tp:

Police can teleport to players within a configuged radius of them which notifies the player.


Police Chat:

Police can chat with /police chat

**Permissions**

SimplePolice.cmd.admin.help:

SimplePolice.cmd.admin.reload:

SimplePolice.cmd.admin.frisk.add:

SimplePolice.cmd.admin.frisk.remove:

SimplePolice.cmd.admin.frisk.list:

SimplePolice.cmd.admin.police.add:

SimplePolice.cmd.admin.police.remove:

SimplePolice.cmd.admin.jails.add:

SimplePolice.cmd.admin.jails.remove:

SimplePolice.cmd.jail:

SimplePolice.cmd.tp:

SimplePolice.cmd.release:

SimplePolice.cmd.chat:

**Commands**

/police help - displays all the police commands

/police admin help - displays all the admin commands

/911 - calls the police

**API**

Maven Repo:

	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>

 	<dependency>
	    <groupId>com.github.ramdon-person.SimplePolice</groupId>
	    <artifactId>SimplePolice-API</artifactId>
	    <version>5.0</version>
	</dependency>

Java docs:
https://ci.codemc.io/job/fierceeo/job/SimplePolice/JavaDoc/

Getting the api instance:

SimplePoliceAPI api = (SimplePoliceAPI) Bukkit.getPluginManager().getPlugin("SimplePolice");

Note: your plugin will have to depend (or soft depend) on SimplePolice to use the api

**Support:**

If you need support please join my discord server: https://discord.gg/rxzHRHcC7W
