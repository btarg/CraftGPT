package io.github.btarg.javaOpenAI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {

    // TODO:
    //  - Add a database to store previous responses
    //  - Have the AI give a comment on their actions (response to the function_call_responses)
    //  - To do that we will need to properly store the last responses

    private static final String openaiApiKey = System.getenv("OPENAI_API_KEY");

    // Map to store functions
    private static final Map<String, Command> commandMap = new HashMap<>();

    // Functional interface for commands
    @FunctionalInterface
    private interface Command {
        void execute(JsonNode functionArgs);
    }

    public static void main(String[] args) {
        // Add functions to the commandMap
        commandMap.put("givePlayerItem", JsonFunctions::givePlayerItem);
        commandMap.put("helloWorld", JsonFunctions::helloWorld);

        Javalin app = Javalin.create();

        // Define API endpoint
        app.post("/api/generate-response", Main::generateResponse);

        // Start the server
        app.start(7000);
    }


    private static void generateResponse(Context ctx) {
        try {
            // Read data from the request JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode requestData = objectMapper.readTree(ctx.body());

            String player_name = requestData.path("player_name").asText();
            String player_uuid = requestData.path("player_uuid").asText();
            String message_content = requestData.path("message_content").asText();

            // Maintain a list of previous responses
            List<String> previousResponses = new ArrayList<>();

            // Call OpenAI API
            var responses = callOpenAI(player_name, player_uuid, message_content, previousResponses);

            for (String openaiResponse : responses) {
                if (openaiResponse == null) {
                    ctx.status(500).result("Internal Server Error");
                } else {
                    // Add the current response to the list of previous responses
                    previousResponses.add(openaiResponse);

                    // Simulate Java method calls based on the OpenAI response
                    toolCallToCommand(openaiResponse);
                }
            }
            var responseObject = Map.of(
                    "function_call_responses", responses
            );
            ctx.status(200).result(objectMapper.writeValueAsString(responseObject));
            // TODO: the AI should now react to this response


        } catch (IOException e) {
            e.printStackTrace();
            ctx.status(400).result("Invalid request body");
        }
    }

    // Call OpenAI API
    private static List<String> callOpenAI(String playerName, String playerUuid, String messageContent, List<String> previousResponses) throws JsonProcessingException {
        HttpClient httpClient = HttpClient.newHttpClient();

        // a message object is a JSON object with the following format:
        //// {
        ////     "player_name": "btarg",
        ////     "player_uuid": "12345678-1234-1234-1234-123456789012",
        ////     "message_content": "Hello, World! Give me 5 diamonds and 10 iron ingots."
        //// }

        List<Map<String, String>> messageDatas = new ArrayList<>();
        List<String> responses = new ArrayList<>();

        for (String extractedTask : TaskExtractor.extractTasks(messageContent)) {

            // Add previous responses to the messageDatas for extra context
            for (String previousResponse : previousResponses) {
                var previousMessageObject = Map.of(
                        "role", "system",
                        "content", previousResponse
                );
                messageDatas.add(previousMessageObject);
            }

            String formatted = String.format("Player Name: %s\nUUID: %s\nTask: %s", playerName, playerUuid, extractedTask);
            System.out.println(formatted);
            var messageObject = Map.of(
                    "role", "user",
                    "content", formatted
            );
            messageDatas.add(messageObject);

            // load json string from file E:/Ai shenanigans/tools.json
            // Create an ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            Object[] jsonArray;
            // Read the JSON array from the file into a Java array
            try {
                jsonArray = objectMapper.readValue(new File("E:/Ai shenanigans/tools.json"), Object[].class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Prepare the request JSON
            var requestData = Map.of(
                    // set model
                    "model", "gpt-3.5-turbo",
                    // set messages
                    "messages", messageDatas.toArray(),
                    "tools", jsonArray,
                    // set max_tokens
                    "max_tokens", 150,
                    "temperature", 0.7
            );

            try {
                // Serialize request data to JSON
                String requestBody = new ObjectMapper().writeValueAsString(requestData);

                // Prepare the HTTP request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + openaiApiKey)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                // Send the HTTP request
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


                // Check the HTTP response status
                int statusCode = response.statusCode();
                System.out.println("HTTP Status Code: " + statusCode);

                // Return the response body
                responses.add(response.body());


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return responses;
    }

    // Simulate Java method calls based on the OpenAI response
    private static void toolCallToCommand(String openaiResponse) {
        try {
            // Parse the OpenAI response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseNode = objectMapper.readTree(openaiResponse);
            JsonNode toolCalls = responseNode.at("/choices/0/message/tool_calls");

            if (toolCalls.isArray()) {
                for (JsonNode toolCall : toolCalls) {
                    String functionName = toolCall.at("/function/name").asText();
                    JsonNode functionArgs = objectMapper.readTree(toolCall.at("/function/arguments").asText());
                    // Get the function from the map and run it
                    Command command = commandMap.get(functionName);
                    if (command != null) {
                        command.execute(functionArgs);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
