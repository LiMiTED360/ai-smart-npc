package aiSmartNpc.helper;

import aiSmartNpc.Conversation;
import aiSmartNpc.Message;
import aiSmartNpc.NPC;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHelper {
    private static final Pattern CONTENT_PATTERN = Pattern.compile("\"content\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");

    public static String extractTextFromJSON(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return "[error] Received empty reply";
        }

        Matcher matcher = CONTENT_PATTERN.matcher(jsonResponse);

        String foundContent = null;
        while (matcher.find()) {
            foundContent = matcher.group(1);
        }

        if (foundContent != null) {
            return cleanUpAnswer(foundContent);
        }

        return "[error] Could not understand response, DEBUG - Could not read JSON: " + jsonResponse;
    }

    private static String cleanUpAnswer(String jsonResponse) {
        return jsonResponse
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\u00e4", "ä")
                .replace("\\u00c4", "Ä")
                .replace("\\u00f6", "ö")
                .replace("\\u00d6", "Ö")
                .replace("\\u00fc", "ü")
                .replace("\\u00dc", "Ü")
                .replace("\\u00df", "ß");
    }


    public static String makeSafeForJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "")
                .replace("\t", "\\t");
    }

    public static String makeContextForJson(List<Message> messages) {
        String output = "";

        for (Message message : messages) {
            if (message.isFromAI()) {
            output += "    {\"role\": \"" + "assist" + "\", \"content\": \"" + makeSafeForJson(message.getMessage()) + "\"},\n";
            } else {
            output += "    {\"role\": \"" + "user" + "\", \"content\": \"" + makeSafeForJson(message.getMessage()) + "\"},\n";
            }
        }

        return output;
    }

    public static String makeJason(NPC npc, Conversation conversation, String newMessage) {
        String talkingTo;
        if (conversation.getPlayername() != null) {
            talkingTo = conversation.getPlayername();
        } else {
            talkingTo = "";
        }


        String jsonBody = "{\n" +
                "  \"model\": \"" + conversation.getAiModel() + "\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"" + getSystempromt(npc) + talkingTo + "\"},\n" +

                //Adds Examples as Context for the AI
                makeContextForJson(conversation.getMessages()) +


                //Adds new message for the AI to Check
                "    {\"role\": \"user\", \"content\": \"" + makeSafeForJson(newMessage) + "\"}\n" +
                "  ],\n" +

                //Gives AI the Parameters
                "  \"temperature\": " + temperature + ",\n" +
                "  \"top_p\": " + top_p + ",\n" +
                "  \"max_tokens\": " + max_tokens + ",\n" +
                "  \"user\": \"" + user + "\"" +
                "}";
    }

    public static String getSystempromt(NPC npc) {

    }
}