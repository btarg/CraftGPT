package io.github.btarg.javaOpenAI;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskExtractor {
    public static List<String> extractTasks(String message) {
        // Use OpenNLP SimpleTokenizer to tokenize the message
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(message);

        // Create a list to store extracted tasks
        List<String> tasks = new ArrayList<>();

        // Iterate over the tokens and extract tasks
        for (Span span : extractTaskSpans(tokens)) {
            // Extract tokens between start and end indices of the current span
            String[] spanTokens = Arrays.copyOfRange(tokens, span.getStart(), span.getEnd());

            // Join the span tokens into a single string
            String task = String.join(" ", spanTokens);

            // Add non-empty tasks to the list
            if (!task.isEmpty() && !tasks.contains(task)) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    private static Span[] extractTaskSpans(String[] tokens) {
        // Logic to identify task spans, you may need to customize this based on your requirements
        // For simplicity, this example considers any non-stopword as a task
        List<Span> taskSpans = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (!isStopword(tokens[i])) {
                int start = i;
                while (i < tokens.length && !isStopword(tokens[i])) {
                    i++;
                }
                int end = i;
                taskSpans.add(new Span(start, end));
            }
        }

        return taskSpans.toArray(new Span[0]);
    }

    private static boolean isStopword(String token) {
        // Example: Check if the token is a stopword (you might want to use a more comprehensive stopword list)
        return token.equalsIgnoreCase("and then") || token.equals(",") || token.equals(";") || token.equals("and") || token.equals("then");
    }
}
