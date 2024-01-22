# CraftGPT
CraftGPT uses Langchain4J and OpenAI's "tools" to interact with a Minecraft world as "God".

It will be able to do things like:
- Understand the context of the world, including the player's inventory, location, etc.
- Respond to prompts in chat
- Give and take items from players

## World Log
In order to understand the world, CraftGPT will need to log many different events so that GPT-3 can respond to them.
As of writing, this is not implemented, but instead GPT-3 is given prompts by players via the in-game chat.