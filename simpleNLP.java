import java.util.*;
import java.io.*;

public class simpleNLP {
    private Map<String, String> responses;

    public simpleNLP() {
        responses = new HashMap<String, String>();

        loadResponses();
    }

    private void loadResponses() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("responses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    responses.put(parts[0].trim().toLowerCase(), parts[1].trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading response file: " + e.getMessage());
        }
    }

    public String getResponse(String input) {
        input = input.toLowerCase().trim();
        if (input.contains("hello") || input.contains("hi") || input.contains("hey")) {
            return "Hello! How can I help you today?";
        } else if (input.contains("bye") || input.contains("goodbye")) {
            return "Goodbye! Have a great day!";
        } else if (input.contains("thank")) {
            return "You're welcome!";
        } else if (input.contains("how are you")) {
            return "I'm a bot, so I'm always good!";
        } else if (input.contains("your name")) {
            return "I am your friendly Java Chatbot.";
        } else {
            String response = responses.get(input);
            if (response != null) {
                return response;
            }
            return "Sorry, I don't understand that.";
        }
    }
}
