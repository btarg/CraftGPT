package io.github.btarg.javaOpenAI.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    @JsonProperty("item")
    private String itemName;

    @JsonProperty("count")
    private int itemCount;

    // Add getters (and setters if needed)
    public String getItemName() {
        return itemName;
    }

    public int getItemCount() {
        return itemCount;
    }
}
