# midnight

Full rewrite of Night Bot, a multi-purpose Disord bot.

## Features
### Slash Commands
No need to use `/help` each time you forgot what that one command was. Midnight exclusively uses slash commands for its user interactions. Discord slash commands allow users to see a list of commands supported by a bot, including easy details on what arguments a command takes and what each argument does. And don't worry - the outputs for most commands are only visible to you, so your server members can't see what commands you're running.

### Moderation Commands
Simple moderation commands make it easy for your moderators to temporarily restrict a member's ability to send messages, send images, or join voice chats for a specified amount of time. Input the command and the bot will take care of the rest - including assigning the ban and removing it once the ban has expired.
- Administrators can set the roles associated with each ban with `/setmsgban`, `/setimgban`, and `/setvcban`.
- Moderators can administer these bans with `/msgban`, `/imgban`, and `/vcban`
- These commands take parameters for hours and minutes to ban a user, up to a maximum of 7 days in one ban
- These commands also take an optional parameter for a ban reason
- The banned user will be informed via DM about the type of ban, duration of the ban, who banned them, and the reason for the ban (if provided)

### Message Interceptor
A robust system to cache incoming messages, including their attachments, and retreive them if that message has been deleted or edited from Discord. Moderators can opt-in to this message log so they can be informed via DM about any message edits and deletes in the server. The DM will include the original message if it was found in the cache.
- Caches up to 2000 of the most recent messages
- Moderators can easily opt in or out with `/viewlog`
- All attachments are sent too if the original message was deleted

### User Intros
A fun feature that allows users to upload their own intros and outros to play when they join or leave a voice chat. Users can customize their own set of intros with a powerful set of options, including separate intros and outros, custom volumes for each intro/outro, and a custom weight for each intro. Users can add mulitple intros, and a random intro will be selected based on a weighted probablility.
- User-uploadable intros and outros via `/addintro` and `/addoutro` commands
- Easily remove unwanted intros and outros with `/removeintro` and `removeoutro`
- Easily change the volume and/or weight for an intro/outro with `/editintro` or `/editoutro`
- See all the details for your intros and outros with `/listintros`
- User-definable weights for each intro to affect probability
- Supports MP3 and WAV files up to 2MB

### Other Commands
- `/migrate` command to move all users in your voice channel to another voice channel (moderators only)
- `/ping` command to test the bot's ping to Discord

## Planned Features
### Automated Bot Responses
Widely configurable automated bot responses based on a variety of conditions and triggers:
- Message contains a certain word
- Message was sent by a certain user
- Message was not sent by a certain user
- Message matches a given regex
- Ignores certain text channels

### Server Join Message
Sends a customizable DM message to users who join a server.

### Automatic Username Changer
Just a fun little feature to troll friends with. Automatically changes a user's server nickname to a name from a pre-determined list when a user joins the server or changes their username. 

### Manual Customization
Administrators can download their server's JSON configuration file and tweak it manually themselves, and reupload it. Will include measures to ensure that the JSON file is valid. Powerful feature for power users!

## About Midnight
Almost a year ago, I wrote a Discord Bot called Night Bot as a fun little personal project. This was my first time writing a bot, so many shortcuts were taken and overall the code was messy and unoptimized. There was some drastic restructuring and rewriting I wanted to do to it, but I decided to rewrite the bot from scratch because it would be easier, and I would learn Git while I was at it. Behold, the creation of midnight.
