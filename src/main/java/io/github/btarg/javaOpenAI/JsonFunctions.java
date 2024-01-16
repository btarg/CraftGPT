package io.github.btarg.javaOpenAI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.btarg.javaOpenAI.objects.GiveItemsJSONObject;

public class JsonFunctions {
    static void givePlayerItem(JsonNode functionArgs) {
        // Extract values from the functionargs
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GiveItemsJSONObject giveItemsJSONObject = objectMapper.treeToValue(functionArgs, GiveItemsJSONObject.class);
            for (int i = 0; i < giveItemsJSONObject.getItems().length; i++) {
                System.out.println(giveItemsJSONObject.getItems()[i].getItemName());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    static void helloWorld(JsonNode functionArgs) {
        String playerName = functionArgs.at("/player_name").asText();
        System.out.println("Hello, " + playerName + "!");
    }
}
