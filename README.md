# CraftGPT
CraftGPT uses Langchain4J and OpenAI's "tools" to interact with a Minecraft world as "God".

It will be able to do things like:
- Understand the context of the world, including the player's inventory, location, etc.
- Respond to prompts in chat
- Give and take items from players

# TODO
## Initial prompts
While persistent memory is already implemented, "character prompts" to remind the AI of who it is and what it's doing are not.

I want the AI to act as a God, who will choose to do things based on the player's actions. For example, if the player is building a house, the AI might choose to give them some wood. Or, if it is feeling mischievous, it might choose to spawn a creeper. Events should be chosen based on how entertaining they could be.

As part of implementing entertaining functions, I'll look into porting over some of the functions from classic [Twitch Vs Minecraft.](https://github.com/btarg/TwitchVsMinecraft2)

## World Log
In order to understand the world, CraftGPT will need to log many different events so that GPT-3 can respond to them.
As of writing, this is not implemented, but instead GPT-3 is given prompts by players via the in-game chat.