package io.github.btarg.javaOpenAI.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GiveItemsJSONObject {
    @JsonProperty("player_uuid")
    private String playerUuid;
    @JsonProperty("items")
    private Item[] items;

    public String getPlayerUuid() {
        return playerUuid;
    }
    public Item[] getItems() {
        return items;
    }
}
