# midnight
Full rewrite of Night Bot.

## Setup
You can download the bot's jar file from the [releases page](https://github.com/RyanAnayaMc/midnight/releases). This bot requires you to make your own Discord bot user. To do this, go to the [Discord Developer Portal](https://discord.com/developers/applications), create a new application, and set it up as a bot user. You will need to copy the bot token (do not share it with anyone!) and put it in a file called `token.dat`. Place this file in the `data/` folder, so the directory structure looks like this:

(main directory)/midnight-version.jar

(main directory)/data/token.dat

## Features
Most features from my original bot are planned to be added. Some are finished, some are in progress, some haven't been started yet.

### Completed Featrues
#### Message Interceptor System
This feature caches up to 2000 messages (will be user-definable later) locally, including attachments. If the bot detects a message delete, then it will DM server moderators the contents of the deleted message, including attachments. If the bot detects a message edit, it will DM the server moderators the contents of the original message. This will fail if the message is not in the cache (i.e. it got replaced since the cache has more than 2000 messages, or the message was sent before the bot was started). Moderators can opt-in and opt-out from this system with the slash command `/viewlog`.

#### Moderator Ban Commands
This feature allows moderators to easily apply temporary bans to users. Three types of bans are supported - message bans, image bans, and voice bans. These temporarily restrict a user's ability to send messages, send embedded links/files, or join voice channels, respectively. These roles are not set up by the bot - they must be set up by the administrator. Once these roles are set up, an administrator can bind these roles to the commands with the `/setmsgban`, `/setimgban`, and `/setvcban` slash commands. Afterwards, moderators can use the `/msgban`, `/imgban`, or `/vcban` slash commands to temporarily apply one of these bans on a user for a predetermined amount of time, up to 7 days. The banned user will be notified about the ban via DMs. The bot will manage removing the role at the specified time. __Note: The role will not be removed if the bot has been restarted before the role was removed. This will be fixed in a later update.__

### WIP Features
#### Voice Chat Intros
Configurable intro tracks that activate when a user joins a voice chat.
- Plays a random track from a user-defined list
- User/moderator configurable
- Moderator manageable
- Ignores certain voice channels

### Planned Features
#### Automated Bot Responses
Widely configurable automated bot responses based on a variety of conditions and triggers:
- Message contains a certain word
- Message was sent by a certain user
- Message was not sent by a certain user
- Message matches a given regex
- Ignores certain text channels

#### Server Join Message
Sends a customizable DM message to users who join a server.

#### Automatic Username Changer
Just a fun little feature to troll friends with. Automatically changes a user's server nickname to a name from a pre-determined list when a user joins the server or changes their username. 

#### Manual Customization
Administrators can download their server's JSON configuration file and tweak it manually themselves, and reupload it. Will include measures to ensure that the JSON file is valid. Powerful feature for power users!

## Libraries
[Java Discord API (JDA)](https://github.com/DV8FromTheWorld/JDA)

[Quartz Scheduler](https://github.com/quartz-scheduler/quartz)

## About
Almost a year ago, I wrote a Discord Bot called Night Bot as a fun little personal project. This was my first time writing a bot, so many shortcuts were taken and overall the code was messy and unoptimized. There was some drastic restructuring and rewriting I wanted to do to it, but I decided to rewrite the bot from scratch because it would be easier, and I would learn Git while I was at it. Behold, the creation of midnight.
